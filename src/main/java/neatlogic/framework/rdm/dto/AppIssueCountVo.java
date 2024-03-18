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
