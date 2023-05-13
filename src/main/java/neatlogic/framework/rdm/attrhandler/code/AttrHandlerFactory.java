/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package neatlogic.framework.rdm.attrhandler.code;

import neatlogic.framework.applicationlistener.core.ModuleInitializedListenerBase;
import neatlogic.framework.bootstrap.NeatLogicWebApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AttrHandlerFactory extends ModuleInitializedListenerBase {
    private static final Map<String, IAttrValueHandler> componentMap = new HashMap<>();

    public static IAttrValueHandler getHandler(String name) {
        return componentMap.get(name);
    }

    @Override
    protected void onInitialized(NeatLogicWebApplicationContext context) {
        Map<String, IAttrValueHandler> map = context.getBeansOfType(IAttrValueHandler.class);
        for (Map.Entry<String, IAttrValueHandler> entry : map.entrySet()) {
            componentMap.put(entry.getValue().getName(), entry.getValue());
        }
    }

    @Override
    protected void myInit() {

    }
}
