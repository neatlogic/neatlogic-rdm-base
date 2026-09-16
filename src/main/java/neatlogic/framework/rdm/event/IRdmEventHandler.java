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
import neatlogic.framework.rdm.dto.RdmEventHandlerVo;

import java.util.Collections;
import java.util.Set;

/** 研发事件插件类型契约；插件通过 RdmEventHandlerBase 复用统一事务和审计，工厂按接口发现。 */
public interface IRdmEventHandler<T> {
    /** 获取全局唯一插件标识。 */
    String getName();
    /** 获取插件显示名称。 */
    String getLabel();
    /** 获取插件图标。 */
    String getIcon();
    /** 获取插件说明。 */
    String getDescription();
    /** 获取插件展示排序。 */
    default int getSort() { return 99; }
    /** 声明允许挂载的父插件。 */
    Set<String> supportParentHandler();
    /** 根据配置提取子插件，普通插件无需处理。 */
    default void makeupChildHandler(RdmEventHandlerVo handlerVo) { }
    /** 声明支持的事件标识。 */
    Set<String> supportEventTypes();
    /** 声明适用应用类型；空集合表示共享插件，仍须满足事件及对象类型约束。 */
    default Set<String> supportAppTypes() { return Collections.emptySet(); }
    /** 校验插件业务配置；有配置约束的插件应抛出固定语义业务异常。 */
    default void validateConfig(JSONObject config) { }
    /** 声明是否需要专属配置编辑组件，无配置插件无需实现。 */
    default boolean requiresConfigEditor() { return false; }
    /** 声明插件接受的精确对象类型。 */
    Class<T> getObjectClass();
    /** 执行根配置并传递同类型管理对象。 */
    T trigger(RdmEventHandlerVo handlerVo, RdmEventDefinition<T> event, T object);
    /** 关联父审计执行配置，业务父插件应使用基类的子执行入口。 */
    T trigger(RdmEventHandlerVo handlerVo, RdmEventDefinition<T> event, T object, Long parentAuditId);
}
