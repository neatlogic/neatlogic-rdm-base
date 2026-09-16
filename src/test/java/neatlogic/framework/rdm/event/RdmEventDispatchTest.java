/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the Sustainable Use License (SUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.rdm.event;

import neatlogic.framework.asynchronization.thread.ModuleInitApplicationListener;
import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.asynchronization.threadpool.CachedThreadPool;
import neatlogic.framework.rdm.config.RdmConfig;
import neatlogic.framework.rdm.dao.mapper.RdmEventMapper;
import neatlogic.framework.rdm.dto.*;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static neatlogic.framework.rdm.event.RdmEventTestSupport.*;

/** 经过真实 AfterTransactionJob 和 AsyncTaskManager 的独立进程回归。 */
public class RdmEventDispatchTest {
    /** 覆盖提交、回滚、真实队列、多 worker 并发及异常隔离。 */
    public static void main(String[] args) throws Exception {
        initMessages();
        RdmEventObjectAdapterFactory.register(ADAPTER);
        ModuleInitApplicationListener.getModuleinitphaser().forceTermination();
        Properties properties = new Properties();
        for (String invalid : Arrays.asList("0", "-1", "bad")) {
            properties.setProperty("rdm.events.thread.count", invalid);
            new RdmConfig().loadConfig(properties);
            check(RdmConfig.RDM_EVENT_THREAD_COUNT() == 4, "非法配置未回退");
        }
        properties.setProperty("rdm.events.thread.count", "2");
        new RdmConfig().loadConfig(properties);
        CountDownLatch bothWorkers = new CountDownLatch(2);
        CountDownLatch releaseWorkers = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(3);
        CountDownLatch emptyQueried = new CountDownLatch(1);
        Set<Long> queried = ConcurrentHashMap.newKeySet();
        Set<Long> executed = ConcurrentHashMap.newKeySet();
        Set<String> threads = ConcurrentHashMap.newKeySet();
        AtomicInteger active = new AtomicInteger();
        AtomicInteger maximum = new AtomicInteger();
        register(new Handler() {
            /** 阻塞两个事件，直接证明两个 worker 已同时进入插件。 */
            protected IssueVo myTrigger(RdmEventHandlerVo config, RdmEventPluginVo plugin, IssueVo issue, RdmEventAuditVo audit, RdmEventStatusVo status) {
                check("触发时值".equals(issue.getName()), "发布后原对象变更污染了快照");
                executed.add(issue.getId());
                threads.add(Thread.currentThread().getName());
                int count = active.incrementAndGet();
                maximum.accumulateAndGet(count, Math::max);
                try {
                    if (issue.getId() <= 2L) {
                        bothWorkers.countDown();
                        check(releaseWorkers.await(10, TimeUnit.SECONDS), "等待释放 worker 超时");
                    }
                    if (issue.getId() == 1L) {
                        throw new IllegalStateException("预期的测试插件失败");
                    }
                    return issue;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new AssertionError(e);
                } finally {
                    active.decrementAndGet();
                    finished.countDown();
                }
            }
        });
        RdmEventMapper mapper = (RdmEventMapper) Proxy.newProxyInstance(RdmEventMapper.class.getClassLoader(),
                new Class<?>[]{RdmEventMapper.class}, (proxy, method, params) -> {
                    if (method.getName().equals("getEventAppById")) { return app((Long) params[0]); }
                    if (method.getName().equals("getHandlerByEvent")) {
                        Long appId = (Long) params[2];
                        queried.add(appId);
                        if (appId == 4L) {
                            emptyQueried.countDown();
                            return Collections.emptyList();
                        }
                        RdmEventHandlerVo row = config(1L, 0, "test");
                        row.setAppId(appId);
                        return Collections.singletonList(row);
                    }
                    if (method.getName().equals("getHandlerByParentId")) { return Collections.emptyList(); }
                    throw new AssertionError("非预期 Mapper 调用：" + method.getName());
                });
        try {
            TenantContext.init("rdm-event-test");
            RdmEventRegistry.register(EVENT);
            new RdmEventManager(mapper);
            TransactionSynchronizationManager.initSynchronization();
            RdmEventManager.doEvent(9L, 999L, EVENT, issue(999L));
            check(queried.isEmpty(), "提交前查询了插件配置");
            complete(false);
            publish(1L);
            publish(2L);
            check(bothWorkers.await(10, TimeUnit.SECONDS), "没有两个真实 worker 并发执行");
            publish(3L);
            publish(4L);
            check(emptyQueried.await(10, TimeUnit.SECONDS), "空配置事件未查询");
            releaseWorkers.countDown();
            check(finished.await(10, TimeUnit.SECONDS), "异常后队列中的事件未完成");
            check(executed.equals(new HashSet<>(Arrays.asList(1L, 2L, 3L))), "空配置或回滚事件执行了插件");
            check(!queried.contains(999L), "回滚事件进入配置查询");
            check(maximum.get() == 2 && threads.size() >= 2, "worker 配置未实际生效");
            System.out.println("RdmEventDispatchTest passed: real queue, commit/rollback, empty config, concurrency and failure isolation");
        } finally {
            releaseWorkers.countDown();
            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                TransactionSynchronizationManager.clearSynchronization();
            }
            TenantContext.get().release();
            Field pool = CachedThreadPool.class.getDeclaredField("mainThreadPool");
            pool.setAccessible(true);
            ((ExecutorService) pool.get(null)).shutdownNow();
        }
    }

    /** 使用提交通知发布事件。 */
    private static void publish(long id) {
        TransactionSynchronizationManager.initSynchronization();
        IssueVo original = issue(id);
        original.setName("触发时值");
        RdmEventManager.doEvent(9L, id, EVENT, original);
        original.setName("提交前已修改");
        complete(true);
    }

    /** 模拟 Spring 通知，实际异步链由生产框架执行。 */
    private static void complete(boolean committed) {
        List<TransactionSynchronization> callbacks = TransactionSynchronizationManager.getSynchronizations();
        if (committed) {
            for (TransactionSynchronization callback : callbacks) { callback.afterCommit(); }
        }
        for (TransactionSynchronization callback : callbacks) {
            if (committed) { callback.afterCompletion(TransactionSynchronization.STATUS_COMMITTED); }
            else { callback.afterCompletion(TransactionSynchronization.STATUS_ROLLED_BACK); }
        }
        TransactionSynchronizationManager.clearSynchronization();
    }
}
