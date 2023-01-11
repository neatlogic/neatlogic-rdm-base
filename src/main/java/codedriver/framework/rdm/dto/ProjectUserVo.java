/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.rdm.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.rdm.enums.ProjectUserType;
import codedriver.framework.restful.annotation.EntityField;

public class ProjectUserVo {
    @EntityField(name = "用户id", type = ApiParamType.STRING)
    private String userId;
    @EntityField(name = "用户名", type = ApiParamType.STRING)
    private String userName;
    @EntityField(name = "用户类型", type = ApiParamType.ENUM, member = ProjectUserType.class)
    private String userType;

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
}
