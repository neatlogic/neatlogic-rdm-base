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

import neatlogic.framework.common.constvalue.IEnum;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public enum AttrType implements IEnum<JSONObject> {
    TEXT("text", "文本", 1),
    TEXTAREA("textarea", "文本框", 1),
    SELECT("select", "下拉选择", 1),
    DATE("date", "日期", 1),
    DATETIME("datetime", "日期时间", 1),
    TIME("time", "时间", 1),
    DATETIMERANGE("datetimerange", "日期范围", 1),
    FILE("file", "附件", 1),
    STATUS("status", "状态", 0),
    PRIORITY("priority", "优先级", 0),
    USER("user", "用户", 1),
    TAG("tag", "标签", 0);

    private final String name;
    private final String label;

    private final Integer allowCustom;

    AttrType(String _value, String _text, Integer _allowCustom) {
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
        return label;
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
