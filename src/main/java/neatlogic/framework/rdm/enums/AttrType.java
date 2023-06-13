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

public enum AttrType implements IEnum<JSONObject> {
    NUMBER("number", "number", "common.number", false, false, null),
    TEXT("text", "text", "common.text", false, false, null),
    TEXTAREA("textarea", "textarea", "common.textbox", false, false, null),
    SELECT("select", "select", "common.dropdownlist", false, true, null),
    DATE("date", "date", "common.date", false, false, null),
    DATETIME("datetime", "datetime", "common.datetime", false, false, null),
    TIME("time", "time", "common.time", false, false, null),
    FILE("file", "file", "common.attachment", false, true, null),
    PRIORITY("priority", "priority", "common.priority", true, false, null),
    UESR("user", "user", "common.user", false, true, null),
    TAG("tag", "tagList", "common.tag", true, true, null),
    WORKER("worker", "userIdList", "common.worker", true, true, null),
    CATALOG("catalog", "catalog", "common.catalog", true, false, null),
    ITERATION("iteration", "iteration", "common.iteration", true, false, "iteration"),
    ENDDATE("enddate", "endDate", "term.rdm.enddate", true, false, null),
    STARTDATE("startdate", "startDate", "term.rdm.startdate", true, false, null);
    private final String name;
    //private final String label;
    private final String type;
    private final String label;

    private final boolean isPrivate;

    private final boolean isArray;

    private final String belong;


    AttrType(String type, String name, String label, Boolean isPrivate, Boolean isArray, String belong) {
        this.type = type;
        this.name = name;
        this.label = label;
        this.isPrivate = isPrivate;
        this.isArray = isArray;
        this.belong = belong;
    }


    public String getType() {
        return type;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return $.t(label);
    }

    public boolean isArray() {
        return isArray;
    }

    public static String getLabel(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getType().equals(name)) {
                return s.getLabel();
            }
        }
        return "";
    }

    public static boolean getIsArray(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getType().equals(name)) {
                return s.isArray();
            }
        }
        return false;
    }

    @Override
    public List<JSONObject> getValueTextList() {
        List<JSONObject> resultList = new ArrayList<>();
        for (AttrType e : values()) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("value", e.getName());
            jsonObj.put("text", e.getLabel());
            resultList.add(jsonObj);
        }
        return resultList;
    }

    public String getBelong() {
        return belong;
    }

    public static String getBelong(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getType().equals(name)) {
                return s.getBelong();
            }
        }
        return null;
    }

    @Override
    public String getValue() {
        return name;
    }

    @Override
    public String getEnumName() {
        return "项目对象属性类型";
    }
}
