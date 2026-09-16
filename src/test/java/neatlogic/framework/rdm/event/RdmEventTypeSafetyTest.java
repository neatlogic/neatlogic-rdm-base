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

import neatlogic.framework.rdm.dto.RdmEventAuditVo;
import neatlogic.framework.rdm.dto.RdmEventHandlerVo;
import neatlogic.framework.rdm.dto.RdmEventPluginVo;
import neatlogic.framework.rdm.dto.RdmEventStatusVo;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;

import static neatlogic.framework.rdm.event.RdmEventTestSupport.*;

/** 用独立业务对象验证泛型插件、对象快照以及配置树隔离，不访问数据库。 */
public class RdmEventTypeSafetyTest {
    /** 模拟外部应用的字符串身份与可变业务属性，无需继承框架对象。 */
    static class TestVo {
        String id = "MR/AbC-123";
        String title = "触发快照";
        final List<String> labels = new ArrayList<>();
    }

    /** 快照复制可变集合，最新状态读取保持可观测。 */
    static class Adapter implements IRdmEventObjectAdapter<TestVo> {
        int reloads;
        TestVo latest;
        /** 声明精确的管理对象类型。 */
        public Class<TestVo> getObjectClass() { return TestVo.class; }
        /** 提供稳定对象类型标识。 */
        public String getObjectType() { return "test_merge_request"; }
        /** 原样保留外部系统标识。 */
        public String getObjectId(TestVo vo) { return vo.id; }
        /** 深复制本测试对象携带的全部可变字段。 */
        public TestVo snapshot(TestVo vo) {
            TestVo copy = new TestVo(); copy.id = vo.id; copy.title = vo.title;
            copy.labels.addAll(vo.labels); return copy;
        }
        /** 确认重载发生在插件事务内。 */
        public TestVo reload(Long projectId, Long appId, String objectId) {
            check(TransactionSynchronizationManager.isActualTransactionActive(), "重载不在事务内");
            reloads++; return latest;
        }
        /** 外部应用对象在本用例中只归属于一个项目应用。 */
        public void validateScope(Long projectId, Long appId, TestVo vo) {
            if (!Objects.equals(projectId, 9L) || !Objects.equals(appId, 1L)) {
                throw new IllegalArgumentException("测试对象归属不匹配");
            }
        }
    }

    /** 插件直接读取 TestVo 字段，业务实现不使用强制类型转换。 */
    static class Plugin extends RdmEventHandlerBase<TestVo> {
        final String name;
        int calls;
        RdmEventHandlerVo childToRun;
        /** 复用可观测事务与审计替身。 */
        Plugin(String name, RdmEventHandlerTest.Fixture fixture) { this.name = name; rdmEventMapper = fixture.mapper; }
        /** 返回插件标识。 */
        public String getName() { return name; }
        /** 返回插件展示名称。 */
        public String getLabel() { return "测试强类型插件"; }
        /** 本用例无需图标。 */
        public String getIcon() { return ""; }
        /** 返回插件说明。 */
        public String getDescription() { return "操作独立业务对象"; }
        /** 明确声明类型，无业务转换。 */
        public Class<TestVo> getObjectClass() { return TestVo.class; }
        /** 同类型事件可以共享插件。 */
        public Set<String> supportEventTypes() { return new HashSet<>(Arrays.asList("TYPE_UPDATE", "TYPE_DELETE")); }
        /** 允许受测试父插件驱动。 */
        public Set<String> supportParentHandler() { return Collections.singleton("typed-parent"); }
        /** 修改本类型的业务字段并返回同一身份对象。 */
        protected TestVo myTrigger(RdmEventHandlerVo config, RdmEventPluginVo plugin, TestVo vo,
                                  RdmEventAuditVo audit, RdmEventStatusVo status) {
            calls++; vo.title += ":" + name;
            if (childToRun != null) { return triggerChild(config, childToRun, vo, audit); }
            return vo;
        }
    }

    /** 创建项目应用内的强类型配置。 */
    private static RdmEventHandlerVo typedConfig(long id, String plugin, String event) {
        RdmEventHandlerVo config = config(id, 0, plugin); config.setEvent(event); return config;
    }

    /** 确认非法输入在业务动作之前明确失败。 */
    private static void rejected(Runnable action) {
        try { action.run(); }
        catch (IllegalArgumentException | neatlogic.framework.exception.core.ApiRuntimeException expected) { return; }
        throw new AssertionError("非法类型或配置未拒绝");
    }

    /** 验证通过原始类型绕过编译约束的输入仍会被运行时边界拒绝。 */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void invalidRuntimeObject(RdmEventDefinition<TestVo> event) {
        RdmEventDefinition raw = event;
        rejected(() -> RdmEventManager.validateObject(9L, 1L, raw, "不是业务对象", null));
    }

    /** 即便事件标识相同，不同对象类型也不能复用不兼容插件。 */
    private static void incompatiblePlugin() {
        IRdmEventObjectAdapter<String> adapter = new IRdmEventObjectAdapter<String>() {
            /** 声明另一类对象。 */
            public Class<String> getObjectClass() { return String.class; }
            /** 提供独立类型标识。 */
            public String getObjectType() { return "test_text"; }
            /** 字符串本身作为标识。 */
            public String getObjectId(String value) { return value; }
            /** 复制测试值。 */
            public String snapshot(String value) { return new String(value); }
            /** 重载测试值。 */
            public String reload(Long projectId, Long appId, String id) { return id; }
            /** 此用例只验证插件类型，不执行对象归属逻辑。 */
            public void validateScope(Long projectId, Long appId, String value) { }
        };
        RdmEventDefinition<String> incompatible = new RdmEventDefinition<>("TYPE_UPDATE", "更新", "测试", Collections.singleton("story"), adapter.getObjectType(), adapter.getObjectClass(), false);
        rejected(() -> RdmEventHandlerFactory.validate(typedConfig(9, "typed-parent", "TYPE_UPDATE"), incompatible));
    }

    /** 执行异构对象、深快照、注册和配置树回归。 */
    public static void main(String[] args) throws Exception {
        initMessages();
        RdmEventHandlerTest.Fixture fixture = new RdmEventHandlerTest.Fixture();
        Adapter adapter = new Adapter();
        RdmEventObjectAdapterFactory.register(adapter);
        RdmEventDefinition<TestVo> update = new RdmEventDefinition<>("TYPE_UPDATE", "更新", "测试", Collections.singleton("story"), adapter.getObjectType(), adapter.getObjectClass(), false);
        RdmEventDefinition<TestVo> deleted = new RdmEventDefinition<>("TYPE_DELETE", "删除", "测试", Collections.singleton("story"), adapter.getObjectType(), adapter.getObjectClass(), true);
        RdmEventRegistry.register(update); RdmEventRegistry.register(deleted);
        rejected(() -> RdmEventRegistry.register(update));
        Plugin parent = new Plugin("typed-parent", fixture);
        Plugin child = new Plugin("typed-child", fixture);
        register(parent); register(child);
        invalidRuntimeObject(update);
        incompatiblePlugin();

        TestVo original = new TestVo(); original.labels.add("发布前");
        TestVo snapshot = adapter.snapshot(original);
        original.title = "发布后"; original.labels.add("发布后");
        check(snapshot.labels.size() == 1 && "触发快照".equals(snapshot.title), "快照共享可变引用");
        RdmEventHandlerVo first = typedConfig(1, parent.getName(), deleted.getName());
        RdmEventHandlerVo second = typedConfig(2, child.getName(), deleted.getName());
        TestVo result = RdmEventManager.executeHandlers(Arrays.asList(second, first), deleted, snapshot);
        check(result.title.equals("触发快照:typed-parent:typed-child"), "删除链未串行传递同类型快照");
        check(adapter.reloads == 0, "删除事件错误重载已删除对象");
        check(fixture.last().getObjectId().equals("MR/AbC-123"), "外部字符串身份丢失");

        adapter.latest = new TestVo(); adapter.latest.title = "数据库最新值";
        RdmEventHandlerVo root = typedConfig(3, parent.getName(), update.getName());
        TestVo latest = RdmEventManager.executeHandlers(Collections.singletonList(root), update, new TestVo());
        check(latest == adapter.latest && adapter.reloads == 1, "非需求对象未在事务内重载");
        int calls = parent.calls + child.calls;
        RdmEventHandlerVo nested = typedConfig(4, child.getName(), update.getName());
        nested.setParentId(root.getId()); root.setHandlerList(Collections.singletonList(nested));
        RdmEventManager.validateConfiguration(root, update, 9L, 1L);
        nested.setAppId(2L);
        rejected(() -> RdmEventManager.executeHandlers(Collections.singletonList(root), update, new TestVo()));
        nested.setAppId(1L);
        nested.setEvent(deleted.getName());
        rejected(() -> RdmEventManager.validateConfiguration(root, update, 9L, 1L));
        nested.setEvent(update.getName());
        nested.setHandlerList(Collections.singletonList(root));
        rejected(() -> RdmEventManager.validateConfiguration(root, update, 9L, 1L));
        check(calls == parent.calls + child.calls, "非法子配置执行了业务动作");
        root.setHandlerList(null); nested.setHandlerList(null);
        parent.childToRun = nested;
        int auditCount = fixture.audits.size();
        RdmEventManager.executeHandlers(Collections.singletonList(root), update, new TestVo());
        check(fixture.audits.size() == auditCount + 2, "真实子插件未创建父子审计");
        check(Objects.equals(fixture.audits.get(auditCount + 1).getParentId(), fixture.audits.get(auditCount).getId()), "父审计关联丢失");
        nested.setAppId(2L);
        int childCalls = child.calls;
        rejected(() -> RdmEventManager.executeHandlers(Collections.singletonList(root), update, new TestVo()));
        check(child.calls == childCalls, "动态跨应用子插件执行了业务");
        nested.setAppId(1L);
        RdmEventHandlerVo recursive = typedConfig(root.getId(), parent.getName(), update.getName());
        recursive.setParentId(root.getId()); parent.childToRun = recursive;
        int parentCalls = parent.calls;
        rejected(() -> RdmEventManager.executeHandlers(Collections.singletonList(root), update, new TestVo()));
        check(parent.calls == parentCalls + 1, "动态循环重复进入业务动作");
        parent.childToRun = null;
        System.out.println("RdmEventTypeSafetyTest passed: 独立对象、注册、类型、深快照、删除链、跨应用子配置及循环拒绝");
    }
}
