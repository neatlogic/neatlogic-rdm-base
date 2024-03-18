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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

public class WebhookDataVo extends BasePageVo {

    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "term.rdm.projectid", type = ApiParamType.LONG)
    private Long projectId;
    @EntityField(name = "nmraa.getappapi.input.param.desc", type = ApiParamType.LONG)
    private Long issueAppId;
    @JSONField(serialize = false)
    private String dataStr;

    private JSONObject data;
    @EntityField(name = "nmraa.getappapi.input.param.desc", type = ApiParamType.LONG)
    private Long appId;
    @EntityField(name = "nfrd.webhookdatavo.entityfield.name", type = ApiParamType.STRING)
    private String event;

    @EntityField(name = "common.userid", type = ApiParamType.STRING)
    private String userId;

    @EntityField(name = "nfrd.webhookdatavo.entityfield.name", type = ApiParamType.STRING)
    private String userName;
    @EntityField(name = "nfrd.webhookdatavo.entityfield.name", type = ApiParamType.STRING)
    private String email;
    @EntityField(name = "term.rdm.relativetasklist", type = ApiParamType.JSONARRAY)
    private List<IssueVo> issueList;
    @EntityField(name = "term.rdm.issueid", type = ApiParamType.LONG)
    private Long issueId;
    @EntityField(name = "nmrai.saveissueapi.input.param.desc.name", type = ApiParamType.STRING)
    private String issueName;
    @EntityField(name = "term.rdm.apptype", type = ApiParamType.STRING)
    private String issueAppType;
    @EntityField(name = "term.rdm.appcolor", type = ApiParamType.STRING)
    private String issueAppColor;
    @EntityField(name = "common.createtime", type = ApiParamType.LONG)
    private Date createTime;

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public List<IssueVo> getIssueList() {
        return issueList;
    }

    public void setIssueList(List<IssueVo> issueList) {
        this.issueList = issueList;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getIssueAppId() {
        return issueAppId;
    }

    public void setIssueAppId(Long issueAppId) {
        this.issueAppId = issueAppId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataStr() {
        if (data != null) {
            dataStr = data.toJSONString();
        }
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public JSONObject getData() {
        if (data == null && StringUtils.isNotBlank(dataStr)) {
            try {
                data = JSONObject.parseObject(dataStr);
            } catch (Exception ignored) {

            }
        }
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getIssueId() {
        return issueId;
    }

    public String getIssueAppType() {
        return issueAppType;
    }

    public String getIssueAppColor() {
        if (StringUtils.isBlank(issueAppColor) && StringUtils.isNotBlank(issueAppType)) {
            issueAppColor = AppTypeManager.getColor(issueAppType);
        }
        return issueAppColor;
    }

    public void setIssueAppType(String issueAppType) {
        this.issueAppType = issueAppType;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
