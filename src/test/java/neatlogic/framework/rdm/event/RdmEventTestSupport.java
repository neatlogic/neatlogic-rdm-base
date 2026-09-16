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

import neatlogic.framework.rdm.dao.mapper.RdmEventMapper;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.dto.RdmEventHandlerVo;
import neatlogic.framework.util.SpringContextUtil;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.context.support.StaticMessageSource;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/** 独立进程测试共享的最小环境，不连接租户数据库。 */
final class RdmEventTestSupport {
    private RdmEventTestSupport() {}

    /** 初始化领域异常需要的消息资源。 */
    static void initMessages() { initMessages(true); }

    /** 能力边界测试可不安装提供方，其余回归使用明确测试能力。 */
    static void initMessages(boolean enableCapability) {
        if (enableCapability) { RdmEventRuntimeCapabilityFactory.register(() -> true); }
        StaticApplicationContext context = new StaticApplicationContext();
        context.getBeanFactory().registerSingleton("messageSourceAccessor", new MessageSourceAccessor(new StaticMessageSource()));
        new SpringContextUtil().setApplicationContext(context);
        try {
            Field mapper = RdmEventManager.class.getDeclaredField("mapper");
            mapper.setAccessible(true);
            mapper.set(null, Proxy.newProxyInstance(RdmEventMapper.class.getClassLoader(),
                    new Class<?>[]{RdmEventMapper.class}, (proxy, method, args) -> {
                        if (method.getName().equals("getEventAppById")) { return app((Long) args[0]); }
                        return null;
                    }));
        } catch (ReflectiveOperationException e) { throw new AssertionError(e); }
    }

    /** 构造事件校验所需的真实应用查询替身。 */
    static AppVo app(Long appId) {
        AppVo app = new AppVo(); app.setId(appId); app.setProjectId(9L); app.setType("story"); return app;
    }

    /** 通过生产注册入口注册插件。 */
    static void register(IRdmEventHandler<?> handler) {
        registerHandler(handler);
    }

    static final IRdmEventObjectAdapter<IssueVo> ADAPTER = new IRdmEventObjectAdapter<IssueVo>() {
        /** 获取测试对象类型。 */
        public Class<IssueVo> getObjectClass() { return IssueVo.class; }
        /** 获取稳定类型标识。 */
        public String getObjectType() { return "issue"; }
        /** 读取已有标识。 */
        public String getObjectId(IssueVo vo) { if (!vo.hasId()) { return null; } return vo.getId().toString(); }
        /** 测试对象仅携带基础字段，独立复制。 */
        public IssueVo snapshot(IssueVo vo) { IssueVo copy = issue(vo.getId()); copy.setName(vo.getName()); return copy; }
        /** 重载相同身份对象。 */
        public IssueVo reload(Long projectId, Long appId, String id) { return issue(Long.parseLong(id)); }
        /** 验证对象归属。 */
        public void validateScope(Long projectId, Long appId, IssueVo vo) {
            if (!Objects.equals(projectId, vo.getProjectId()) || !Objects.equals(appId, vo.getAppId())) {
                throw new IllegalArgumentException("对象归属不一致");
            }
        }
    };
    static final RdmEventDefinition<IssueVo> EVENT = new RdmEventDefinition<>("ISSUE_UPDATE", "更新", "测试快照事件", Collections.singleton("story"), ADAPTER.getObjectType(), IssueVo.class, true);
    static final RdmEventDefinition<IssueVo> DELETED = new RdmEventDefinition<>("ISSUE_DELETE", "删除", "测试删除", Collections.singleton("story"), ADAPTER.getObjectType(), IssueVo.class, true);

    /** 构造不依赖自动 ID 的需求。 */
    static IssueVo issue(long id) {
        IssueVo issue = new IssueVo();
        issue.setId(id);
        issue.setAppId(id);
        issue.setProjectId(9L);
        return issue;
    }

    /** 构造可直接触发的插件配置。 */
    static RdmEventHandlerVo config(long id, int sort, String name) {
        RdmEventHandlerVo config = new RdmEventHandlerVo();
        config.setId(id);
        config.setSort(sort);
        config.setHandler(name);
        config.setEvent("ISSUE_UPDATE");
        config.setProjectId(9L);
        config.setAppId(1L);
        return config;
    }

    /** 明确失败原因，不依赖 JVM 的断言开关。 */
    static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    /** 提供测试插件通用元信息，业务测试只覆盖触发行为。 */
    abstract static class Handler extends RdmEventHandlerBase<IssueVo> {
        /** 为真实插件基类配置隔离事务和审计替身。 */
        Handler() {
            try { rdmEventMapper = new RdmEventHandlerTest.Fixture().mapper; }
            catch (Exception e) { throw new AssertionError(e); }
        }
        public String getName() { return "test"; }
        public String getLabel() { return "测试"; }
        public String getIcon() { return ""; }
        public String getDescription() { return "测试插件"; }
        public Set<String> supportEventTypes() { return Collections.singleton("ISSUE_UPDATE"); }
        public Set<String> supportParentHandler() { return Collections.emptySet(); }
        /** 声明插件对象类型。 */
        public Class<IssueVo> getObjectClass() { return IssueVo.class; }

    }
    /** 测试通过模块初始化入口发现插件，不向生产工厂增加手动注册接口。 */
    public static void registerHandler(neatlogic.framework.rdm.event.IRdmEventHandler<?> handler) {
        neatlogic.framework.bootstrap.NeatLogicWebApplicationContext context = new neatlogic.framework.bootstrap.NeatLogicWebApplicationContext() {
            /** 只提供本次待发现的插件，不建立外部资源连接。 */
            @Override
            public <T> java.util.Map<String, T> getBeansOfType(Class<T> type) {
                return java.util.Collections.singletonMap("testHandler", type.cast(handler));
            }
        };
        new neatlogic.framework.rdm.event.RdmEventHandlerFactory() {
            /** 调用与生产相同的模块发现逻辑。 */
            void initialize() { onInitialized(context); }
        }.initialize();
    }
}
