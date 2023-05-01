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
    NUMBER("number", "number", new I18n("enum.rdm.attrtype.number"), false, false),
    TEXT("text", "text", new I18n("enum.rdm.attrtype.text"), false, false),
    TEXTAREA("textarea", "textarea", new I18n("enum.rdm.attrtype.textarea"), false, false),
    SELECT("select", "select", new I18n("enum.rdm.attrtype.select"), false, true),
    DATE("date", "date", new I18n("enum.rdm.attrtype.date"), false, false),
    DATETIME("datetime", "datetime", new I18n("enum.rdm.attrtype.datetime"), false, false),
    TIME("time", "time", new I18n("enum.rdm.attrtype.time"), false, false),
    FILE("file", "file", new I18n("enum.rdm.attrtype.file"), false, false),
    PRIORITY("priority", "priority", new I18n("enum.rdm.attrtype.priority"), true, false),
    TAG("tag", "tagList", new I18n("enum.rdm.attrtype.tag"), true, true),
    WORKER("worker", "userIdList", new I18n("enum.rdm.attrtype.worker"), true, true),
    CATALOG("catalog", "catalog", new I18n("enum.rdm.attrtype.catalog"), true, false);

    private final String name;
    //private final String label;
    private final String type;
    private final I18n label;

    private final boolean isPrivate;

    private final boolean isArray;

    AttrType(String _type, String _name, I18n _text, Boolean _isPrivate, Boolean _isArray) {
        this.type = _type;
        this.name = _name;
        this.label = _text;
        this.isPrivate = _isPrivate;
        this.isArray = _isArray;
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
        return label.toString();
    }

    public boolean isArray() {
        return isArray;
    }

    public static String getLabel(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getName().equals(name)) {
                return s.getLabel();
            }
        }
        return "";
    }

    public static boolean getIsArray(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getName().equals(name)) {
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

    @Override
    public String getValue() {
        return name;
    }

    @Override
    public String getEnumName() {
        return "项目对象属性类型";
    }
}
