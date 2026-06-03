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
            IAppType[] appTypes = c.getEnumConstants();
            boolean isValid = false;
            for (IAppType appType : appTypes) {
                isValid = appType.isValid();
            }
            if (isValid) {
                Collections.addAll(appTypeSet, c.getEnumConstants());
            }
        }
    }

    public static List<IAppType> getAppTypeList() {
        List<IAppType> appTypeList = new ArrayList<>(appTypeSet);
        appTypeList.sort(Comparator.comparingInt(IAppType::getSort));
        return appTypeList;
    }

    public static boolean isContain(String appType) {
        for (IAppType s : appTypeSet) {
            if (s.getName().equalsIgnoreCase(appType)) {
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

    public static boolean getNeedCopyOnRel(String name) {
        for (IAppType s : appTypeSet) {
            if (s.getName().equals(name)) {
                return s.getNeedCopyOnRel();
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
