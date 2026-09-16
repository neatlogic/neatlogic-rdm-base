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

package neatlogic.framework.rdm.event;

import neatlogic.framework.asynchronization.taskmanager.AsyncTaskManager;
import neatlogic.framework.rdm.config.RdmConfig;
import neatlogic.framework.rdm.dao.mapper.RdmEventMapper;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.dto.RdmEventAuditVo;
import neatlogic.framework.rdm.dto.RdmEventHandlerVo;
import neatlogic.framework.rdm.exception.event.*;
import neatlogic.framework.transaction.core.AfterTransactionJob;
import neatlogic.framework.transaction.util.TransactionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import java.util.*;

/** 提交后异步驱动强类型事件，单次事件串行，不提供持久化队列或重试。 */
@Service
public class RdmEventManager {
    private static final Logger logger = LoggerFactory.getLogger(RdmEventManager.class);
    private static RdmEventMapper mapper;
    private static AsyncTaskManager<RdmEventJob<?>> manager;

    /** 根容器通过共享 Mapper 初始化事件执行池并查询应用归属。 */
    @Autowired
    public RdmEventManager(RdmEventMapper eventMapper) {
        mapper = eventMapper;
        manager = AsyncTaskManager.getInstance("RDM-EVENT-HANDLER", RdmConfig.RDM_EVENT_THREAD_COUNT(), RdmEventJob::execute);
    }
    /** 商业能力关闭时不影响业务；开启后制作快照并在提交后匹配配置。 */
    public static <T> void doEvent(Long projectId, Long appId, RdmEventDefinition<T> event, T object) {
        if (!RdmEventRuntimeCapabilityFactory.isAvailable()) { return; }
        if (manager == null) { throw new RdmEventEngineNotInitializedException(); }
        validateApplication(projectId, appId, event);
        validateObject(projectId, appId, event, object, null);
        String objectId = event.getAdapter().getObjectId(object);
        T snapshot = event.getAdapter().snapshot(object);
        if (snapshot == object) { throw new RdmEventSnapshotNotIndependentException(); }
        validateObject(projectId, appId, event, snapshot, objectId);
        new AfterTransactionJob<T>("RDM-EVENT-OFFER").execute(snapshot, published -> {
            if (!RdmEventRuntimeCapabilityFactory.isAvailable()) { return; }
            List<RdmEventHandlerVo> handlers = loadHandlers(projectId, appId, event);
            if (handlers != null && !handlers.isEmpty()) {
                manager.submitTask(new RdmEventJob<>(projectId, appId, handlers, event, published));
            }
        });
    }
    /** 从持久化根配置装配完整子树，保证父插件可选择执行已经保存的子配置。 */
    static List<RdmEventHandlerVo> loadHandlers(Long projectId, Long appId, RdmEventDefinition<?> event) {
        if (!RdmEventRuntimeCapabilityFactory.isAvailable()) { return Collections.emptyList(); }
        List<RdmEventHandlerVo> roots = mapper.getHandlerByEvent(event.getName(), projectId, appId);
        if (roots == null || roots.isEmpty()) { return Collections.emptyList(); }
        List<RdmEventHandlerVo> sorted = sortHandlers(roots);
        for (RdmEventHandlerVo root : sorted) {
            if (root.getParentId() != null && root.getParentId() != 0L) {
                throw new RdmEventRootConfigurationInvalidException();
            }
            loadChildren(root, projectId, appId, event, new HashSet<>());
            validateConfiguration(root, event, projectId, appId);
        }
        return sorted;
    }
    /** 按父标识读取所有子节点，错误归属不能被应用过滤条件静默隐藏。 */
    private static void loadChildren(RdmEventHandlerVo parent, Long projectId, Long appId,
                                     RdmEventDefinition<?> event, Set<Long> path) {
        validateHandler(parent, event, projectId, appId);
        if (!path.add(parent.getId())) { throw new RdmEventConfigurationCycleException(); }
        try {
            List<RdmEventHandlerVo> children = mapper.getHandlerByParentId(parent.getId());
            if (children == null) { children = Collections.emptyList(); }
            children = sortHandlers(children);
            for (RdmEventHandlerVo child : children) {
                validateHandler(child, event, projectId, appId);
                validateChild(parent, child);
                loadChildren(child, projectId, appId, event, path);
            }
            parent.setHandlerList(children);
        } finally { path.remove(parent.getId()); }
    }
    /** 配置保存与执行统一验证应用存在性、项目归属及支持类型。 */
    public static void validateApplication(Long projectId, Long appId, RdmEventDefinition<?> event) {
        getApplicationType(projectId, appId, event);
    }
    /** 返回通过数据库归属验证的应用类型，供配置和运行时共享适配规则。 */
    public static String getApplicationType(Long projectId, Long appId, RdmEventDefinition<?> event) {
        if (projectId == null || appId == null || event == null || RdmEventRegistry.get(event.getName()) != event) {
            throw new RdmEventApplicationArgumentInvalidException();
        }
        if (mapper == null) { throw new RdmEventEngineNotInitializedException(); }
        AppVo app = mapper.getEventAppById(appId);
        if (app == null || !Objects.equals(projectId, app.getProjectId())
                || !Objects.equals(appId, app.getId()) || !event.getAppTypes().contains(app.getType())) {
            throw new RdmEventApplicationNotSupportedException();
        }
        return app.getType();
    }
    /** 校验配置树的范围、类型与循环，供配置保存和实际执行共用。 */
    public static void validateConfiguration(RdmEventHandlerVo handler, RdmEventDefinition<?> event,
                                             Long projectId, Long appId) {
        validateApplication(projectId, appId, event);
        validateTree(handler, event, projectId, appId, null, new HashSet<>());
    }
    /** 逐层验证已提取的子配置，路径集合允许不同分支复用同一合法配置。 */
    private static void validateTree(RdmEventHandlerVo handler, RdmEventDefinition<?> event,
                                      Long projectId, Long appId, RdmEventHandlerVo parent, Set<Long> path) {
        validateHandler(handler, event, projectId, appId);
        if (!path.add(handler.getId())) { throw new RdmEventConfigurationCycleException(); }
        if (parent != null) { validateChild(parent, handler); }
        try {
            if (handler.getHandlerList() != null) {
                for (RdmEventHandlerVo child : handler.getHandlerList()) {
                    validateTree(child, event, projectId, appId, handler, path);
                }
            }
        } finally { path.remove(handler.getId()); }
    }
    /** 检查单个配置的精确归属和插件类型，不允许历史空范围通配。 */
    static void validateHandler(RdmEventHandlerVo handler, RdmEventDefinition<?> event, Long projectId, Long appId) {
        if (handler == null || !Objects.equals(handler.getProjectId(), projectId)
                || !Objects.equals(handler.getAppId(), appId) || !Objects.equals(handler.getEvent(), event.getName())) {
            throw new RdmEventConfigurationScopeMismatchException();
        }
        RdmEventHandlerFactory.validate(handler, event);
    }
    /** 父插件可在外部副作用前预校验子配置，只允许所属父配置及声明的父插件触发。 */
    public static void validateChild(RdmEventHandlerVo parent, RdmEventHandlerVo child) {
        IRdmEventHandler<?> plugin = RdmEventHandlerFactory.getHandler(child.getHandler());
        if (!Objects.equals(child.getParentId(), parent.getId()) || plugin == null
                || !plugin.supportParentHandler().contains(parent.getHandler())) {
            throw new RdmEventChildHandlerMismatchException();
        }
    }
    /** 校验对象类型、范围与不可变身份，分别报告非法标识、类型超长和身份变更。 */
    static <T> void validateObject(Long projectId, Long appId, RdmEventDefinition<T> event, T object, String expectedId) {
        if (object == null || !event.getObjectClass().equals(object.getClass())) {
            throw new RdmEventObjectClassMismatchException();
        }
        String objectId = event.getAdapter().getObjectId(object);
        if (objectId == null || objectId.trim().isEmpty() || objectId.length() > 255) {
            throw new RdmEventObjectIdInvalidException();
        }
        if (event.getObjectType().length() > 100) {
            throw new RdmEventObjectTypeTooLongException();
        }
        if (expectedId != null && !expectedId.equals(objectId)) {
            throw new RdmEventObjectIdChangedException();
        }
        event.getAdapter().validateScope(projectId, appId, object);
    }
    /** 保存强类型定义及对象，泛型捕获使队列不需要业务类型转换。 */
    private static class RdmEventJob<T> {
        private final Long projectId;
        private final Long appId;
        private final List<RdmEventHandlerVo> handlers;
        private final RdmEventDefinition<T> event;
        private final T object;
        /** 保存单次发布的数据。 */
        private RdmEventJob(Long projectId, Long appId, List<RdmEventHandlerVo> handlers, RdmEventDefinition<T> event, T object) {
            this.projectId = projectId;
            this.appId = appId;
            this.handlers = handlers;
            this.event = event;
            this.object = object;
        }
        /** 失败只终止当前任务，避免影响池中其他事件。 */
        private void execute() {
            try { executeHandlers(projectId, appId, handlers, event, object); }
            catch (Exception e) { logger.error("研发事件执行失败：" + event.getName(), e); }
        }
    }
    /** 先检查所有根配置，再按排序及标识串行触发，异常立即终止。 */
    static <T> T executeHandlers(List<RdmEventHandlerVo> handlers, RdmEventDefinition<T> event, T object) {
        if (handlers == null || handlers.isEmpty()) { return object; }
        return executeHandlers(handlers.get(0).getProjectId(), handlers.get(0).getAppId(), handlers, event, object);
    }
    /** 使用发布时保留的可信范围校验配置，避免从历史配置推导执行范围。 */
    static <T> T executeHandlers(Long projectId, Long appId, List<RdmEventHandlerVo> handlers,
                                 RdmEventDefinition<T> event, T object) {
        if (handlers == null || handlers.isEmpty()) { return object; }
        List<RdmEventHandlerVo> sorted = sortHandlers(handlers);
        if (!RdmEventRuntimeCapabilityFactory.isAvailable()) {
            recordCapabilityStopped(projectId, appId, sorted.get(0), event, object);
            return object;
        }
        for (RdmEventHandlerVo handler : sorted) {
            if (handler.getParentId() != null && handler.getParentId() != 0L) {
                throw new RdmEventRootConfigurationInvalidException();
            }
            validateConfiguration(handler, event, projectId, appId);
        }
        validateObject(projectId, appId, event, object, null);
        for (RdmEventHandlerVo handler : sorted) {
            if (!RdmEventRuntimeCapabilityFactory.isAvailable()) {
                recordCapabilityStopped(projectId, appId, handler, event, object);
                return object;
            }
            object = RdmEventHandlerFactory.trigger(handler, event, object, null);
        }
        return object;
    }
    /** 排队或执行链失去能力时，仅记录首个停止节点，不触发插件或读取业务状态。 */
    private static <T> void recordCapabilityStopped(Long projectId, Long appId, RdmEventHandlerVo handler,
                                                     RdmEventDefinition<T> event, T object) {
        RdmEventAuditVo audit = new RdmEventAuditVo();
        audit.setProjectId(projectId); audit.setAppId(appId);
        audit.setObjectType(event.getObjectType()); audit.setObjectId(event.getAdapter().getObjectId(object));
        audit.setEvent(event.getName()); audit.setHandler(handler.getHandler());
        audit.setHandlerName(handler.getName()); audit.setEventHandlerId(handler.getId());
        audit.setConfig(handler.getConfig()); audit.setStartTime(new Date()); audit.setEndTime(new Date());
        audit.setStatus(RdmEventStatus.FAILED.getValue());
        audit.setError(new RdmEventCapabilityUnavailableException().getMessage());
        TransactionStatus tx = TransactionUtil.openNewTx();
        try {
            mapper.insertAudit(audit);
            TransactionUtil.commitTx(tx);
        } catch (Exception e) {
            if (!tx.isCompleted()) { TransactionUtil.rollbackTx(tx); }
            throw e;
        }
    }
    /** 复制并稳定排序，同序号配置按已有标识排序。 */
    static List<RdmEventHandlerVo> sortHandlers(List<RdmEventHandlerVo> handlers) {
        List<RdmEventHandlerVo> sorted = new ArrayList<>(handlers);
        sorted.sort(Comparator.comparing(RdmEventHandlerVo::getSort, Comparator.nullsFirst(Integer::compareTo))
                .thenComparing(RdmEventHandlerVo::getId));
        return sorted;
    }
}
