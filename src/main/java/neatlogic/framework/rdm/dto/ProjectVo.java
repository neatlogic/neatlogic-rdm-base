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
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ProjectVo extends BaseEditorVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;


    @EntityField(name = "common.name", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "common.color", type = ApiParamType.STRING)
    private String color;
    @EntityField(name = "common.templateid", type = ApiParamType.LONG)
    private Long templateId;
    @EntityField(name = "term.rdm.projecttype", type = ApiParamType.STRING)
    private String type;
    @EntityField(name = "common.description", type = ApiParamType.STRING)
    private String description;
    @EntityField(name = "common.startdate", type = ApiParamType.LONG)
    private Date startDate;
    @EntityField(name = "common.enddate", type = ApiParamType.LONG)
    private Date endDate;
    @EntityField(name = "common.progress", type = ApiParamType.INTEGER)
    private Integer progress;
    @EntityField(name = "common.userlist", type = ApiParamType.JSONARRAY)
    private List<ProjectUserVo> userList;
    @EntityField(name = "term.rdm.applist", type = ApiParamType.JSONARRAY)
    private List<AppVo> appList;
    @EntityField(name = "term.rdm.startenddate", type = ApiParamType.JSONARRAY)
    private List<String> dateRange;
    @JSONField(serialize = false)
    private List<String> startDateRange;
    @JSONField(serialize = false)
    private List<String> endDateRange;
    @EntityField(name = "term.rdm.project.useridlist", type = ApiParamType.JSONARRAY)
    private List<String> userIdList;
    @EntityField(name = "term.rdm.project.manageridlist", type = ApiParamType.JSONARRAY)
    private List<String> leaderIdList;
    @EntityField(name = "page.isclose", type = ApiParamType.INTEGER)
    private Integer isClose;
    @EntityField(name = "term.rdm.issuecount", type = ApiParamType.INTEGER)
    private Integer issueCount;


    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getUserIdList() {
        if (CollectionUtils.isNotEmpty(userList) && CollectionUtils.isEmpty(userIdList)) {
            userIdList = new ArrayList<>();
            for (ProjectUserVo userVo : userList) {
                if (userVo.getUserTypeList() != null && userVo.getUserTypeList().stream().anyMatch(d -> d.getUserType().equalsIgnoreCase(ProjectUserType.MEMBER.getValue()))) {
                    userIdList.add("user#" + userVo.getUserId());
                }
            }
        }
        return userIdList;
    }

    public Integer getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(Integer issueCount) {
        this.issueCount = issueCount;
    }

    public Integer getIsClose() {
        return isClose;
    }

    public void setIsClose(Integer isClose) {
        this.isClose = isClose;
    }

    public List<String> getLeaderIdList() {
        if (CollectionUtils.isNotEmpty(userList) && CollectionUtils.isEmpty(leaderIdList)) {
            leaderIdList = new ArrayList<>();
            for (ProjectUserVo userVo : userList) {
                if (userVo.getUserTypeList() != null && userVo.getUserTypeList().stream().anyMatch(d -> d.getUserType().equalsIgnoreCase(ProjectUserType.LEADER.getValue()))) {
                    leaderIdList.add("user#" + userVo.getUserId());
                }
            }
        }
        return leaderIdList;
    }


    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<AppVo> getAppList() {
        if (CollectionUtils.isNotEmpty(appList)) {
            appList.removeIf(d -> !AppTypeManager.isContain(d.getType()));
        }
        return appList;
    }

    public void setAppList(List<AppVo> appList) {
        this.appList = appList;
    }

    public List<String> getDateRange() {
        if (dateRange == null) {
            dateRange = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (startDate != null) {
                dateRange.add(sdf.format(startDate));
            }
            if (endDate != null) {
                dateRange.add(sdf.format(endDate));
            }
        }
        return dateRange;
    }

    public void setDateRange(List<String> dateRange) {
        this.dateRange = dateRange;
    }

    public List<String> getStartDateRange() {
        return startDateRange;
    }

    public void setStartDateRange(List<String> startDateRange) {
        this.startDateRange = startDateRange;
    }

    public List<String> getEndDateRange() {
        return endDateRange;
    }

    public void setEndDateRange(List<String> endDateRange) {
        this.endDateRange = endDateRange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Date getStartDate() {
        if (startDate == null && dateRange != null && dateRange.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                startDate = sdf.parse(dateRange.get(0));
            } catch (ParseException ignored) {

            }
        }
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        if (endDate == null && dateRange != null && dateRange.size() > 1) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                endDate = sdf.parse(dateRange.get(1));
            } catch (ParseException ignored) {
            }
        }
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public List<ProjectUserVo> getUserList() {
        if (CollectionUtils.isNotEmpty(userList)) {
            userList.sort(Comparator.comparing(ProjectUserVo::getMainUserType));
        }
        return userList;
    }

    public void setUserList(List<ProjectUserVo> userList) {
        this.userList = userList;
    }
}
