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
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.Md5Util;
import neatlogic.framework.util.SnowflakeUtil;
import neatlogic.framework.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;

public class AppStatusRelVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "来源状态id", type = ApiParamType.LONG)
    private Long fromStatusId;

    @EntityField(name = "来源状态uuid", type = ApiParamType.STRING)
    private String fromStatusUuid;

    @EntityField(name = "目标状态uuid", type = ApiParamType.STRING)
    private String toStatusUuid;
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

    @EntityField(name = "随机生成的uuid", type = ApiParamType.STRING)
    private String uuid;

    public String getUuid() {
        if (StringUtils.isBlank(uuid)) {
            if (id != null) {
                uuid = Md5Util.encryptMD5(id.toString());
            } else {
                uuid = UuidUtil.randomUuid();
            }
        }
        return uuid;
    }

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public String getFromStatusUuid() {
        //通过来源id得到来源uuid
        if (StringUtils.isBlank(fromStatusUuid) && fromStatusId != null) {
            fromStatusUuid = Md5Util.encryptMD5(fromStatusId.toString());
        }
        return fromStatusUuid;
    }

    public void setFromStatusUuid(String fromStatusUuid) {
        this.fromStatusUuid = fromStatusUuid;
    }

    public String getToStatusUuid() {
        //通过目标id得到目标uuid
        if (StringUtils.isBlank(toStatusUuid) && toStatusId != null) {
            toStatusUuid = Md5Util.encryptMD5(toStatusId.toString());
        }
        return toStatusUuid;
    }

    public void setToStatusUuid(String toStatusUuid) {
        this.toStatusUuid = toStatusUuid;
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
