/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.rdm.dto;

import com.alibaba.fastjson.JSONArray;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;

import java.io.Serializable;

public class IssueStatResultVo implements Serializable {
    @EntityField(name = "统计属性标识", type = ApiParamType.STRING)
    private String statKey;
    @EntityField(name = "是否已配置", type = ApiParamType.BOOLEAN)
    private boolean configured;
    @EntityField(name = "统计类型", type = ApiParamType.STRING)
    private String type;
    @EntityField(name = "标题", type = ApiParamType.STRING)
    private String title;
    @EntityField(name = "数值", type = ApiParamType.DOUBLE)
    private Object value;
    @EntityField(name = "筛选字段", type = ApiParamType.STRING)
    private String filterField;
    @EntityField(name = "筛选属性id", type = ApiParamType.LONG)
    private Long filterAttrId;
    @EntityField(name = "数据列表", type = ApiParamType.JSONARRAY)
    private JSONArray dataList;

    public String getStatKey() {
        return statKey;
    }

    public void setStatKey(String statKey) {
        this.statKey = statKey;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getFilterField() {
        return filterField;
    }

    public void setFilterField(String filterField) {
        this.filterField = filterField;
    }

    public Long getFilterAttrId() {
        return filterAttrId;
    }

    public void setFilterAttrId(Long filterAttrId) {
        this.filterAttrId = filterAttrId;
    }

    public JSONArray getDataList() {
        return dataList;
    }

    public void setDataList(JSONArray dataList) {
        this.dataList = dataList;
    }
}
