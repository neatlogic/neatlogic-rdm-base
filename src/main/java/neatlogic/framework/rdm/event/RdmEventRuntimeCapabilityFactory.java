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
import neatlogic.framework.rdm.exception.event.RdmEventCapabilityProviderDuplicateException;
import neatlogic.framework.rdm.exception.event.RdmEventCapabilityProviderEmptyException;
import neatlogic.framework.rdm.exception.event.RdmEventCapabilityUnavailableException;
import org.springframework.context.event.ContextClosedEvent;

/** 从模块容器发现唯一运行能力；未安装提供方时默认关闭。 */
@RootComponent
public class RdmEventRuntimeCapabilityFactory extends ModuleInitializedListenerBase {
    private static volatile IRdmEventRuntimeCapability capability;
    private static NeatLogicWebApplicationContext owner;

    /** 拒绝空提供方和重复注册，避免商业能力被其他模块覆盖。 */
    public static synchronized void register(IRdmEventRuntimeCapability provider) {
        if (provider == null) { throw new RdmEventCapabilityProviderEmptyException(); }
        if (capability != null) { throw new RdmEventCapabilityProviderDuplicateException(); }
        capability = provider;
    }

    /** 无提供方或提供方授权不可用时关闭能力。 */
    public static boolean isAvailable() {
        IRdmEventRuntimeCapability provider = capability;
        return provider != null && provider.isAvailable();
    }

    /** 配置管理及插件执行不得绕过统一能力检查。 */
    public static void requireAvailable() {
        if (!isAvailable()) { throw new RdmEventCapabilityUnavailableException(); }
    }

    /** 只注册模块自身的能力 Bean，并在所属容器关闭时清理引用。 */
    @Override
    protected synchronized void onInitialized(NeatLogicWebApplicationContext context) {
        synchronized (RdmEventRuntimeCapabilityFactory.class) {
            if (owner == context) { return; }
            for (IRdmEventRuntimeCapability provider : context.getBeansOfType(IRdmEventRuntimeCapability.class).values()) {
                register(provider);
                owner = context;
                context.addApplicationListener((ContextClosedEvent event) -> {
                    synchronized (RdmEventRuntimeCapabilityFactory.class) {
                        if (event.getApplicationContext() == owner && capability == provider) {
                            capability = null;
                            owner = null;
                        }
                    }
                });
            }
        }
    }

    /** 注册由模块初始化回调完成。 */
    @Override
    protected void myInit() { }
}
