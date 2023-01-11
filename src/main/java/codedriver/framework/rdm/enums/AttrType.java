/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.rdm.enums;

import codedriver.framework.common.constvalue.IEnum;
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
