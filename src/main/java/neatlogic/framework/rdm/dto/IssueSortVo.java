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
                    return "riu.`user_id`";
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
