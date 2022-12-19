/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.rdm.enums;

public enum ObjectType {
    REQUEST("request", "需求"), TASK("task", "任务"), BUG("bug", "缺陷"), TESTPLAN("testplan", "测试计划"), TESTCASE("testcase", "测试用例");

    private final String value;
    private final String text;

    ObjectType(String _value, String _text) {
        this.value = _value;
        this.text = _text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }


    public static String getText(String name) {
        for (ObjectType s : ObjectType.values()) {
            if (s.getValue().equals(name)) {
                return s.getText();
            }
        }
        return "";
    }
}
