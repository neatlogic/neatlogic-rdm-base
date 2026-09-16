/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the Sustainable Use License (SUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.rdm.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;
import org.apache.commons.lang3.StringUtils;

/** 研发事件插件全局配置，统一控制插件启停和公共参数。 */
public class RdmEventPluginVo {
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String label;
    @EntityField(name = "图标", type = ApiParamType.STRING)
    private String icon;
    @EntityField(name = "描述", type = ApiParamType.STRING)
    private String description;
    @EntityField(name = "插件唯一标识", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "配置", type = ApiParamType.JSONOBJECT)
    private JSONObject config;
    @JSONField(serialize = false)
    private String configStr;
    @EntityField(name = "是否激活", type = ApiParamType.INTEGER)
    private Integer isActive = 1;

    /** 获取 name。 */
    public String getName() { return name; }
    /** 设置 name。 */
    public void setName(String name) { this.name = name; }

    /** 按需解析数据库中的配置字符串。 */
    public JSONObject getConfig() {
        if (config == null && StringUtils.isNotBlank(configStr)) {
            try {
                config = JSONObject.parseObject(configStr);
            } catch (Exception ignored) {
                // 与现有配置对象保持一致，历史非法配置返回 null。
            }
        }
        return config;
    }

    /** 设置配置对象，同时清除旧字符串，避免空配置恢复成历史值。 */
    public void setConfig(JSONObject config) {
        this.config = config;
        this.configStr = null;
    }

    /** 将当前配置对象序列化为数据库字段值。 */
    public String getConfigStr() {
        if (config != null) {
            configStr = config.toJSONString();
        }
        return configStr;
    }

    /** 设置数据库配置字符串，同时清除已解析的旧对象。 */
    public void setConfigStr(String configStr) {
        this.configStr = configStr;
        this.config = null;
    }

    /** 获取 isActive。 */
    public Integer getIsActive() { return isActive; }
    /** 设置 isActive。 */
    public void setIsActive(Integer isActive) { this.isActive = isActive; }

    /** 获取名称。 */
    public String getLabel() { return label; }
    /** 设置名称。 */
    public void setLabel(String label) { this.label = label; }

    /** 获取图标。 */
    public String getIcon() { return icon; }
    /** 设置图标。 */
    public void setIcon(String icon) { this.icon = icon; }

    /** 获取描述。 */
    public String getDescription() { return description; }
    /** 设置描述。 */
    public void setDescription(String description) { this.description = description; }
    /** 创建空插件配置，供数据库映射使用。 */
    public RdmEventPluginVo() { }
    /** 创建带展示元数据的插件配置。 */
    public RdmEventPluginVo(String name, String label, String icon, String description) {
        this.name = name;
        this.label = label;
        this.icon = icon;
        this.description = description;
    }
}
