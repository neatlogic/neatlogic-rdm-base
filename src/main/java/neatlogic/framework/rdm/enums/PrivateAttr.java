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
