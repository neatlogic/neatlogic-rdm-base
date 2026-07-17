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

package neatlogic.framework.rdm.enums;

import neatlogic.framework.common.constvalue.IUserType;
import neatlogic.framework.dto.UserTypeVo;
import neatlogic.framework.util.I18n;

import java.util.HashMap;
import java.util.Map;

public enum IssueUserType implements IUserType {
    OWNER("owner", new I18n("创建人"), true),
    WORKER("worker", new I18n("当前处理人"), true),
    PROJECT_OWNER("projectowner", new I18n("项目所有人"), true),
    PROJECT_LEADER("projectleader", new I18n("项目负责人"), true),
    PROJECT_MEMBER("projectmember", new I18n("项目成员"), true),
    OPERATOR("operator", new I18n("当前操作人"), true),
    COMMENTER("commenter", new I18n("当前评论人"), false),
    REPLY_USER("replyuser", new I18n("被回复评论作者"), false);

    private final String status;
    private final I18n text;
    private final boolean isShow;

    IssueUserType(String _status, I18n _text, boolean _isShow) {
        this.status = _status;
        this.text = _text;
        this.isShow = _isShow;
    }

    public String getValue() {
        return status;
    }

    public String getText() {
        return text.toString();
    }

    public boolean getIsShow() {
        return isShow;
    }

    public static String getValue(String _status) {
        for (IssueUserType s : IssueUserType.values()) {
            if (s.getValue().equals(_status)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getText(String _status) {
        for (IssueUserType s : IssueUserType.values()) {
            if (s.getValue().equals(_status)) {
                return s.getText();
            }
        }
        return "";
    }


    @Override
    public UserTypeVo getUserType() {
        UserTypeVo vo = new UserTypeVo();
        vo.setModuleId(getModuleId());
        Map<String, String> map = new HashMap<>();
        for (IssueUserType type : IssueUserType.values()) {
            map.put(type.getValue(), type.getText());
        }
        vo.setValues(map);
        return vo;
    }

    @Override
    public String getModuleId() {
        return "rdm";
    }
}
