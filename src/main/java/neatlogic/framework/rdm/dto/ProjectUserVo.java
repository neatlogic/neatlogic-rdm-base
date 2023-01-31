/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.rdm.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.enums.ProjectUserType;
import neatlogic.framework.restful.annotation.EntityField;

public class ProjectUserVo {
    @EntityField(name = "用户id", type = ApiParamType.STRING)
    private String userId;
    @EntityField(name = "用户名", type = ApiParamType.STRING)
    private String userName;
    @EntityField(name = "用户类型", type = ApiParamType.ENUM, member = ProjectUserType.class)
    private String userType;
    @EntityField(name = "项目id", type = ApiParamType.LONG)
    private Long projectId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
