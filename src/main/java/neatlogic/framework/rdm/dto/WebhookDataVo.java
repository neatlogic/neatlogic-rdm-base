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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

public class WebhookDataVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
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


    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
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
}
