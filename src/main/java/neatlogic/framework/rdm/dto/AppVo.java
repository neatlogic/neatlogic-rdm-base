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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
issue包括需求、任务、测试计划、测试用例、缺陷等
 */
public class AppVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "term.rdm.projectid", type = ApiParamType.LONG)
    private Long projectId;
    @EntityField(name = "common.type", type = ApiParamType.ENUM, member = AppVo.class)
    private String type;
    @EntityField(name = "common.sort", type = ApiParamType.INTEGER)
    private int sort;
    @EntityField(name = "common.name", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "common.color", type = ApiParamType.STRING)
    private String color;
    @EntityField(name = "common.attributelist", type = ApiParamType.JSONARRAY)
    private List<AppAttrVo> attrList;

    @EntityField(name = "common.statuslist", type = ApiParamType.JSONARRAY)
    private List<AppStatusVo> statusList;
    @EntityField(name = "nfrd.appvo.entityfield.name.hasissue", type = ApiParamType.BOOLEAN)
    private Boolean hasIssue;
    @EntityField(name = "nfrd.appvo.entityfield.name.hasiteration", type = ApiParamType.BOOLEAN)
    private Boolean hasIteration;
    @EntityField(name = "term.rdm.issuecount", type = ApiParamType.INTEGER)
    private Integer issueCount;
    @EntityField(name = "common.isactive", type = ApiParamType.INTEGER)
    private Integer isActive;
    @EntityField(name = "common.config", type = ApiParamType.JSONOBJECT)
    private JSONObject config;
    @JSONField(serialize = false)
    private String configStr;

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

    public List<AppStatusVo> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<AppStatusVo> statusList) {
        this.statusList = statusList;
    }

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public String getName() {
        if (StringUtils.isBlank(name) && StringUtils.isNotBlank(type)) {
            name = AppTypeManager.getLabel(type);
        }
        return name;
    }

    public String getColor() {
        if (StringUtils.isBlank(color) && StringUtils.isNotBlank(type)) {
            color = AppTypeManager.getColor(type);
        }
        return color;
    }

    public Boolean getHasIssue() {
        if (hasIssue == null && StringUtils.isNotBlank(type)) {
            hasIssue = AppTypeManager.getHasIssue(type);
        }
        return hasIssue;
    }

    public Boolean getHasIteration() {
        if (hasIteration == null && StringUtils.isNotBlank(type)) {
            AttrType[] attrTypeList = AppTypeManager.getAttrList(type);
            if (attrTypeList != null) {
                hasIteration = Arrays.stream(attrTypeList).anyMatch(d -> d == AttrType.ITERATION);
            } else {
                hasIteration = false;
            }
        } else {
            hasIteration = false;
        }
        return hasIteration;
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

    public JSONObject getConfig() {
        if (config == null) {
            if (StringUtils.isNotBlank(configStr)) {
                try {
                    config = JSONObject.parseObject(configStr);
                } catch (Exception ignored) {
                    config = new JSONObject();
                }
            } else {
                config = new JSONObject();
            }
        }
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }

    public String getConfigStr() {
        if (config != null) {
            configStr = config.toJSONString();
        }
        return configStr;
    }

    public void setConfigStr(String configStr) {
        this.configStr = configStr;
    }
}
