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

import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.dto.RdmEventDefinitionVo;
import neatlogic.framework.rdm.exception.event.RdmEventObjectAdapterClassMismatchException;
import neatlogic.framework.rdm.exception.event.RdmEventObjectAdapterDuplicateException;
import neatlogic.framework.rdm.exception.event.RdmEventObjectAdapterNotFoundException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Collections;

import static neatlogic.framework.rdm.event.RdmEventTestSupport.check;
import static neatlogic.framework.rdm.event.RdmEventTestSupport.initMessages;

/** 通过真实模块上下文刷新通知验证适配器工厂，不直接调用监听器内部方法。 */
public class RdmEventObjectAdapterFactoryTest {
    /** 覆盖初始化顺序、模块发现、异构泛型取值、重复标识及类型失配。 */
    public static void main(String[] args) {
        initMessages();
        RdmEventDefinition<String> definition = new RdmEventDefinition<>("FACTORY_TEST", "工厂测试", "测试",
                Collections.singleton("story"), "factory_text", String.class, false);
        check(definition.getObjectClass() == String.class && "factory_text".equals(definition.getObjectType()), "元数据依赖适配器初始化");
        check("factory_text".equals(new RdmEventDefinitionVo(definition).getObjectType()), "列表元数据触发提前查找");
        expect(RdmEventObjectAdapterNotFoundException.class, definition::getAdapter);
        Adapter<String> text = new Adapter<>("factory_text", String.class);
        Adapter<IssueVo> issue = new Adapter<>("factory_issue", IssueVo.class);
        NeatLogicWebApplicationContext context = new NeatLogicWebApplicationContext() {
            /** 以模块 Bean 的方式提供两个不同管理对象适配器。 */
            @Override
            protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
                beanFactory.registerSingleton("textAdapter", text);
                beanFactory.registerSingleton("issueAdapter", issue);
            }
        };
        context.addApplicationListener(new RdmEventObjectAdapterFactory());
        try {
            context.refresh();
            IRdmEventObjectAdapter<String> typedText = definition.getAdapter();
            IRdmEventObjectAdapter<IssueVo> typedIssue = RdmEventObjectAdapterFactory.getAdapter("factory_issue", IssueVo.class);
            check(typedText == text && typedIssue == issue, "模块刷新未注册所有适配器");
            expect(RdmEventObjectAdapterDuplicateException.class, () -> RdmEventObjectAdapterFactory.register(text));
            expect(RdmEventObjectAdapterClassMismatchException.class,
                    () -> RdmEventObjectAdapterFactory.getAdapter("factory_text", IssueVo.class));
            expect(RdmEventObjectAdapterNotFoundException.class,
                    () -> RdmEventObjectAdapterFactory.getAdapter("unknown", String.class));
        } finally { context.close(); }
        System.out.println("RdmEventObjectAdapterFactoryTest passed: module discovery, metadata order, typed lookup, duplicates and mismatch");
    }

    /** 断言固定语义的失败类型。 */
    private static void expect(Class<? extends Throwable> type, Runnable action) {
        try { action.run(); }
        catch (Throwable error) {
            if (type.isInstance(error)) { return; }
            throw new AssertionError("异常类型不匹配", error);
        }
        throw new AssertionError("预期失败未发生");
    }

    /** 提供两种无需转换的测试适配器声明。 */
    private static class Adapter<T> implements IRdmEventObjectAdapter<T> {
        private final String type;
        private final Class<T> objectClass;
        /** 保存稳定类型及精确类声明。 */
        Adapter(String type, Class<T> objectClass) { this.type = type; this.objectClass = objectClass; }
        /** 返回对象类型。 */
        public Class<T> getObjectClass() { return objectClass; }
        /** 返回稳定类型标识。 */
        public String getObjectType() { return type; }
        /** 本用例不依赖真实业务标识。 */
        public String getObjectId(T object) { return "test"; }
        /** 本用例只验证工厂注册，不执行快照。 */
        public T snapshot(T object) { return object; }
        /** 本用例无需业务读取。 */
        public T reload(Long projectId, Long appId, String objectId) { return null; }
        /** 本用例无需额外对象归属校验。 */
        public void validateScope(Long projectId, Long appId, T object) { }
    }
}
