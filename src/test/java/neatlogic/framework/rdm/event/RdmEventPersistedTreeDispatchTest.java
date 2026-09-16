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
import neatlogic.framework.rdm.exception.event.RdmEventConfigurationScopeMismatchException;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static neatlogic.framework.rdm.event.RdmEventTestSupport.*;

/** 验证数据库扁平配置经过真实发布后能作为完整子树交给父插件。 */
public class RdmEventPersistedTreeDispatchTest {
    /** 覆盖父插件主动触发子树、禁用子节点与错误归属拒绝。 */
    public static void main(String[] args) throws Exception {
        initMessages();
        RdmEventObjectAdapterFactory.register(ADAPTER);
        ModuleInitApplicationListener.getModuleinitphaser().forceTermination();
        Properties properties = new Properties(); properties.setProperty("rdm.events.thread.count", "1");
        new RdmConfig().loadConfig(properties);
        RdmEventHandlerTest.Fixture fixture = new RdmEventHandlerTest.Fixture();
        RdmEventHandlerVo root = config(201L, 0, "persisted-parent");
        RdmEventHandlerVo child = config(202L, 0, "persisted-child"); child.setParentId(root.getId());
        RdmEventHandlerVo disabled = config(203L, 1, "persisted-child");
        disabled.setParentId(root.getId()); disabled.setIsActive(0);
        List<RdmEventHandlerVo> flat = Arrays.asList(root, child, disabled);
        AtomicInteger childCalls = new AtomicInteger();
        AtomicInteger parentCalls = new AtomicInteger();
        CountDownLatch completed = new CountDownLatch(1);
        Plugin parent = new Plugin("persisted-parent", fixture.mapper) {
            /** 从框架装配的子树选择执行，禁止插件重复读取数据库。 */
            protected IssueVo myTrigger(RdmEventHandlerVo config, RdmEventPluginVo plugin, IssueVo object,
                                         RdmEventAuditVo audit, RdmEventStatusVo status) {
                parentCalls.incrementAndGet();
                try {
                    check(config.getHandlerList() != null && config.getHandlerList().size() == 2, "正式发布丢失了已保存子树");
                    for (RdmEventHandlerVo node : config.getHandlerList()) {
                        object = triggerChild(config, node, object, audit);
                    }
                    return object;
                } finally { completed.countDown(); }
            }
        };
        Plugin childPlugin = new Plugin("persisted-child", fixture.mapper) {
            /** 只统计真正进入业务动作的子配置。 */
            protected IssueVo myTrigger(RdmEventHandlerVo config, RdmEventPluginVo plugin, IssueVo object,
                                         RdmEventAuditVo audit, RdmEventStatusVo status) {
                childCalls.incrementAndGet(); return object;
            }
        };
        register(parent); register(childPlugin); RdmEventRegistry.register(EVENT);
        RdmEventMapper mapper = (RdmEventMapper) Proxy.newProxyInstance(RdmEventMapper.class.getClassLoader(),
                new Class<?>[]{RdmEventMapper.class}, (proxy, method, parameters) -> {
                    if (method.getName().equals("getEventAppById")) { return app((Long) parameters[0]); }
                    if (method.getName().equals("getHandlerByEvent")) { return Collections.singletonList(root); }
                    if (method.getName().equals("getHandlerByParentId")) {
                        List<RdmEventHandlerVo> children = new ArrayList<>();
                        for (RdmEventHandlerVo node : flat) {
                            if (Objects.equals(node.getParentId(), parameters[0])) { children.add(node); }
                        }
                        return children;
                    }
                    throw new AssertionError("非预期持久化调用：" + method.getName());
                });
        try {
            TenantContext.init("rdm-persisted-tree-test");
            new RdmEventManager(mapper);
            TransactionSynchronizationManager.initSynchronization();
            RdmEventManager.doEvent(9L, 1L, EVENT, issue(1L));
            for (TransactionSynchronization callback : TransactionSynchronizationManager.getSynchronizations()) { callback.afterCommit(); }
            for (TransactionSynchronization callback : TransactionSynchronizationManager.getSynchronizations()) {
                callback.afterCompletion(TransactionSynchronization.STATUS_COMMITTED);
            }
            TransactionSynchronizationManager.clearSynchronization();
            check(completed.await(10, TimeUnit.SECONDS), "真实事件队列未执行父插件");
            check(parentCalls.get() == 1 && childCalls.get() == 1, "禁用子节点进入业务执行或合法子节点丢失");
            child.setAppId(2L);
            rejected(() -> RdmEventManager.loadHandlers(9L, 1L, EVENT));
            child.setAppId(1L); child.setEvent("OTHER_EVENT");
            rejected(() -> RdmEventManager.loadHandlers(9L, 1L, EVENT));
            check(parentCalls.get() == 1 && childCalls.get() == 1, "错误范围子树进入执行阶段");
            System.out.println("RdmEventPersistedTreeDispatchTest passed: persisted tree, real queue, parent dispatch, disabled child, invalid scope/event");
        } finally {
            if (TransactionSynchronizationManager.isSynchronizationActive()) { TransactionSynchronizationManager.clearSynchronization(); }
            TenantContext.get().release();
            Field pool = CachedThreadPool.class.getDeclaredField("mainThreadPool"); pool.setAccessible(true);
            ((ExecutorService) pool.get(null)).shutdownNow();
        }
    }

    /** 错误归属的子节点必须被发现，不能因查询过滤而静默忽略。 */
    private static void rejected(Runnable action) {
        try { action.run(); }
        catch (RdmEventConfigurationScopeMismatchException expected) { return; }
        throw new AssertionError("错误范围的已保存子配置未拒绝");
    }

    /** 为真实执行基类提供最小插件声明。 */
    private abstract static class Plugin extends RdmEventHandlerBase<IssueVo> {
        private final String name;
        /** 共享可观测审计替身。 */
        Plugin(String name, RdmEventMapper mapper) { this.name = name; this.rdmEventMapper = mapper; }
        /** 获取插件标识。 */
        public String getName() { return name; }
        /** 获取显示名称。 */
        public String getLabel() { return name; }
        /** 测试无需图标。 */
        public String getIcon() { return ""; }
        /** 获取测试说明。 */
        public String getDescription() { return "持久化子树执行测试"; }
        /** 子插件允许指定父插件驱动。 */
        public Set<String> supportParentHandler() { return Collections.singleton("persisted-parent"); }
        /** 声明实际发布的事件。 */
        public Set<String> supportEventTypes() { return Collections.singleton(EVENT.getName()); }
        /** 直接使用明确的需求对象类型。 */
        public Class<IssueVo> getObjectClass() { return IssueVo.class; }
    }
}
