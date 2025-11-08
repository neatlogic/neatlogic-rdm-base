/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.rdm.webhook;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import neatlogic.framework.common.RootComponent;

import java.util.HashMap;
import java.util.Map;

@RootComponent
public class WebhookFactory extends ModuleInitializedListenerBase {
    private static final Map<String, IWebhookHandler> componentMap = new HashMap<>();

    public static IWebhookHandler getHandler(String handler) {
        return componentMap.get(handler);
    }

    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IWebhookHandler> myMap = context.getBeansOfType(IWebhookHandler.class);
        for (Map.Entry<String, IWebhookHandler> entry : myMap.entrySet()) {
            IWebhookHandler component = entry.getValue();
            if (component.getName() != null) {
                componentMap.put(component.getName(), component);
            }
        }
    }

    @Override
    protected void myInit() {

    }
}
