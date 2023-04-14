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

public class AppStatusRelVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "来源状态id", type = ApiParamType.LONG)
    private Long fromStatusId;
    @EntityField(name = "来源状态唯一标识", type = ApiParamType.STRING)
    private String fromStatusName;

    @EntityField(name = "来源状态颜色", type = ApiParamType.STRING)
    private String fromStatusColor;
    @EntityField(name = "来源状态名称", type = ApiParamType.STRING)
    private String fromStatusLabel;
    @EntityField(name = "目标状态id", type = ApiParamType.LONG)
    private Long toStatusId;
    @EntityField(name = "目标状态颜色", type = ApiParamType.STRING)
    private String toStatusColor;
    @EntityField(name = "目标状态唯一标识", type = ApiParamType.STRING)
    private String toStatusName;
    @EntityField(name = "目标状态名称", type = ApiParamType.STRING)
    private String toStatusLabel;
    @EntityField(name = "应用id", type = ApiParamType.LONG)
    private Long appId;
    @EntityField(name = "关系设置", type = ApiParamType.JSONOBJECT)
    private JSONObject config;
    @JSONField(serialize = false)
    private String configStr;


    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromStatusId() {
        return fromStatusId;
    }

    public void setFromStatusId(Long fromStatusId) {
        this.fromStatusId = fromStatusId;
    }

    public Long getToStatusId() {
        return toStatusId;
    }

    public void setToStatusId(Long toStatusId) {
        this.toStatusId = toStatusId;
    }

    public String getFromStatusName() {
        return fromStatusName;
    }

    public void setFromStatusName(String fromStatusName) {
        this.fromStatusName = fromStatusName;
    }

    public String getFromStatusLabel() {
        return fromStatusLabel;
    }

    public String getFromStatusColor() {
        return fromStatusColor;
    }

    public void setFromStatusColor(String fromStatusColor) {
        this.fromStatusColor = fromStatusColor;
    }

    public String getToStatusColor() {
        return toStatusColor;
    }

    public void setToStatusColor(String toStatusColor) {
        this.toStatusColor = toStatusColor;
    }

    public void setFromStatusLabel(String fromStatusLabel) {
        this.fromStatusLabel = fromStatusLabel;
    }

    public String getToStatusName() {
        return toStatusName;
    }

    public void setToStatusName(String toStatusName) {
        this.toStatusName = toStatusName;
    }

    public String getToStatusLabel() {
        return toStatusLabel;
    }

    public void setToStatusLabel(String toStatusLabel) {
        this.toStatusLabel = toStatusLabel;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public JSONObject getConfig() {
        if (config == null && StringUtils.isNotBlank(configStr)) {
            try {
                config = JSONObject.parseObject(configStr);
            } catch (Exception ignored) {

            }
        }
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }

    public String getConfigStr() {
        if (config != null) {
            configStr = config.toJSONString();
        }
        return configStr;
    }

    public void setConfigStr(String configStr) {
        this.configStr = configStr;
    }
}
