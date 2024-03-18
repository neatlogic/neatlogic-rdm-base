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
