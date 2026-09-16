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

package neatlogic.framework.rdm.dao.mapper;

import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.dto.RdmEventAuditVo;
import neatlogic.framework.rdm.dto.RdmEventHandlerVo;
import neatlogic.framework.rdm.dto.RdmEventPluginVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 研发事件配置与审计持久化接口。 */
public interface RdmEventMapper {
    /** 读取事件范围校验所需的应用归属和类型，不依赖业务模块解析器。 */
    AppVo getEventAppById(Long appId);
    /** 按项目、应用和事件精确查询根配置，空范围不匹配。 */
    List<RdmEventHandlerVo> getHandlerByEvent(@Param("event") String event, @Param("projectId") Long projectId, @Param("appId") Long appId);
    /** 查询指定父配置的子配置。 */
    List<RdmEventHandlerVo> getHandlerByParentId(Long parentId);
    /** 根据标识查询处理器配置。 */
    RdmEventHandlerVo getHandlerById(Long id);
    /** 按配置引用标识查询处理器。 */
    RdmEventHandlerVo getHandlerByUuid(String uuid);
    /** 查询父审计下的子执行记录。 */
    List<RdmEventAuditVo> getChildAuditByParentId(Long parentId);
    /** 根据标识查询审计明细。 */
    RdmEventAuditVo getAuditById(Long id);
    /** 分页查询指定项目、应用及业务对象的根执行审计。 */
    List<RdmEventAuditVo> searchAudit(@Param("projectId") Long projectId, @Param("appId") Long appId, @Param("objectType") String objectType, @Param("objectId") String objectId, @Param("startNum") int startNum, @Param("pageSize") int pageSize);
    /** 查询指定项目、应用及业务对象的根审计数量。 */
    int searchAuditCount(@Param("projectId") Long projectId, @Param("appId") Long appId, @Param("objectType") String objectType, @Param("objectId") String objectId);
    /** 查询插件全局配置。 */
    RdmEventPluginVo getPluginByName(String name);
    /** 查询应用内全部配置，供服务组装树和按事件统计根节点。 */
    List<RdmEventHandlerVo> getHandlerListByApp(@Param("projectId") Long projectId, @Param("appId") Long appId);
    /** 锁定真实应用行，串行化同一应用的配置变更。 */
    Long lockEventApp(@Param("projectId") Long projectId, @Param("appId") Long appId);
    /** 删除已校验归属的单个配置，子树由服务明确遍历。 */
    void deleteHandlerById(Long id);
    /** 更新已校验归属的同级配置排序。 */
    void updateHandlerSort(@Param("id") Long id, @Param("sort") Integer sort);
    /** 新配置只允许插入，唯一标识冲突不能接管其他应用配置。 */
    void insertHandler(RdmEventHandlerVo handlerVo);
    /** 只更新当前项目应用中的已有节点，保持唯一标识归属不变。 */
    void updateHandler(RdmEventHandlerVo handlerVo);
    /** 保存事件配置。 */
    void saveHandler(RdmEventHandlerVo handlerVo);
    /** 保存插件全局配置。 */
    void savePlugin(RdmEventPluginVo pluginVo);
    /** 记录开始执行。 */
    void insertAudit(RdmEventAuditVo auditVo);
    /** 更新最终执行状态。 */
    void updateAudit(RdmEventAuditVo auditVo);
}
