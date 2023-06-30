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
import neatlogic.framework.util.$;

import java.util.ArrayList;
import java.util.List;

public enum ProjectUserType implements IEnum<JSONObject> {
    OWNER("owner", "term.rdm.projectowner"),
    LEADER("leader", "term.rdm.projectleader"),
    MEMBER("member", "term.rdm.projectmember");;


    private final String value;
    private final String text;

    ProjectUserType(String _value, String _text) {
        this.value = _value;
        this.text = _text;
    }

    @Override
    public List<JSONObject> getValueTextList() {
        List<JSONObject> resultList = new ArrayList<>();
        for (ProjectUserType e : values()) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("value", e.getValue());
            jsonObj.put("text", e.getText());
            resultList.add(jsonObj);
        }
        return resultList;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getEnumName() {
        return "nfre.projectusertype.getenumname";
    }

    public String getText() {
        return $.t(text);
    }

    public static ProjectUserType get(String name) {
        for (ProjectUserType s : ProjectUserType.values()) {
            if (s.getValue().equals(name)) {
                return s;
            }
        }
        return null;
    }


    public static String getText(String name) {
        for (ProjectUserType s : ProjectUserType.values()) {
            if (s.getValue().equals(name)) {
                return s.getText();
            }
        }
        return "";
    }
}
