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
import neatlogic.framework.rdm.dto.RdmEventHandlerVo;
import neatlogic.framework.rdm.exception.event.*;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import java.util.stream.Collectors;

/** 按接口发现插件，并在唯一分发边界恢复已经验证的类型。 */
@RootComponent
public class RdmEventHandlerFactory extends ModuleInitializedListenerBase {
    private static final Map<String, IRdmEventHandler<?>> eventMap = new HashMap<>();
    private static final List<IRdmEventHandler<?>> pluginList = new ArrayList<>();
    /** 按标识查询插件。 */
    public static IRdmEventHandler<?> getHandler(String handlerName) {
        if (handlerName == null) { return null; }
        return eventMap.get(handlerName);
    }
    /** 获取模块初始化时按名称排序的插件列表。 */
    public static List<IRdmEventHandler<?>> getHandlerList() {
        return pluginList;
    }
    /** 查询适配事件及其对象类型的插件。 */
    public static List<IRdmEventHandler<?>> getHandlerList(String event) {
        return getHandlerList(event, null);
    }
    /** 同时筛选事件与允许的父插件。 */
    public static List<IRdmEventHandler<?>> getHandlerList(String event, String parentPlugin) {
        RdmEventDefinition<?> definition = RdmEventRegistry.get(event);
        return getHandlerList().stream().filter(handler ->
                (StringUtils.isBlank(event) || (definition != null
                        && definition.getObjectClass().equals(handler.getObjectClass())
                        && handler.supportEventTypes().contains(event)))
                && (StringUtils.isBlank(parentPlugin) || handler.supportParentHandler().contains(parentPlugin)))
                .collect(Collectors.toList());
    }
    /** 按实际应用、事件对象类型和父插件筛选，不受前端选项限制影响。 */
    public static List<IRdmEventHandler<?>> getHandlerList(String appType, String event, String parentPlugin) {
        RdmEventDefinition<?> definition = RdmEventRegistry.get(event);
        return getHandlerList().stream().filter(handler -> isSupported(handler, definition, appType, parentPlugin))
                .collect(Collectors.toList());
    }
    /** 不执行配置业务校验，只判断插件是否适用于指定位置。 */
    public static boolean isSupported(IRdmEventHandler<?> handler, RdmEventDefinition<?> event,
                                      String appType, String parentPlugin) {
        return handler != null && event != null && event.getAppTypes().contains(appType)
                && handler.getObjectClass().equals(event.getObjectClass())
                && handler.supportEventTypes().contains(event.getName())
                && (handler.supportAppTypes().isEmpty() || handler.supportAppTypes().contains(appType))
                && (StringUtils.isBlank(parentPlugin) || handler.supportParentHandler().contains(parentPlugin));
    }
    /** 兼容核心调用，应用类型必须通过共享 Mapper 查询取得。 */
    public static void validate(RdmEventHandlerVo config, RdmEventDefinition<?> event) {
        String appType = RdmEventManager.getApplicationType(config.getProjectId(), config.getAppId(), event);
        validate(config, event, appType, null);
    }
    /** 配置保存和运行统一校验应用、事件、精确对象类型、父插件及业务配置。 */
    public static void validate(RdmEventHandlerVo config, RdmEventDefinition<?> event,
                                String appType, String parentPlugin) {
        if (config == null || event == null || !Objects.equals(config.getEvent(), event.getName())) {
            throw new RdmEventConfigurationScopeMismatchException();
        }
        IRdmEventHandler<?> handler = getHandler(config.getHandler());
        if (handler == null) { throw new RdmEventHandlerNotFoundException(); }
        if (event == null || !event.getAppTypes().contains(appType)) {
            throw new RdmEventApplicationNotSupportedException();
        }
        if (!handler.getObjectClass().equals(event.getObjectClass())) {
            throw new RdmEventHandlerObjectClassMismatchException();
        }
        if (!handler.supportEventTypes().contains(event.getName())) {
            throw new RdmEventHandlerEventNotSupportedException();
        }
        if (!handler.supportAppTypes().isEmpty() && !handler.supportAppTypes().contains(appType)) {
            throw new RdmEventHandlerAppNotSupportedException();
        }
        if (StringUtils.isNotBlank(parentPlugin) && !handler.supportParentHandler().contains(parentPlugin)) {
            throw new RdmEventHandlerParentNotSupportedException();
        }
        handler.validateConfig(config.getConfig());
    }
    /** 类型擦除只在已核对双方 Class 的注册分发边界恢复。 */
    @SuppressWarnings("unchecked")
    static <T> T trigger(RdmEventHandlerVo config, RdmEventDefinition<T> event, T object, Long parentAuditId) {
        RdmEventRuntimeCapabilityFactory.requireAvailable();
        validate(config, event);
        RdmEventManager.validateObject(config.getProjectId(), config.getAppId(), event, object, null);
        String objectId = event.getAdapter().getObjectId(object);
        IRdmEventHandler<T> handler = (IRdmEventHandler<T>) getHandler(config.getHandler());
        T result = handler.trigger(config, event, event.getObjectClass().cast(object), parentAuditId);
        RdmEventManager.validateObject(config.getProjectId(), config.getAppId(), event, result, objectId);
        return result;
    }
    /** 模块初始化时注册插件。 */
    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IRdmEventHandler> myMap = context.getBeansOfType(IRdmEventHandler.class);
        for (Map.Entry<String, IRdmEventHandler> entry : myMap.entrySet()) {
            IRdmEventHandler<?> handler = entry.getValue();
            eventMap.put(handler.getName(), handler);
            pluginList.add(handler);
        }
        pluginList.sort(Comparator.comparing(IRdmEventHandler::getName));
    }
    /** 注册统一在模块初始化回调完成。 */
    @Override
    protected void myInit() { }
}
