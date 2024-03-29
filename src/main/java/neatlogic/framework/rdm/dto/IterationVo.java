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
import neatlogic.framework.rdm.enums.AppType;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import neatlogic.framework.util.TimeUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IterationVo extends BasePageVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "项目id", type = ApiParamType.LONG)
    private Long projectId;
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "说明", type = ApiParamType.STRING)
    private String description;
    @EntityField(name = "任务总数", type = ApiParamType.INTEGER)
    private int issueCount;
    @EntityField(name = "已完成任务总数", type = ApiParamType.INTEGER)
    private int doneIssueCount;
    @EntityField(name = "是否开启", type = ApiParamType.INTEGER)
    private Integer isOpen;
    @EntityField(name = "应用配型", type = ApiParamType.STRING)
    private String appType = AppType.ITERATION.getName();
    @EntityField(name = "应用颜色", type = ApiParamType.STRING)
    private String appColor = AppType.ITERATION.getColor();
    @EntityField(name = "时间范围", type = ApiParamType.JSONARRAY)
    private List<String> dateRange;

    @JSONField(serialize = false)//搜索专用
    private List<String> startDateRange;
    @JSONField(serialize = false)//搜索专用
    private List<String> endDateRange;

    @EntityField(name = "开始日期", type = ApiParamType.LONG)
    private Date startDate;

    @EntityField(name = "结束日期", type = ApiParamType.LONG)
    private Date endDate;

    public int getIssueCount() {
        return issueCount;
    }

    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public void setIssueCount(int issueCount) {
        this.issueCount = issueCount;
    }

    public int getDoneIssueCount() {
        return doneIssueCount;
    }

    public void setDoneIssueCount(int doneIssueCount) {
        this.doneIssueCount = doneIssueCount;
    }

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public List<String> getDateRange() {
        if (dateRange == null && startDate != null && endDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(TimeUtil.YYYY_MM_DD);
            dateRange = new ArrayList<String>() {{
                this.add(sdf.format(startDate));
                this.add(sdf.format(endDate));
            }};
        }
        return dateRange;
    }

    public void setDateRange(List<String> dateRange) {
        this.dateRange = dateRange;
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

    public Date getStartDate() {
        if (startDate == null && CollectionUtils.isNotEmpty(dateRange)) {
            SimpleDateFormat sdf = new SimpleDateFormat(TimeUtil.YYYY_MM_DD);
            try {
                startDate = sdf.parse(dateRange.get(0));
            } catch (Exception ignored) {

            }
        }
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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

    public Date getEndDate() {
        if (endDate == null && CollectionUtils.isNotEmpty(dateRange) && dateRange.size() >= 2) {
            SimpleDateFormat sdf = new SimpleDateFormat(TimeUtil.YYYY_MM_DD);
            try {
                endDate = sdf.parse(dateRange.get(1));
            } catch (Exception ignored) {

            }
        }
        return endDate;
    }

    public String getAppType() {
        return appType;
    }


    public String getAppColor() {
        return appColor;
    }


    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public static void main(String[] arv) {

    }
}
