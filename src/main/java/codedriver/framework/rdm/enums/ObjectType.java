/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.rdm.enums;

import codedriver.framework.common.constvalue.IEnum;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public enum ObjectType implements IEnum<JSONObject> {
    REQUEST("request", "需求", new PrivateAttr[]{PrivateAttr.NAME, PrivateAttr.STATUS, PrivateAttr.TAG, PrivateAttr.CREATE_USER, PrivateAttr.PRIORITY, PrivateAttr.WORKER}), TASK("task", "任务", new PrivateAttr[]{PrivateAttr.NAME, PrivateAttr.STATUS, PrivateAttr.TAG, PrivateAttr.CREATE_USER, PrivateAttr.PRIORITY, PrivateAttr.WORKER}), BUG("bug", "缺陷", new PrivateAttr[]{PrivateAttr.NAME, PrivateAttr.STATUS, PrivateAttr.TAG, PrivateAttr.CREATE_USER, PrivateAttr.PRIORITY, PrivateAttr.WORKER}), TESTPLAN("testplan", "测试计划", new PrivateAttr[]{PrivateAttr.NAME, PrivateAttr.STATUS, PrivateAttr.TAG, PrivateAttr.CREATE_USER, PrivateAttr.PRIORITY, PrivateAttr.WORKER}), TESTCASE("testcase", "测试用例", new PrivateAttr[]{PrivateAttr.NAME, PrivateAttr.CREATE_USER, PrivateAttr.WORKER});

    private final String name;
    private final String label;

    private final PrivateAttr[] attrList;

    ObjectType(String _value, String _text, PrivateAttr[] _attrList) {
        this.name = _value;
        this.label = _text;
        this.attrList = _attrList;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
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
