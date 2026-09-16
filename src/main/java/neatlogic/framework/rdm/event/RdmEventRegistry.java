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

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/** 注册应用提供的类型定义，数据库标识不能指定任意 Java 类型。 */
@RootComponent
public class RdmEventRegistry extends ModuleInitializedListenerBase {
    private static final Map<String, RdmEventDefinition<?>> EVENTS = new ConcurrentHashMap<>();
    /** 注册唯一事件，重复标识立即失败。 */
    public static void register(RdmEventDefinition<?> event) {
        Objects.requireNonNull(event, "事件定义不能为空");
        if (EVENTS.putIfAbsent(event.getName(), event) != null) {
            throw new IllegalArgumentException("重复事件标识：" + event.getName());
        }
    }
    /** 按持久化标识查询定义。 */
    public static RdmEventDefinition<?> get(String name) {
        if (name == null) { return null; }
        return EVENTS.get(name);
    }
    /** 获取事件显示名称，未知标识保留原值。 */
    public static String getLabel(String name) {
        RdmEventDefinition<?> event = get(name);
        if (event != null) { return event.getLabel(); }
        return name;
    }
    /** 获取注册定义的只读快照。 */
    public static List<RdmEventDefinition<?>> getEvents() {
        return Collections.unmodifiableList(new ArrayList<>(EVENTS.values()));
    }
    /** 模块初始化时发现业务事件提供者。 */
    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        for (IRdmEventDefinitionProvider provider : context.getBeansOfType(IRdmEventDefinitionProvider.class).values()) {
            for (RdmEventDefinition<?> event : provider.getEvents()) { register(event); }
        }
    }
    /** 注册在模块初始化回调完成。 */
    @Override
    protected void myInit() { }
}
