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
import neatlogic.framework.util.I18n;

import java.util.ArrayList;
import java.util.List;

public enum AttrType implements IEnum<JSONObject> {
    NUMBER("number", "number", new I18n("数字"), false, false, null),
    TEXT("text", "text", new I18n("文本"), false, false, null),
    TEXTAREA("textarea", "textarea", new I18n("文本框"), false, false, null),
    SELECT("select", "select", new I18n("下拉选择"), false, true, null),
    DATE("date", "date", new I18n("日期"), false, false, null),
    DATETIME("datetime", "datetime", new I18n("日期时间"), false, false, null),
    TIME("time", "time", new I18n("时间"), false, false, null),
    FILE("file", "file", new I18n("附件"), false, true, null),
    PRIORITY("priority", "priority", new I18n("优先级"), true, false, null),
    UESR("user", "user", new I18n("用户"), false, true, null),
    TAG("tag", "tagList", new I18n("标签"), true, true, null),
    WORKER("worker", "userIdList", new I18n("处理人"), true, true, null),
    CATALOG("catalog", "catalog", new I18n("目录"), true, false, null),
    ITERATION("iteration", "iteration", "迭代", true, false, "iteration"),
    ENDDATE("enddate", "endDate", "预计结束", true, false, null),
    STARTDATE("startdate", "startDate", "预计开始", true, false, null);
    private final String name;
    //private final String label;
    private final String type;
    private final I18n label;

    private final String labelText;

    private final boolean isPrivate;

    private final boolean isArray;

    private final String belong;


    AttrType(String type, String name, I18n text, Boolean isPrivate, Boolean isArray, String belong) {
        this.type = type;
        this.name = name;
        this.label = text;
        this.isPrivate = isPrivate;
        this.isArray = isArray;
        this.labelText = null;
        this.belong = belong;
    }

    AttrType(String type, String name, String text, Boolean isPrivate, Boolean isArray, String belong) {
        this.type = type;
        this.name = name;
        this.labelText = text;
        this.isPrivate = isPrivate;
        this.isArray = isArray;
        this.label = null;
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
        return label != null ? label.toString() : labelText;
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
