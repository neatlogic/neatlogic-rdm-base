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

public enum ObjectType implements IEnum<JSONObject> {
    REQUEST("request", new I18n("enum.rdm.objecttype.request"), new PrivateAttr[]{PrivateAttr.NAME, PrivateAttr.STATUS, PrivateAttr.TAG, PrivateAttr.CREATE_USER, PrivateAttr.PRIORITY, PrivateAttr.WORKER}),
    TASK("task", new I18n("enum.rdm.objecttype.task"), new PrivateAttr[]{PrivateAttr.NAME, PrivateAttr.STATUS, PrivateAttr.TAG, PrivateAttr.CREATE_USER, PrivateAttr.PRIORITY, PrivateAttr.WORKER}),
    BUG("bug", new I18n("enum.rdm.objecttype.bug"), new PrivateAttr[]{PrivateAttr.NAME, PrivateAttr.STATUS, PrivateAttr.TAG, PrivateAttr.CREATE_USER, PrivateAttr.PRIORITY, PrivateAttr.WORKER}),
    TESTPLAN("testplan", new I18n("enum.rdm.objecttype.testplan"), new PrivateAttr[]{PrivateAttr.NAME, PrivateAttr.STATUS, PrivateAttr.TAG, PrivateAttr.CREATE_USER, PrivateAttr.PRIORITY, PrivateAttr.WORKER}),
    TESTCASE("testcase", new I18n("enum.rdm.objecttype.testcase"), new PrivateAttr[]{PrivateAttr.NAME, PrivateAttr.CREATE_USER, PrivateAttr.WORKER});

    private final String name;
    private final I18n label;

    private final PrivateAttr[] attrList;

    ObjectType(String _value, I18n _text, PrivateAttr[] _attrList) {
        this.name = _value;
        this.label = _text;
        this.attrList = _attrList;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label.toString();
    }

    public PrivateAttr[] getAttrList() {
        return attrList;
    }

    public static PrivateAttr[] getAttrList(String name) {
        for (ObjectType s : ObjectType.values()) {
            if (s.getName().equals(name)) {
                return s.getAttrList();
            }
        }
        return null;
    }

    public static String getLabel(String name) {
        for (ObjectType s : ObjectType.values()) {
            if (s.getName().equals(name)) {
                return s.getLabel();
            }
        }
        return "";
    }

    @Override
    public List<JSONObject> getValueTextList() {
        List<JSONObject> array = new ArrayList<>();
        for (ObjectType objectType : ObjectType.values()) {
            array.add(new JSONObject() {
                {
                    this.put("value", objectType.getName());
                    this.put("text", objectType.getLabel());
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
