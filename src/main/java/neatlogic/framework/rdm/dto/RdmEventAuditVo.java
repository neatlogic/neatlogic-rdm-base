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
import neatlogic.framework.common.config.Config;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.event.IRdmEventHandler;
import neatlogic.framework.rdm.event.RdmEventHandlerFactory;
import neatlogic.framework.rdm.event.RdmEventStatus;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/** 研发事件执行审计，通过父审计标识关联级联执行。 */
public class RdmEventAuditVo {
    @EntityField(name = "状态名称", type = ApiParamType.STRING)
    private String statusName;
    @EntityField(name = "插件图标", type = ApiParamType.STRING)
    private String handlerIcon;
    @EntityField(name = "执行耗时", type = ApiParamType.LONG)
    private long timeCost;
    @EntityField(name = "执行服务节点", type = ApiParamType.INTEGER)
    private Integer serverId;
    @EntityField(name = "子审计列表", type = ApiParamType.JSONARRAY)
    private List<RdmEventAuditVo> childAuditList;
    @EntityField(name = "是否有子审计", type = ApiParamType.INTEGER)
    private Integer hasChild;
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "父审计id", type = ApiParamType.LONG)
    private Long parentId;
    @EntityField(name = "对象类型", type = ApiParamType.STRING)
    private String objectType;
    @EntityField(name = "对象标识", type = ApiParamType.STRING)
    private String objectId;
    @EntityField(name = "项目id", type = ApiParamType.LONG)
    private Long projectId;
    @EntityField(name = "应用id", type = ApiParamType.LONG)
    private Long appId;
    @EntityField(name = "事件", type = ApiParamType.STRING)
    private String event;
    @EntityField(name = "事件处理器", type = ApiParamType.STRING)
    private String handler;
    @EntityField(name = "事件处理器名称", type = ApiParamType.STRING)
    private String handlerName;
    @EntityField(name = "事件处理器配置id", type = ApiParamType.LONG)
    private Long eventHandlerId;
    @EntityField(name = "开始时间", type = ApiParamType.LONG)
    private Date startTime;
    @EntityField(name = "结束时间", type = ApiParamType.LONG)
    private Date endTime;
    @EntityField(name = "状态", type = ApiParamType.STRING)
    private String status;
    @EntityField(name = "异常", type = ApiParamType.STRING)
    private String error;
    @EntityField(name = "配置", type = ApiParamType.JSONOBJECT)
    private JSONObject config;
    @JSONField(serialize = false)
    private String configStr;
    @EntityField(name = "结果", type = ApiParamType.JSONOBJECT)
    private JSONObject result;
    @JSONField(serialize = false)
    private String resultStr;

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

    /** 获取业务对象类型。 */
    public String getObjectType() { return objectType; }
    /** 设置业务对象类型。 */
    public void setObjectType(String objectType) { this.objectType = objectType; }

    /** 获取业务对象字符串标识。 */
    public String getObjectId() { return objectId; }
    /** 设置业务对象字符串标识。 */
    public void setObjectId(String objectId) { this.objectId = objectId; }

    /** 获取 projectId。 */
    public Long getProjectId() { return projectId; }
    /** 设置 projectId。 */
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    /** 获取 appId。 */
    public Long getAppId() { return appId; }
    /** 设置 appId。 */
    public void setAppId(Long appId) { this.appId = appId; }

    /** 获取 event。 */
    public String getEvent() { return event; }
    /** 设置 event。 */
    public void setEvent(String event) { this.event = event; }

    /** 获取 handler。 */
    public String getHandler() { return handler; }
    /** 设置 handler。 */
    public void setHandler(String handler) { this.handler = handler; }

    /** 获取 handlerName。 */
    public String getHandlerName() { return handlerName; }
    /** 设置 handlerName。 */
    public void setHandlerName(String handlerName) { this.handlerName = handlerName; }

    /** 获取 eventHandlerId。 */
    public Long getEventHandlerId() { return eventHandlerId; }
    /** 设置 eventHandlerId。 */
    public void setEventHandlerId(Long eventHandlerId) { this.eventHandlerId = eventHandlerId; }

    /** 获取 startTime。 */
    public Date getStartTime() { return startTime; }
    /** 设置 startTime。 */
    public void setStartTime(Date startTime) { this.startTime = startTime; }

    /** 获取 endTime。 */
    public Date getEndTime() { return endTime; }
    /** 设置 endTime。 */
    public void setEndTime(Date endTime) { this.endTime = endTime; }

    /** 获取 status。 */
    public String getStatus() { return status; }
    /** 设置 status。 */
    public void setStatus(String status) { this.status = status; }

    /** 获取 error。 */
    public String getError() { return error; }
    /** 设置 error。 */
    public void setError(String error) { this.error = error; }

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

    /** 按需解析数据库中的结果字符串。 */
    public JSONObject getResult() {
        if (result == null && StringUtils.isNotBlank(resultStr)) {
            try {
                result = JSONObject.parseObject(resultStr);
            } catch (Exception ignored) {
                // 与现有结果对象保持一致，历史非法结果返回 null。
            }
        }
        return result;
    }

    /** 设置结果对象，同时清除旧字符串，避免空结果恢复成历史值。 */
    public void setResult(JSONObject result) {
        this.result = result;
        this.resultStr = null;
    }

    /** 将当前结果对象序列化为数据库字段值。 */
    public String getResultStr() {
        if (result != null) {
            resultStr = result.toJSONString();
        }
        return resultStr;
    }

    /** 设置数据库结果字符串，同时清除已解析的旧对象。 */
    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
        this.result = null;
    }

    /** 获取子审计列表。 */
    public List<RdmEventAuditVo> getChildAuditList() { return childAuditList; }
    /** 设置子审计列表。 */
    public void setChildAuditList(List<RdmEventAuditVo> childAuditList) { this.childAuditList = childAuditList; }

    /** 获取是否有子审计。 */
    public Integer getHasChild() { return hasChild; }
    /** 设置是否有子审计。 */
    public void setHasChild(Integer hasChild) { this.hasChild = hasChild; }
    /** 根据状态值解析显示名称。 */
    public String getStatusName() { return RdmEventStatus.getText(status); }
    /** 获取插件图标。 */
    public String getHandlerIcon() {
        IRdmEventHandler<?> plugin = RdmEventHandlerFactory.getHandler(handler);
        if (plugin != null) {
            return plugin.getIcon();
        }
        return handlerIcon;
    }
    /** 获取开始和结束时间之间的毫秒数。 */
    public long getTimeCost() {
        if (startTime != null && endTime != null) {
            return endTime.getTime() - startTime.getTime();
        }
        return timeCost;
    }
    /** 新审计默认使用当前节点，读取时保留数据库节点。 */
    public Integer getServerId() {
        if (serverId == null) {
            return Config.SCHEDULE_SERVER_ID;
        }
        return serverId;
    }
    /** 设置执行节点。 */
    public void setServerId(Integer serverId) { this.serverId = serverId; }
}
