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
    TEXT("text", new I18n("enum.rdm.attrtype.text"), 1),
    TEXTAREA("textarea", new I18n("enum.rdm.attrtype.textarea"), 1),
    SELECT("select", new I18n("enum.rdm.attrtype.select"), 1),
    DATE("date", new I18n("enum.rdm.attrtype.date"), 1),
    DATETIME("datetime", new I18n("enum.rdm.attrtype.datetime"), 1),
    TIME("time", new I18n("enum.rdm.attrtype.time"), 1),
    //DATETIMERANGE("datetimerange", new I18n("enum.rdm.attrtype.datetimerange"), 1),
    FILE("file", new I18n("enum.rdm.attrtype.file"), 1),
    STATUS("status", new I18n("enum.rdm.attrtype.status"), 0),
    PRIORITY("priority", new I18n("enum.rdm.attrtype.priority"), 0),
    USER("user", new I18n("enum.rdm.attrtype.user"), 1),
    TAG("tag", new I18n("enum.rdm.attrtype.tag"), 0);

    private final String name;
    //private final String label;

    private final I18n label;

    private final Integer allowCustom;

    AttrType(String _value, I18n _text, Integer _allowCustom) {
        this.name = _value;
        this.label = _text;
        this.allowCustom = _allowCustom;
    }

    public Integer getAllowCustom() {
        return allowCustom;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label.toString();
    }


    public static String getLabel(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getName().equals(name)) {
                return s.getLabel();
            }
        }
        return "";
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
