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
import neatlogic.framework.rdm.exception.event.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 沿用模块初始化工厂机制，按稳定对象类型管理业务适配器。 */
@RootComponent
public class RdmEventObjectAdapterFactory extends ModuleInitializedListenerBase {
    private static final Map<String, IRdmEventObjectAdapter<?>> ADAPTERS = new ConcurrentHashMap<>();

    /** 注册唯一对象类型，重复标识立即失败以免覆盖其他模块实现。 */
    public static void register(IRdmEventObjectAdapter<?> adapter) {
        if (adapter == null) { throw new RdmEventObjectAdapterIsEmptyException(); }
        if (adapter.getObjectType() == null || adapter.getObjectType().trim().isEmpty()) {
            throw new RdmEventObjectTypeIsEmptyException();
        }
        if (adapter.getObjectClass() == null) { throw new RdmEventObjectClassIsEmptyException(); }
        if (ADAPTERS.putIfAbsent(adapter.getObjectType(), adapter) != null) {
            throw new RdmEventObjectAdapterDuplicateException();
        }
    }

    /** 只在核对精确类型后恢复泛型，调用方无需转换业务对象。 */
    @SuppressWarnings("unchecked")
    public static <T> IRdmEventObjectAdapter<T> getAdapter(String objectType, Class<T> objectClass) {
        if (objectType == null || objectType.trim().isEmpty()) { throw new RdmEventObjectTypeIsEmptyException(); }
        if (objectClass == null) { throw new RdmEventObjectClassIsEmptyException(); }
        IRdmEventObjectAdapter<?> adapter = ADAPTERS.get(objectType);
        if (adapter == null) { throw new RdmEventObjectAdapterNotFoundException(); }
        if (!objectClass.equals(adapter.getObjectClass())) { throw new RdmEventObjectAdapterClassMismatchException(); }
        return (IRdmEventObjectAdapter<T>) adapter;
    }

    /** 从每个已完成初始化的模块容器发现适配器 Bean。 */
    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        for (IRdmEventObjectAdapter<?> adapter : context.getBeansOfType(IRdmEventObjectAdapter.class).values()) {
            register(adapter);
        }
    }

    /** 注册工作在模块初始化通知中完成。 */
    @Override
    protected void myInit() { }
}
