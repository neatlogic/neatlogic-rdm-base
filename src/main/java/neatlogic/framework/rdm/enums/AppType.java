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

package neatlogic.framework.rdm.enums;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.common.constvalue.IEnum;
import neatlogic.framework.util.$;

import java.util.ArrayList;
import java.util.List;

public enum AppType implements IEnum<JSONObject> {
    ITERATION("iteration", "common.iteration", "#87CEEB", null, false),
    STORY("story", "common.request", "#1670f0", new AttrType[]{AttrType.ITERATION, AttrType.CATALOG, AttrType.WORKER, AttrType.TAG, AttrType.PRIORITY, AttrType.STARTDATE, AttrType.ENDDATE}, true),
    TASK("task", "common.task", "#25b864", new AttrType[]{AttrType.TAG, AttrType.PRIORITY, AttrType.WORKER, AttrType.STARTDATE, AttrType.ENDDATE}, true),
    BUG("bug", "common.bug", "#f33b3b", new AttrType[]{AttrType.TAG, AttrType.PRIORITY, AttrType.WORKER, AttrType.STARTDATE, AttrType.ENDDATE}, true),
    TESTPLAN("testplan", "common.testplan", "#FFA500", new AttrType[]{AttrType.TAG, AttrType.PRIORITY}, false),
    TESTCASE("testcase", "common.testcase", "#4B0082", new AttrType[]{AttrType.CATALOG, AttrType.WORKER, AttrType.TAG}, false);


    private final String name;
    private final String label;
    private final String color;
    private final AttrType[] attrList;

    private final boolean hasIssue;


    AppType(String name, String label, String color, AttrType[] attrList, Boolean hasIssue) {
        this.name = name;
        this.label = label;
        this.color = color;
        this.attrList = attrList;
        this.hasIssue = hasIssue;
    }


    public String getName() {
        return name;
    }

    public boolean getHasIssue() {
        return hasIssue;
    }

    public String getLabel() {
        return $.t(label);
    }

    public String getColor() {
        return color;
    }

    public AttrType[] getAttrList() {
        return attrList;
    }

    public static AttrType[] getAttrList(String name) {
        for (AppType s : AppType.values()) {
            if (s.getName().equals(name)) {
                return s.getAttrList();
            }
        }
        return null;
    }

    public static String getLabel(String name) {
        for (AppType s : AppType.values()) {
            if (s.getName().equals(name)) {
                return s.getLabel();
            }
        }
        return "";
    }

    public static boolean getHasIssue(String name) {
        for (AppType s : AppType.values()) {
            if (s.getName().equals(name)) {
                return s.getHasIssue();
            }
        }
        return false;
    }

    public static String getColor(String name) {
        for (AppType s : AppType.values()) {
            if (s.getName().equals(name)) {
                return s.getColor();
            }
        }
        return "";
    }


    @Override
    public List<JSONObject> getValueTextList() {
        List<JSONObject> array = new ArrayList<>();
        for (AppType appType : AppType.values()) {
            array.add(new JSONObject() {
                {
                    this.put("value", appType.getName());
                    this.put("text", appType.getLabel());
                }
            });
        }
        return array;
    }

    @Override
    public String getValue() {
        return name;
    }

    @Override
    public String getEnumName() {
        return "项目对象类型";
    }
}
