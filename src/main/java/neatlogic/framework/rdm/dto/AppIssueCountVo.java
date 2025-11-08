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

package neatlogic.framework.rdm.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.restful.annotation.EntityField;
import org.apache.commons.lang3.StringUtils;

public class AppIssueCountVo {
    @EntityField(name = "数量", type = ApiParamType.INTEGER)//作为搜索条件使用
    private int count;
    @EntityField(name = "应用类型", type = ApiParamType.STRING)
    private String appType;

    @EntityField(name = "应用类型名称", type = ApiParamType.STRING)
    private String appTypeName;
    @EntityField(name = "应用类型颜色", type = ApiParamType.STRING)
    private String appTypeColor;

    @EntityField(name = "应用类型名称", type = ApiParamType.STRING)
    private String appTypeLabel;
    @EntityField(name = "是否结束", type = ApiParamType.INTEGER)
    private int isEnd;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppTypeName() {
        if (StringUtils.isBlank(appTypeName) && StringUtils.isNotBlank(this.appType)) {
            appTypeName = AppTypeManager.getLabel(this.appType);
        }
        return appTypeName;
    }


    public String getAppTypeColor() {
        if (StringUtils.isBlank(appTypeColor) && StringUtils.isNotBlank(this.appType)) {
            appTypeColor = AppTypeManager.getColor(this.appType);
        }
        return appTypeColor;
    }

    public int getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(int isEnd) {
        this.isEnd = isEnd;
    }

    public String getAppTypeLabel() {
        if (StringUtils.isBlank(appTypeLabel) && StringUtils.isNotBlank(this.appType)) {
            appTypeLabel = AppTypeManager.getLabel(this.appType);
        }
        return appTypeLabel;
    }

    public void setAppTypeLabel(String appTypeLabel) {
        this.appTypeLabel = appTypeLabel;
    }
}
