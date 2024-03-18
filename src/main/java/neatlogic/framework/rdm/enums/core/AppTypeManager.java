/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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
