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
import neatlogic.framework.rdm.dto.AppAttrVo;
import neatlogic.framework.util.$;

import java.util.ArrayList;
import java.util.List;

public enum SystemAttrType implements IEnum<JSONObject> {
    //系统属性只是为了用户设置字段排序时使用，不想再增加标记属性，用_开头代表是系统属性
    NAME("_name", "name", "common.name", "nfre.systemattrtype"),
    STATUS("_status", "status", "common.status", "nfre.systemattrtype"),
    CREATE_USER("_createuser", "createuser", "common.createuser", "nfre.systemattrtype"),
    CREATE_DATE("_createdate", "createdate", "common.createdate", "nfre.systemattrtype");
    private final String name;
    private final String type;
    private final String label;

    private final String typeText;

    public static List<AppAttrVo> getSystemAttrList(Long appId) {
        List<AppAttrVo> systemAttrList = new ArrayList<>();
        for (SystemAttrType attrType : SystemAttrType.values()) {
            AppAttrVo systemAttrVo = new AppAttrVo();
            systemAttrVo.setAppId(appId);
            systemAttrVo.setId(0L);//避免自动分配id
            systemAttrVo.setType(attrType.getType());
            systemAttrVo.setName(attrType.getName());
            systemAttrVo.setLabel(attrType.getLabel());
            systemAttrVo.setTypeText(attrType.getTypeText());
            systemAttrList.add(systemAttrVo);
        }

        return systemAttrList;
    }


    SystemAttrType(String type, String name, String label, String typeText) {
        this.type = type;
        this.name = name;
        this.label = label;
        this.typeText = typeText;
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

    public String getTypeText() {
        return $.t(typeText);
    }


    public static String getLabel(String name) {
        for (SystemAttrType s : SystemAttrType.values()) {
            if (s.getType().equals(name)) {
                return s.getLabel();
            }
        }
        return "";
    }


    @Override
    public List<JSONObject> getValueTextList() {
        List<JSONObject> resultList = new ArrayList<>();
        for (SystemAttrType e : values()) {
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
        return "nfre.systemattrtype.getenumname";
    }
}
