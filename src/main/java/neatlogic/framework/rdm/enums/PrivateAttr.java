/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.rdm.enums;

public enum PrivateAttr {
    NAME("name", "名称", 1, AttrType.TEXT),
    STATUS("status", "状态", 1, AttrType.STATUS),
    PRIORITY("priority", "优先级", 0, AttrType.PRIORITY),
    CREATE_USER("createuser", "创建人", 1, AttrType.USER),
    WORKER("worker", "责任人", 0, AttrType.USER),
    TAG("tag", "标签", 0, AttrType.TAG);

    private final String name;
    private final String label;
    private final AttrType type;

    private final Integer isRequired;

    PrivateAttr(String _value, String _text, Integer _isRequired, AttrType _type) {
        this.name = _value;
        this.label = _text;
        this.type = _type;
        this.isRequired = _isRequired;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type.getName();
    }

    public static String getType(String name) {
        for (PrivateAttr s : PrivateAttr.values()) {
            if (s.getName().equals(name)) {
                return s.getType();
            }
        }
        return "";
    }

    public static String getLabel(String name) {
        for (PrivateAttr s : PrivateAttr.values()) {
            if (s.getName().equals(name)) {
                return s.getLabel();
            }
        }
        return "";
    }
}
