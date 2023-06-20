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

import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.enums.AppType;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/*
issue包括需求、任务、测试计划、测试用例、缺陷等
 */
public class AppVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "项目id", type = ApiParamType.LONG)
    private Long projectId;
    @EntityField(name = "类型", type = ApiParamType.ENUM, member = AppVo.class)
    private String type;
    @EntityField(name = "排序", type = ApiParamType.INTEGER)
    private int sort;
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "颜色", type = ApiParamType.STRING)
    private String color;
    @EntityField(name = "属性列表", type = ApiParamType.JSONARRAY)
    private List<AppAttrVo> attrList;
    @EntityField(name = "是否包含任务", type = ApiParamType.BOOLEAN)
    private Boolean hasIssue;
    @EntityField(name = "任务数量", type = ApiParamType.INTEGER)
    private Integer issueCount;
    @EntityField(name = "是否激活", type = ApiParamType.INTEGER)
    private Integer isActive;

    @JSONField(serialize = false)
    public String getTableName() {
        return TenantContext.get().getDataDbName() + ".`rdm_app_" + this.getId() + "`";
    }

    public void addAppAttr(AppAttrVo appAttrVo) {
        if (this.attrList == null) {
            this.attrList = new ArrayList<>();
        }
        if (!this.attrList.contains(appAttrVo)) {
            this.attrList.add(appAttrVo);
        }
    }

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public String getName() {
        if (StringUtils.isBlank(name) && StringUtils.isNotBlank(type)) {
            name = AppType.getLabel(type);
        }
        return name;
    }

    public String getColor() {
        if (StringUtils.isBlank(color) && StringUtils.isNotBlank(type)) {
            color = AppType.getColor(type);
        }
        return color;
    }

    public Boolean getHasIssue() {
        if (hasIssue == null && StringUtils.isNotBlank(type)) {
            hasIssue = AppType.getHasIssue(type);
        }
        return hasIssue;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<AppAttrVo> getAttrList() {
        return attrList;
    }

    public void setAttrList(List<AppAttrVo> attrList) {
        this.attrList = attrList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Integer getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(Integer issueCount) {
        this.issueCount = issueCount;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}
