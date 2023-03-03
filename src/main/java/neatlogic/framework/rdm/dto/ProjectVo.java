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

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.collections4.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectVo extends BaseEditorVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "颜色标识", type = ApiParamType.STRING)
    private String color;
    @EntityField(name = "模板id", type = ApiParamType.LONG)
    private Long templateId;
    @EntityField(name = "项目类型", type = ApiParamType.STRING)
    private String type;
    @EntityField(name = "说明", type = ApiParamType.STRING)
    private String description;
    @EntityField(name = "开始日期", type = ApiParamType.LONG)
    private Date startDate;
    @EntityField(name = "开始日期", type = ApiParamType.LONG)
    private Date endDate;
    @EntityField(name = "进度", type = ApiParamType.INTEGER)
    private Integer progress;
    @EntityField(name = "用户列表", type = ApiParamType.JSONARRAY)
    private List<ProjectUserVo> userList;
    @EntityField(name = "对象列表", type = ApiParamType.JSONARRAY)
    private List<ObjectVo> objectList;
    @EntityField(name = "开始日期-结束日期", type = ApiParamType.JSONARRAY)
    private List<String> dateRange;
    @JSONField(serialize = false)
    private List<String> startDateRange;
    @JSONField(serialize = false)
    private List<String> endDateRange;
    @EntityField(name = "项目成员用户id列表", type = ApiParamType.JSONARRAY)
    private List<String> memberIdList;
    @EntityField(name = "项目负责人id列表", type = ApiParamType.JSONARRAY)
    private List<String> leaderIdList;


    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getMemberIdList() {
        if (CollectionUtils.isNotEmpty(userList) && CollectionUtils.isEmpty(memberIdList)) {
            memberIdList = new ArrayList<>();
            for (ProjectUserVo userVo : userList) {
                if (userVo.getUserType().equals(ProjectUserType.MEMBER.getValue())) {
                    memberIdList.add("user#" + userVo.getUserId());
                }
            }
        }
        return memberIdList;
    }

    public List<String> getLeaderIdList() {
        if (CollectionUtils.isNotEmpty(userList) && CollectionUtils.isEmpty(leaderIdList)) {
            leaderIdList = new ArrayList<>();
            for (ProjectUserVo userVo : userList) {
                if (userVo.getUserType().equals(ProjectUserType.LEADER.getValue())) {
                    leaderIdList.add("user#" + userVo.getUserId());
                }
            }
        }
        return leaderIdList;
    }


    public void setMemberIdList(List<String> memberIdList) {
        this.memberIdList = memberIdList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ObjectVo> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<ObjectVo> objectList) {
        this.objectList = objectList;
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
        return userList;
    }

    public void setUserList(List<ProjectUserVo> userList) {
        this.userList = userList;
    }
}
