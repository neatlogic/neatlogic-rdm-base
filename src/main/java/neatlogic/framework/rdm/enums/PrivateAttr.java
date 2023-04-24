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
    CATALOG(AttrType.CATALOG),
    WORKER(AttrType.WORKER),
    PRIORITY(AttrType.PRIORITY),
    TAG(AttrType.TAG);

    private final AttrType type;


    PrivateAttr(AttrType _type) {

        this.type = _type;
    }

    public String getName() {
        return type.getName();
    }

    public String getLabel() {
        return type.getLabel();
    }

    public String getType() {
        return type.getType();
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
