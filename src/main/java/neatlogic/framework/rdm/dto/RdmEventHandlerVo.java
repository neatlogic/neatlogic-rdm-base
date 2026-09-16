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
import neatlogic.framework.rdm.event.IRdmEventHandler;
import neatlogic.framework.rdm.event.RdmEventHandlerFactory;
import neatlogic.framework.rdm.event.RdmEventRegistry;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** 研发事件处理器配置，按项目和应用精确匹配，空范围配置不执行。 */
public class RdmEventHandlerVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean isAvailable = true;
    private String unavailableReason;

    /** 判断是否有已有标识，避免读取新增配置时生成标识。 */
    @JSONField(serialize = false)
    public boolean hasId() { return id != null; }
    /** 从插件声明推导每个节点的编辑器需求，包含历史子配置。 */
    public Boolean getRequiresConfigEditor() {
        IRdmEventHandler<?> plugin = RdmEventHandlerFactory.getHandler(handler);
        if (plugin != null) { return plugin.requiresConfigEditor(); }
        return false;
    }
    /** 获取当前注册插件是否仍适配此配置位置。 */
    public Boolean getIsAvailable() { return isAvailable; }
    /** 设置插件可用标记，失效配置保留以便查看及删除。 */
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    /** 获取插件不可用原因，由后端提供显示文本。 */
    public String getUnavailableReason() { return unavailableReason; }
    /** 设置插件不可用原因。 */
    public void setUnavailableReason(String unavailableReason) { this.unavailableReason = unavailableReason; }

    @EntityField(name = "配置引用标识", type = ApiParamType.STRING)
    private String uuid;
    @EntityField(name = "父配置引用标识", type = ApiParamType.STRING)
    private String parentUuid;
    @EntityField(name = "子配置列表", type = ApiParamType.JSONARRAY)
    private List<RdmEventHandlerVo> handlerList;
    @EntityField(name = "插件名称", type = ApiParamType.STRING)
    private String handlerName;
    @EntityField(name = "插件图标", type = ApiParamType.STRING)
    private String handlerIcon;
    @EntityField(name = "事件名称", type = ApiParamType.STRING)
    private String eventName;
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "父处理器id", type = ApiParamType.LONG)
    private Long parentId;
    @EntityField(name = "项目id", type = ApiParamType.LONG)
    private Long projectId;
    @EntityField(name = "应用id", type = ApiParamType.LONG)
    private Long appId;
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "事件处理器", type = ApiParamType.STRING)
    private String handler;
    @EntityField(name = "事件", type = ApiParamType.STRING)
    private String event;
    @EntityField(name = "排序", type = ApiParamType.INTEGER)
    private Integer sort = 0;
    @EntityField(name = "配置", type = ApiParamType.JSONOBJECT)
    private JSONObject config;
    @JSONField(serialize = false)
    private String configStr;
    @EntityField(name = "是否激活", type = ApiParamType.INTEGER)
    private Integer isActive = 1;

    /** 首次使用时生成标识，数据库映射或显式设置的标识保持不变。 */
    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }
    /** 设置 id。 */
    public void setId(Long id) { this.id = id; }

    /** 获取 parentId。 */
    public Long getParentId() { return parentId; }
    /** 设置 parentId。 */
    public void setParentId(Long parentId) { this.parentId = parentId; }

    /** 获取 projectId。 */
    public Long getProjectId() { return projectId; }
    /** 设置 projectId。 */
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    /** 获取 appId。 */
    public Long getAppId() { return appId; }
    /** 设置 appId。 */
    public void setAppId(Long appId) { this.appId = appId; }

    /** 获取 name。 */
    public String getName() { return name; }
    /** 设置 name。 */
    public void setName(String name) { this.name = name; }

    /** 获取 handler。 */
    public String getHandler() { return handler; }
    /** 设置 handler。 */
    public void setHandler(String handler) { this.handler = handler; }

    /** 获取 event。 */
    public String getEvent() { return event; }
    /** 设置 event。 */
    public void setEvent(String event) { this.event = event; }

    /** 获取 sort。 */
    public Integer getSort() { return sort; }
    /** 设置 sort。 */
    public void setSort(Integer sort) { this.sort = sort; }

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

    /** 获取配置引用标识。 */
    public String getUuid() { return uuid; }
    /** 设置配置引用标识。 */
    public void setUuid(String uuid) { this.uuid = uuid; }

    /** 获取父配置引用标识。 */
    public String getParentUuid() { return parentUuid; }
    /** 设置父配置引用标识。 */
    public void setParentUuid(String parentUuid) { this.parentUuid = parentUuid; }

    /** 获取子配置列表。 */
    public List<RdmEventHandlerVo> getHandlerList() { return handlerList; }
    /** 设置子配置列表。 */
    public void setHandlerList(List<RdmEventHandlerVo> handlerList) { this.handlerList = handlerList; }
    /** 添加父插件配置中提取的子配置。 */
    public void addHandler(RdmEventHandlerVo handlerVo) {
        if (handlerList == null) {
            handlerList = new ArrayList<>();
        }
        handlerList.add(handlerVo);
    }
    /** 获取注册插件的显示名称。 */
    public String getHandlerName() {
        IRdmEventHandler<?> plugin = RdmEventHandlerFactory.getHandler(handler);
        if (plugin != null) {
            return plugin.getLabel();
        }
        return handlerName;
    }
    /** 设置插件名称。 */
    public void setHandlerName(String handlerName) { this.handlerName = handlerName; }
    /** 获取注册插件的图标。 */
    public String getHandlerIcon() {
        IRdmEventHandler<?> plugin = RdmEventHandlerFactory.getHandler(handler);
        if (plugin != null) {
            return plugin.getIcon();
        }
        return handlerIcon;
    }
    /** 设置插件图标。 */
    public void setHandlerIcon(String handlerIcon) { this.handlerIcon = handlerIcon; }
    /** 获取事件显示名称。 */
    public String getEventName() {
        if (StringUtils.isNotBlank(event)) {
            return RdmEventRegistry.getLabel(event);
        }
        return eventName;
    }
    /** 设置事件显示名称。 */
    public void setEventName(String eventName) { this.eventName = eventName; }
}
