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
