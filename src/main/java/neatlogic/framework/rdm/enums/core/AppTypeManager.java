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

package neatlogic.framework.rdm.enums.core;

import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.reflection.ReflectionManager;
import org.reflections.Reflections;

import java.util.*;

public class AppTypeManager {
    private static final Set<IAppType> appTypeSet = new HashSet<>();

    static {
        Reflections reflections = ReflectionManager.getInstance();
        Set<Class<? extends IAppType>> appTypeClass = reflections.getSubTypesOf(IAppType.class);
        for (Class<? extends IAppType> c : appTypeClass) {
            Collections.addAll(appTypeSet, c.getEnumConstants());
        }
    }

    public static List<IAppType> getAppTypeList() {
        return new ArrayList<>(appTypeSet);
    }

    public static boolean isContain(String appType) {
        for (IAppType s : appTypeSet) {
            if (s.getName().equals(appType)) {
                return true;
            }
        }
        return false;
    }

    public static AttrType[] getAttrList(String name) {
        for (IAppType s : appTypeSet) {
            if (s.getName().equals(name)) {
                return s.getAttrList();
            }
        }
        return null;
    }

    public static String getLabel(String name) {
        for (IAppType s : appTypeSet) {
            if (s.getName().equals(name)) {
                return s.getLabel();
            }
        }
        return "";
    }

    public static boolean getHasIssue(String name) {
        for (IAppType s : appTypeSet) {
            if (s.getName().equals(name)) {
                return s.getHasIssue();
            }
        }
        return false;
    }

    public static String getColor(String name) {
        for (IAppType s : appTypeSet) {
            if (s.getName().equals(name)) {
                return s.getColor();
            }
        }
        return "";
    }

    public static IAppType get(String name) {
        for (IAppType s : appTypeSet) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }
}
