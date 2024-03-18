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

public enum AttrType implements IEnum<JSONObject> {
    NUMBER("number", "number", "common.number"),
    TEXT("text", "text", "common.text"),
    TEXTAREA("textarea", "textarea", "common.textbox"),
    SELECT("select", "select", "common.dropdownlist"),
    DATE("date", "date", "common.date"),
    DATETIME("datetime", "datetime", "common.datetime"),
    TIME("time", "time", "common.time"),
    FILE("file", "file", "common.attachment"),
    PRIORITY("priority", "priority", "common.priority"),
    UESR("user", "user", "common.user"),
    TAG("tag", "tagList", "common.tag"),
    WORKER("worker", "userIdList", "common.worker"),
    CATALOG("catalog", "catalog", "common.catalog"),
    ITERATION("iteration", "iteration", "common.iteration"),
    ENDDATE("enddate", "endDate", "term.rdm.enddate"),
    STARTDATE("startdate", "startDate", "term.rdm.startdate"),

    TIMECOST("timecost", "timecost", "term.rdm.plantimecost");

    private final String name;
    //private final String label;
    private final String type;
    private final String label;


    AttrType(String type, String name, String label) {
        this.type = type;
        this.name = name;
        this.label = label;
    }


    public String getType() {
        return type;
    }


    public String getName() {
        return name;
    }

    public String getLabel() {
        return $.t(label);
    }


    public static String getLabel(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getType().equalsIgnoreCase(name)) {
                return s.getLabel();
            }
        }
        return "";
    }


    public static AttrType get(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getType().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
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
