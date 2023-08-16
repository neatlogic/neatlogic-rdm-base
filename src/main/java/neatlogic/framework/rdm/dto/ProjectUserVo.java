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
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.restful.annotation.EntityField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ProjectUserVo extends BasePageVo {
    @EntityField(name = "common.userid", type = ApiParamType.STRING)
    private String userId;
    @EntityField(name = "common.username", type = ApiParamType.STRING)
    private String userName;
    @EntityField(name = "common.userenname", type = ApiParamType.STRING)
    private String userEnName;

    @JSONField(serialize = false)
    private String userType;
    @JSONField(serialize = false)
    private String userTypeName;
    @EntityField(name = "nfrd.projectuservo.entityfield.name", type = ApiParamType.JSONARRAY)
    private List<ProjectUserTypeVo> userTypeList;
    @EntityField(name = "term.rdm.projectid", type = ApiParamType.LONG)
    private Long projectId;
    @JSONField(serialize = false)
    private List<String> userIdList;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public List<ProjectUserTypeVo> getUserTypeList() {
        if (CollectionUtils.isNotEmpty(userTypeList)) {
            userTypeList.sort(Comparator.comparing(o -> Objects.requireNonNull(ProjectUserType.get(o.getUserType()))));
        }
        return userTypeList;
    }

    public String getUserEnName() {
        return userEnName;
    }

    public void setUserEnName(String userEnName) {
        this.userEnName = userEnName;
    }

    public ProjectUserType getMainUserType() {
        if (CollectionUtils.isNotEmpty(this.getUserTypeList())) {
            return ProjectUserType.get(this.getUserTypeList().get(0).getUserType());
        }
        return ProjectUserType.MEMBER;
    }

    public void setUserTypeList(List<ProjectUserTypeVo> userTypeList) {
        this.userTypeList = userTypeList;
    }

    public String getUserTypeName() {
        if (StringUtils.isNotBlank(userType) && StringUtils.isBlank(userTypeName)) {
            userTypeName = ProjectUserType.getText(userType);
        }
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }
}
