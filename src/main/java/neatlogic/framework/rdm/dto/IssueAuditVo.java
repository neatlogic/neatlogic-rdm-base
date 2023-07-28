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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.asynchronization.threadlocal.InputFromContext;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.constvalue.InputFrom;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

public class IssueAuditVo extends BasePageVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "term.rdm.issueid", type = ApiParamType.LONG)
    private Long issueId;
    @EntityField(name = "common.attrname", type = ApiParamType.STRING)
    private String attrName;
    @EntityField(name = "nmcaa.getattrapi.input.param.desc.id", type = ApiParamType.LONG)
    private Long attrId;
    @EntityField(name = "common.oldvalue", type = ApiParamType.STRING)
    private JSONArray oldValue;
    @EntityField(name = "common.newvalue", type = ApiParamType.STRING)
    private JSONArray newValue;
    @EntityField(name = "common.inputfrom", type = ApiParamType.ENUM, member = InputFrom.class)
    private String inputFrom;
    @EntityField(name = "common.actiontime", type = ApiParamType.LONG)
    private Date inputTime;
    @EntityField(name = "common.actionuser", type = ApiParamType.STRING)
    private String inputUser;
    @EntityField(name = "term.rdm.appattr", type = ApiParamType.JSONOBJECT)
    private AppAttrVo appAttr;
    @EntityField(name = "common.inputfromname", type = ApiParamType.STRING)
    private String inputFromName;
    @JSONField(serialize = false)
    private String newValueStr;
    @JSONField(serialize = false)
    private String oldValueStr;

    public IssueAuditVo() {

    }

    public IssueAuditVo(Long issueId, String attrName, Object oldValue, Object newValue) {
        this.issueId = issueId;
        this.attrName = attrName;
        this.inputUser = UserContext.get().getUserUuid();
        if (oldValue != null) {
            if (!(oldValue instanceof List)) {
                this.oldValue = new JSONArray() {{
                    this.add(oldValue);
                }};
            } else {
                this.oldValue = JSONArray.parseArray(JSONArray.toJSONString(oldValue));
            }
        }
        if (newValue != null) {
            if (!(newValue instanceof List)) {
                this.newValue = new JSONArray() {{
                    this.add(newValue);
                }};
            } else {
                this.newValue = JSONArray.parseArray(JSONArray.toJSONString(newValue));
            }
        }
        if (InputFromContext.get() != null) {
            this.inputFrom = InputFromContext.get().getInputFrom();
        }
    }

    public String getNewValueStr() {
        if (newValue != null) {
            return newValue.toJSONString();
        }
        return null;
    }


    public String getOldValueStr() {
        if (oldValue != null) {
            return oldValue.toJSONString();
        }
        return null;
    }

    public AppAttrVo getAppAttr() {
        return appAttr;
    }

    public void setAppAttr(AppAttrVo appAttr) {
        this.appAttr = appAttr;
    }

    public void setNewValueStr(String newValueStr) {
        this.newValueStr = newValueStr;
    }

    public void setOldValueStr(String oldValueStr) {
        this.oldValueStr = oldValueStr;
    }

    public IssueAuditVo(Long issueId, Long attrId, Object oldValue, Object newValue) {
        this.issueId = issueId;
        this.attrId = attrId;
        this.inputUser = UserContext.get().getUserUuid();
        if (oldValue != null) {
            if (oldValue instanceof JSONArray) {
                this.oldValue = (JSONArray) oldValue;
            } else if (oldValue instanceof List) {
                try {
                    this.oldValue = JSONArray.parseArray(JSONArray.toJSONString(oldValue));
                } catch (Exception ex) {
                    this.oldValue = new JSONArray() {{
                        this.add(oldValue);
                    }};
                }
            } else {
                this.oldValue = new JSONArray() {{
                    this.add(oldValue);
                }};
            }
        }
        if (newValue != null) {
            if (newValue instanceof JSONArray) {
                this.newValue = (JSONArray) newValue;
            } else if (newValue instanceof List) {
                try {
                    this.newValue = JSONArray.parseArray(JSONArray.toJSONString(newValue));
                } catch (Exception ex) {
                    this.newValue = new JSONArray() {{
                        this.add(newValue);
                    }};
                }
            } else {
                this.newValue = new JSONArray() {{
                    this.add(newValue);
                }};
            }
        }
        if (InputFromContext.get() != null) {
            this.inputFrom = InputFromContext.get().getInputFrom();
        }
    }


    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public Long getIssueId() {
        return issueId;
    }

    public String getInputUser() {
        return inputUser;
    }

    public void setInputUser(String inputUser) {
        this.inputUser = inputUser;
    }

    public String getInputFromName() {
        if (StringUtils.isNotBlank(inputFrom)) {
            return InputFrom.getText(inputFrom);
        }
        return null;
    }


    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public Long getAttrId() {
        return attrId;
    }

    public void setAttrId(Long attrId) {
        this.attrId = attrId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public JSONArray getOldValue() {
        if (oldValue == null && StringUtils.isNotBlank(oldValueStr)) {
            try {
                oldValue = JSONArray.parseArray(oldValueStr);
            } catch (Exception ignored) {

            }
        }
        return oldValue;
    }

    public void setOldValue(JSONArray oldValue) {
        this.oldValue = oldValue;
    }

    public JSONArray getNewValue() {
        if (newValue == null && StringUtils.isNotBlank(newValueStr)) {
            try {
                newValue = JSONArray.parseArray(newValueStr);
            } catch (Exception ignored) {

            }
        }
        return newValue;
    }

    public void setNewValue(JSONArray newValue) {
        this.newValue = newValue;
    }

    public String getInputFrom() {
        return inputFrom;
    }

    public void setInputFrom(String inputFrom) {
        this.inputFrom = inputFrom;
    }

    public Date getInputTime() {
        return inputTime;
    }

    public void setInputTime(Date inputTime) {
        this.inputTime = inputTime;
    }
}
