/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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
