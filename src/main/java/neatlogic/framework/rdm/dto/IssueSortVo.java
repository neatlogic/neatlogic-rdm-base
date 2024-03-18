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

package neatlogic.framework.rdm.dto;

import neatlogic.framework.rdm.enums.AttrType;
import org.apache.commons.lang3.StringUtils;

public class IssueSortVo {

    //属性名称
    private String attr;
    //排序方式
    private String sort;
    //属性类型
    private String type;

    private Boolean isCustom;
    //DB字段
    private String field;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean geIsCustom() {
        if (StringUtils.isNotBlank(this.attr) && StringUtils.isNotBlank(this.type)) {
            if (!this.attr.startsWith("_")) {
                if (this.type.equalsIgnoreCase(AttrType.CATALOG.getType())) {
                    return false;
                } else if (this.type.equalsIgnoreCase(AttrType.PRIORITY.getType())) {
                    return false;
                } else if (this.type.equalsIgnoreCase(AttrType.ITERATION.getType())) {
                    return false;
                } else if (this.type.equalsIgnoreCase(AttrType.WORKER.getType())) {
                    return false;
                } else if (this.type.equalsIgnoreCase(AttrType.TAG.getType())) {
                    return false;
                } else if (this.type.equalsIgnoreCase(AttrType.STARTDATE.getType())) {
                    return false;
                } else if (this.type.equalsIgnoreCase(AttrType.ENDDATE.getType())) {
                    return false;
                } else if (this.type.equalsIgnoreCase(AttrType.TIMECOST.getType())) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public void setIsCustom(Boolean custom) {
        isCustom = custom;
    }

    /**
     * 转换数据库字段，需要和SQL语句配合生成
     */
    public String getField() {
        if (StringUtils.isNotBlank(this.attr) && StringUtils.isNotBlank(this.type)) {
            if (this.attr.startsWith("_")) {
                switch (this.attr) {
                    case "_name":
                        return "ri.`name`";
                    case "_status":
                        return "ri.`status`";
                    case "_createdate":
                        return "ri.`create_date`";
                    case "_createuser":
                        return "ri.`create_user`";
                }
            } else {
                if (this.type.equalsIgnoreCase(AttrType.CATALOG.getType())) {
                    return "rac.`name`";
                } else if (this.type.equalsIgnoreCase(AttrType.PRIORITY.getType())) {
                    return "ri.`priority`";
                } else if (this.type.equalsIgnoreCase(AttrType.ITERATION.getType())) {
                    return "ri.`iteration`";
                } else if (this.type.equalsIgnoreCase(AttrType.WORKER.getType())) {
                    return "u.`user_name`";
                } else if (this.type.equalsIgnoreCase(AttrType.TAG.getType())) {
                    return "rt.`name`";
                } else if (this.type.equalsIgnoreCase(AttrType.STARTDATE.getType())) {
                    return "ri.`start_date`";
                } else if (this.type.equalsIgnoreCase(AttrType.ENDDATE.getType())) {
                    return "ri.`end_date`";
                } else if (this.type.equalsIgnoreCase(AttrType.TIMECOST.getType())) {
                    return "ri.`timecost`";
                } else {
                    return "x.`" + this.attr + "`";
                }
            }
        }
        return field;
    }

}
