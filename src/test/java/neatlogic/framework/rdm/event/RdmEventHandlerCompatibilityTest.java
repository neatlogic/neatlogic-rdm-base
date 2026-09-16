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

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.rdm.dao.mapper.RdmEventMapper;
import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.rdm.exception.event.RdmEventHandlerAppNotSupportedException;
import neatlogic.framework.rdm.exception.event.RdmEventHandlerObjectClassMismatchException;
import neatlogic.framework.rdm.exception.event.RdmEventHandlerParentNotSupportedException;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static neatlogic.framework.rdm.event.RdmEventTestSupport.*;

/** 验证共享插件、专用插件与配置业务校验在列表及运行时采用同一约束。 */
public class RdmEventHandlerCompatibilityTest {
    private static final String EVENT_NAME = "CONFIG_COMPATIBILITY_TEST";
    private static final RdmEventDefinition<IssueVo> EVENT = new RdmEventDefinition<>(EVENT_NAME,
            "配置兼容测试", "测试应用适配", new HashSet<>(Arrays.asList("story", "task")), ADAPTER.getObjectType(), IssueVo.class, true);

    /** 在独立进程中运行，不连接租户数据库。 */
    public static void main(String[] args) throws Exception {
        initMessages();
        RdmEventObjectAdapterFactory.register(ADAPTER);
        Field mapper = RdmEventManager.class.getDeclaredField("mapper");
        mapper.setAccessible(true);
        mapper.set(null, Proxy.newProxyInstance(RdmEventMapper.class.getClassLoader(),
                new Class<?>[]{RdmEventMapper.class}, (proxy, method, params) -> {
                    if (!method.getName().equals("getEventAppById")) { return null; }
                    Long appId = (Long) params[0];
                    AppVo app = app(appId);
                    if (appId != 1L) { app.setType("task"); }
                    return app;
                }));
        RdmEventRegistry.register(EVENT);
        // 模拟模块 AOP 的 JDK 接口代理，注册后必须继续通过代理调用插件。
        Plugin target = new Plugin("proxied", Collections.emptySet());
        org.springframework.aop.framework.ProxyFactory proxyFactory = new org.springframework.aop.framework.ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false);
        int[] intercepted = {0};
        proxyFactory.addAdvice((org.aopalliance.intercept.MethodInterceptor) invocation -> {
            intercepted[0]++;
            return invocation.proceed();
        });
        IRdmEventHandler<?> proxied = (IRdmEventHandler<?>) proxyFactory.getProxy();
        check(org.springframework.aop.support.AopUtils.isJdkDynamicProxy(proxied), "测试必须使用 JDK 代理");
        register(proxied);
        check(RdmEventHandlerFactory.getHandler("proxied") == proxied, "工厂不得剥离插件代理");
        int before = intercepted[0];
        RdmEventHandlerFactory.getHandler("proxied").validateConfig(new JSONObject());
        check(intercepted[0] > before, "工厂调用绕过了代理切面");
        Plugin shared = new Plugin("shared", Collections.emptySet());
        Plugin storyOnly = new Plugin("story-only", Collections.singleton("story"));
        Plugin child = new Plugin("child", Collections.emptySet()) {
            /** 子插件仅允许挂载在共享插件下。 */
            public Set<String> supportParentHandler() { return Collections.singleton("shared"); }
        };
        register(shared); register(storyOnly); register(child);
        check(RdmEventHandlerFactory.getHandlerList("story", EVENT_NAME, null).contains(shared), "共享插件未适配需求应用");
        check(RdmEventHandlerFactory.getHandlerList("task", EVENT_NAME, null).contains(shared), "共享插件未适配相同对象类型的任务应用");
        check(!RdmEventHandlerFactory.getHandlerList("task", EVENT_NAME, null).contains(storyOnly), "专用插件泄漏到其他应用选项");
        check(RdmEventHandlerFactory.getHandlerList("story", EVENT_NAME, "shared").contains(child), "合法父插件被错误排除");
        check(!RdmEventHandlerFactory.getHandlerList("story", EVENT_NAME, "story-only").contains(child), "父插件限制未参与选项筛选");
        RdmEventHandlerVo sharedConfig = config(101L, 0, "shared"); sharedConfig.setEvent(EVENT_NAME);
        RdmEventManager.validateConfiguration(sharedConfig, EVENT, 9L, 1L);
        sharedConfig.setAppId(2L);
        RdmEventManager.validateConfiguration(sharedConfig, EVENT, 9L, 2L);
        RdmEventHandlerVo dedicatedConfig = config(102L, 0, "story-only");
        dedicatedConfig.setEvent(EVENT_NAME); dedicatedConfig.setAppId(2L);
        IssueVo task = issue(1L); task.setAppId(2L);
        expect(RdmEventHandlerAppNotSupportedException.class,
                () -> RdmEventManager.executeHandlers(9L, 2L, Collections.singletonList(dedicatedConfig), EVENT, task));
        check(storyOnly.executions == 0, "不适配插件进入了业务执行");
        RdmEventHandlerVo childConfig = config(103L, 0, "child"); childConfig.setEvent(EVENT_NAME);
        expect(RdmEventHandlerParentNotSupportedException.class,
                () -> RdmEventHandlerFactory.validate(childConfig, EVENT, "story", "story-only"));
        RdmEventHandlerVo wrongEvent = config(104L, 0, "shared"); wrongEvent.setEvent(EVENT_NAME);
        IRdmEventObjectAdapter<String> stringAdapter = new IRdmEventObjectAdapter<String>() {
            /** 返回另一个明确的对象类型。 */
            public Class<String> getObjectClass() { return String.class; }
            /** 返回测试类型标识。 */
            public String getObjectType() { return "string"; }
            /** 直接使用字符串作为测试标识。 */
            public String getObjectId(String value) { return value; }
            /** 创建独立测试快照。 */
            public String snapshot(String value) { return new String(value); }
            /** 返回测试重载结果。 */
            public String reload(Long project, Long app, String id) { return id; }
            /** 测试适配器无额外业务范围。 */
            public void validateScope(Long project, Long app, String value) { }
        };
        RdmEventDefinition<String> stringEvent = new RdmEventDefinition<>(EVENT_NAME, "字符串", "类型测试",
                Collections.singleton("story"), stringAdapter.getObjectType(), String.class, true);
        expect(RdmEventHandlerObjectClassMismatchException.class,
                () -> RdmEventHandlerFactory.validate(wrongEvent, stringEvent, "story", null));
        JSONObject invalid = new JSONObject(); invalid.put("invalid", true);
        sharedConfig.setConfig(invalid);
        expect(IllegalStateException.class,
                () -> RdmEventManager.executeHandlers(9L, 2L, Collections.singletonList(sharedConfig), EVENT, task));
        check(shared.executions == 0, "非法插件配置进入了业务执行");
        check(!new RdmEventHandlerVo().hasId(), "检查新增配置生成了标识");
        check(!new RdmEventHandlerMetaVo(shared).getRequiresConfigEditor(), "默认插件意外需要编辑器");
        System.out.println("RdmEventHandlerCompatibilityTest passed: shared apps, dedicated apps, parents, classes, runtime config validation");
    }

    /** 断言约束在业务执行前以预期类型失败。 */
    private static void expect(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable e) {
            if (type.isInstance(e)) { return; }
            throw new AssertionError("异常类型不匹配", e);
        }
        throw new AssertionError("预期校验失败");
    }

    /** 最小真实插件基类实现，用执行计数检查失败边界。 */
    private static class Plugin extends RdmEventHandlerBase<IssueVo> {
        private final String name;
        private final Set<String> appTypes;
        private int executions;
        /** 保存插件名称与应用专属约束。 */
        private Plugin(String name, Set<String> appTypes) { this.name = name; this.appTypes = appTypes; }
        /** 获取测试名称。 */
        public String getName() { return name; }
        /** 获取显示名称。 */
        public String getLabel() { return name; }
        /** 测试无需图标。 */
        public String getIcon() { return ""; }
        /** 获取说明。 */
        public String getDescription() { return "兼容校验测试"; }
        /** 默认仅作为根插件使用。 */
        public Set<String> supportParentHandler() { return Collections.emptySet(); }
        /** 声明测试事件。 */
        public Set<String> supportEventTypes() { return Collections.singleton(EVENT_NAME); }
        /** 返回专属应用范围或共享空集合。 */
        public Set<String> supportAppTypes() { return appTypes; }
        /** 接收明确需求对象类型。 */
        public Class<IssueVo> getObjectClass() { return IssueVo.class; }
        /** 模拟业务配置校验失败。 */
        public void validateConfig(JSONObject config) {
            if (config != null && config.getBooleanValue("invalid")) { throw new IllegalStateException("测试配置无效"); }
        }
        /** 只统计到达实际业务执行的次数。 */
        protected IssueVo myTrigger(RdmEventHandlerVo handler, RdmEventPluginVo plugin, IssueVo object,
                                     RdmEventAuditVo audit, RdmEventStatusVo status) {
            executions++;
            return object;
        }
    }
}
