/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.rdm.auth.label;

import codedriver.framework.auth.core.AuthBase;

public class RDM_BASE extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "研发管理基础权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "使用研发管理普通功能";
    }

    @Override
    public String getAuthGroup() {
        return "rdm";
    }

    @Override
    public Integer getSort() {
        return 1;
    }
}
