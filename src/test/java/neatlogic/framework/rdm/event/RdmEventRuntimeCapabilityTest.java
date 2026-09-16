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
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.rdm.dao.mapper.RdmEventMapper;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.exception.event.RdmEventCapabilityProviderDuplicateException;
import neatlogic.framework.rdm.exception.event.RdmEventCapabilityProviderEmptyException;
import neatlogic.framework.rdm.exception.event.RdmEventCapabilityUnavailableException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static neatlogic.framework.rdm.event.RdmEventTestSupport.*;

/** 验证缺少商业模块时无副作用，以及提交、排队和逐步执行的能力检查。 */
public class RdmEventRuntimeCapabilityTest {
    /** 真实工厂发现和事务通知，不连接数据库或发送邮件。 */
    public static void main(String[] args) throws Exception {
        initMessages(false);
        check(!RdmEventRuntimeCapabilityFactory.isAvailable(), "未安装能力必须关闭");
        RdmEventManager.doEvent(null, null, null, null);
        expect(RdmEventCapabilityProviderEmptyException.class, () -> RdmEventRuntimeCapabilityFactory.register(null));
        AtomicBoolean enabled = new AtomicBoolean(true);
        IRdmEventRuntimeCapability provider = enabled::get;
        NeatLogicWebApplicationContext module = new NeatLogicWebApplicationContext() {
            /** 模拟商业模块容器提供能力 Bean。 */
            @Override
            protected void loadBeanDefinitions(DefaultListableBeanFactory beans) {
                beans.registerSingleton("capability", provider);
            }
        };
        module.addApplicationListener(new RdmEventRuntimeCapabilityFactory());
        RdmEventObjectAdapterFactory.register(ADAPTER);
        RdmEventRegistry.register(EVENT);
        RdmEventHandlerTest.Fixture fixture = new RdmEventHandlerTest.Fixture();
        AtomicInteger reads = new AtomicInteger();
        RdmEventMapper mapper = (RdmEventMapper) Proxy.newProxyInstance(RdmEventMapper.class.getClassLoader(), new Class<?>[]{RdmEventMapper.class}, (proxy, method, values) -> {
            if (method.getName().equals("getEventAppById")) { reads.incrementAndGet(); return app((Long) values[0]); }
            if (method.getName().equals("getHandlerByEvent")) { throw new AssertionError("关闭后仍查询配置"); }
            try { return method.invoke(fixture.mapper, values); }
            catch (InvocationTargetException e) { throw e.getCause(); }
        });
        ModuleInitApplicationListener.getModuleinitphaser().forceTermination();
        new RdmEventManager(mapper);
        try {
            module.refresh();
            check(RdmEventRuntimeCapabilityFactory.isAvailable(), "模块刷新未发现能力");
            expect(RdmEventCapabilityProviderDuplicateException.class, () -> RdmEventRuntimeCapabilityFactory.register(provider));
            enabled.set(false);
            RdmEventManager.doEvent(null, null, null, null);
            check(reads.get() == 0 && fixture.audits.isEmpty(), "关闭时访问对象或写入审计");
            expect(RdmEventCapabilityUnavailableException.class, RdmEventRuntimeCapabilityFactory::requireAvailable);
            AtomicInteger calls = new AtomicInteger();
            RdmEventHandlerBase<IssueVo> handler = fixture.handler((issue, audit, status) -> {
                calls.incrementAndGet(); enabled.set(false); return issue;
            });
            expect(RdmEventCapabilityUnavailableException.class, () -> handler.trigger(config(1, 0, "test"), EVENT, issue(1)));
            check(fixture.audits.isEmpty(), "关闭时直接调用插件仍执行");
            enabled.set(true);
            TenantContext.init("capability-test");
            TransactionSynchronizationManager.initSynchronization();
            RdmEventManager.doEvent(9L, 1L, EVENT, issue(1));
            enabled.set(false);
            for (TransactionSynchronization callback : TransactionSynchronizationManager.getSynchronizations()) {
                callback.afterCommit(); callback.afterCompletion(TransactionSynchronization.STATUS_COMMITTED);
            }
            TransactionSynchronizationManager.clearSynchronization();
            check(fixture.audits.isEmpty(), "提交前失去能力仍发布事件");
            // 模拟任务已入队、尚未进入执行链时失去能力。
            RdmEventManager.executeHandlers(9L, 1L, Collections.singletonList(config(1, 0, "test")), EVENT, issue(1));
            check(calls.get() == 0 && fixture.audits.size() == 1 && "failed".equals(fixture.last().getStatus()), "排队任务未停止并记录原因");
            fixture.audits.clear();
            enabled.set(true);
            RdmEventManager.executeHandlers(9L, 1L, Arrays.asList(config(1, 0, "test"), config(2, 1, "test")), EVENT, issue(1));
            check(calls.get() == 1 && fixture.audits.size() == 2, "能力关闭后继续执行后序节点");
            check("succeed".equals(fixture.audits.get(0).getStatus()) && "failed".equals(fixture.last().getStatus()), "前序已提交结果或停止审计错误");
        } finally {
            module.close();
            check(!RdmEventRuntimeCapabilityFactory.isAvailable(), "模块关闭未清理能力");
            if (TransactionSynchronizationManager.isSynchronizationActive()) { TransactionSynchronizationManager.clearSynchronization(); }
            TenantContext.get().release();
            Field pool = CachedThreadPool.class.getDeclaredField("mainThreadPool"); pool.setAccessible(true);
            ((ExecutorService) pool.get(null)).shutdownNow();
        }
        System.out.println("RdmEventRuntimeCapabilityTest passed: absent module, discovery/close, unavailable publish, afterCommit, queued and mid-chain stop");
    }

    /** 断言独立异常类型，避免静默跳过配置或分发错误。 */
    private static void expect(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable error) {
            if (type.isInstance(error)) { return; }
            throw new AssertionError("异常类型不匹配", error);
        }
        throw new AssertionError("预期失败未发生");
    }
}
