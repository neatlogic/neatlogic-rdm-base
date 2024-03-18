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

import java.util.List;

public enum IssueRelDirection implements IEnum<JSONObject> {
    //	AGENT("agent","代办人",true),
    FROM("from", "上游"), TO("to", "下游");

    private final String value;
    private final String text;

    IssueRelDirection(String value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public List getValueTextList() {
        return null;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getEnumName() {
        return "任务关联方向";
    }

    public String getText() {
        return text;
    }


    public static String getValue(String value) {
        for (IssueRelDirection s : IssueRelDirection.values()) {
            if (s.getValue().equals(value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getText(String text) {
        for (IssueRelDirection s : IssueRelDirection.values()) {
            if (s.getValue().equals(text)) {
                return s.getText();
            }
        }
        return "";
    }


}
