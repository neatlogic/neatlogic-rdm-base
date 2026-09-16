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

import neatlogic.framework.rdm.dao.mapper.RdmEventMapper;
import neatlogic.framework.rdm.dto.RdmEventAuditVo;
import neatlogic.framework.rdm.dto.RdmEventHandlerVo;
import neatlogic.framework.rdm.dto.RdmEventPluginVo;
import neatlogic.framework.rdm.dto.RdmEventStatusVo;
import neatlogic.framework.rdm.exception.event.RdmEventHandlerTriggerException;
import neatlogic.framework.transaction.util.TransactionUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Objects;

/** 统一强类型插件独立事务、每步重载和独立持久化的父子审计。 */
public abstract class RdmEventHandlerBase<T> implements IRdmEventHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger(RdmEventHandlerBase.class);
    private static final ThreadLocal<Deque<RdmEventAuditVo>> AUDITS = ThreadLocal.withInitial(ArrayDeque::new);
    private static final ThreadLocal<Deque<RdmEventHandlerVo>> CONFIGS = ThreadLocal.withInitial(ArrayDeque::new);
    private final ThreadLocal<RdmEventDefinition<T>> currentEvent = new ThreadLocal<>();
    @Resource
    protected RdmEventMapper rdmEventMapper;

    /** 执行根插件，子插件应使用受约束的子执行入口。 */
    @Override
    public final T trigger(RdmEventHandlerVo handlerVo, RdmEventDefinition<T> event, T object) {
        return trigger(handlerVo, event, object, null);
    }
    /** 验证类型和执行路径后，创建独立审计并启动插件事务。 */
    @Override
    public final T trigger(RdmEventHandlerVo handlerVo, RdmEventDefinition<T> event, T object, Long parentAuditId) {
        try { return triggerChecked(handlerVo, event, object, parentAuditId); }
        catch (Exception e) {
            // 配置校验失败同样不能被父插件吞掉后继续提交。
            for (RdmEventAuditVo activeAudit : AUDITS.get()) {
                activeAudit.setStatus(RdmEventStatus.FAILED.getValue());
            }
            if (AUDITS.get().isEmpty()) { AUDITS.remove(); CONFIGS.remove(); }
            throw e;
        }
    }
    /** 完成前置验证后执行并记录单个插件。 */
    private T triggerChecked(RdmEventHandlerVo handlerVo, RdmEventDefinition<T> event, T object, Long parentAuditId) {
        RdmEventRuntimeCapabilityFactory.requireAvailable();
        RdmEventManager.validateConfiguration(handlerVo, event, handlerVo.getProjectId(), handlerVo.getAppId());
        RdmEventManager.validateObject(handlerVo.getProjectId(), handlerVo.getAppId(), event, object, null);
        if (!getObjectClass().equals(event.getObjectClass()) || !getName().equals(handlerVo.getHandler())) {
            throw new IllegalArgumentException("插件实例与事件配置不匹配");
        }
        validateExecutionPath(handlerVo, parentAuditId);
        if (!AUDITS.get().isEmpty()) {
            RdmEventManager.validateObject(handlerVo.getProjectId(), handlerVo.getAppId(), event, object,
                    AUDITS.get().peek().getObjectId());
        }
        RdmEventAuditVo audit = createAudit(handlerVo, event, object, parentAuditId);
        writeAudit(audit, true);
        RdmEventDefinition<T> previousEvent = currentEvent.get();
        currentEvent.set(event);
        CONFIGS.get().push(handlerVo);
        AUDITS.get().push(audit);
        try {
            RdmEventPluginVo plugin = rdmEventMapper.getPluginByName(handlerVo.getHandler());
            if (Objects.equals(handlerVo.getIsActive(), 0) || (plugin != null && Objects.equals(plugin.getIsActive(), 0))) {
                audit.setStatus(RdmEventStatus.DISABLED.getValue());
                return object;
            }
            return executeMainTransaction(handlerVo, plugin, event, object, audit);
        } catch (Exception e) {
            // 即使父插件捕获子异常，也必须终止整个事件链。
            for (RdmEventAuditVo activeAudit : AUDITS.get()) {
                activeAudit.setStatus(RdmEventStatus.FAILED.getValue());
            }
            audit.setStatus(RdmEventStatus.FAILED.getValue());
            if (e.getMessage() != null) { audit.setError(e.getMessage()); }
            else { audit.setError(ExceptionUtils.getStackTrace(e)); }
            throw e;
        } finally {
            AUDITS.get().pop();
            CONFIGS.get().pop();
            if (CONFIGS.get().isEmpty()) { CONFIGS.remove(); AUDITS.remove(); }
            if (previousEvent == null) { currentEvent.remove(); }
            else { currentEvent.set(previousEvent); }
            audit.setEndTime(new Date());
            try { writeAudit(audit, false); }
            catch (Exception e) { logger.error("研发事件审计收尾失败：" + audit.getId(), e); }
        }
    }
    /** 根据活动调用栈阻止绕过父子关系和间接循环调用。 */
    private void validateExecutionPath(RdmEventHandlerVo handler, Long parentAuditId) {
        Deque<RdmEventHandlerVo> configs = CONFIGS.get();
        if (configs.isEmpty()) {
            if (parentAuditId != null || (handler.getParentId() != null && handler.getParentId() != 0L)) {
                throw new IllegalArgumentException("子配置必须由正在执行的父插件调用");
            }
        } else {
            RdmEventHandlerVo parent = configs.peek();
            if (!Objects.equals(AUDITS.get().peek().getId(), parentAuditId)
                    || !Objects.equals(parent.getProjectId(), handler.getProjectId())
                    || !Objects.equals(parent.getAppId(), handler.getAppId())
                    || !Objects.equals(parent.getEvent(), handler.getEvent())) {
                throw new IllegalArgumentException("子事件执行范围或父审计不匹配");
            }
            RdmEventManager.validateChild(parent, handler);
        }
        for (RdmEventHandlerVo active : configs) {
            if (Objects.equals(active.getId(), handler.getId())) {
                throw new IllegalArgumentException("事件插件循环调用");
            }
        }
    }
    /** 父插件可选择子配置，框架保留精确对象类型及父审计关联。 */
    protected final T triggerChild(RdmEventHandlerVo parent, RdmEventHandlerVo child, T object, RdmEventAuditVo audit) {
        if (currentEvent.get() == null || CONFIGS.get().peek() != parent || AUDITS.get().peek() != audit) {
            throw new IllegalArgumentException("子插件只能在当前父插件执行期间调用");
        }
        try {
            RdmEventManager.validateChild(parent, child);
            return RdmEventHandlerFactory.trigger(child, currentEvent.get(), object, audit.getId());
        } catch (Exception e) {
            audit.setStatus(RdmEventStatus.FAILED.getValue());
            throw e;
        }
    }
    /** 在新事务内重载对象，验证返回身份后提交，提交失败同样向外抛出。 */
    private T executeMainTransaction(RdmEventHandlerVo handler, RdmEventPluginVo plugin,
                                      RdmEventDefinition<T> event, T object, RdmEventAuditVo audit) {
        TransactionStatus tx = TransactionUtil.openNewTx();
        try {
            RdmEventRuntimeCapabilityFactory.requireAvailable();
            String objectId = event.getAdapter().getObjectId(object);
            if (!event.isDeleted()) {
                object = event.getAdapter().reload(handler.getProjectId(), handler.getAppId(), objectId);
            }
            RdmEventManager.validateObject(handler.getProjectId(), handler.getAppId(), event, object, objectId);
            RdmEventStatusVo status = new RdmEventStatusVo();
            RdmEventRuntimeCapabilityFactory.requireAvailable();
            object = myTrigger(handler, plugin, object, audit, status);
            RdmEventManager.validateObject(handler.getProjectId(), handler.getAppId(), event, object, objectId);
            if (RdmEventStatus.FAILED.getValue().equals(status.getStatus())
                    || RdmEventStatus.FAILED.getValue().equals(audit.getStatus())) {
                throw new RdmEventHandlerTriggerException("研发事件插件返回失败状态");
            }
            TransactionUtil.commitTx(tx);
            if (status.getStatus() != null) { audit.setStatus(status.getStatus()); }
            else if (RdmEventStatus.RUNNING.getValue().equals(audit.getStatus())) {
                audit.setStatus(RdmEventStatus.SUCCEED.getValue());
            }
            return object;
        } catch (Exception e) {
            rollback(tx);
            throw e;
        }
    }
    /** 审计使用独立事务，父业务回滚不会吞掉子插件记录。 */
    private void writeAudit(RdmEventAuditVo audit, boolean insert) {
        TransactionStatus tx = TransactionUtil.openNewTx();
        try {
            if (insert) { rdmEventMapper.insertAudit(audit); }
            else { rdmEventMapper.updateAudit(audit); }
            TransactionUtil.commitTx(tx);
        } catch (Exception e) {
            rollback(tx);
            throw e;
        }
    }
    /** 回滚失败只记录日志，保留原始执行异常。 */
    private void rollback(TransactionStatus tx) {
        if (tx == null || tx.isCompleted()) { return; }
        try { TransactionUtil.rollbackTx(tx); }
        catch (Exception e) { logger.warn("研发事件事务回滚失败", e); }
    }
    /** 记录通用对象身份与配置快照，不依赖具体业务 Vo。 */
    private RdmEventAuditVo createAudit(RdmEventHandlerVo handler, RdmEventDefinition<T> event, T object, Long parentAuditId) {
        RdmEventAuditVo audit = new RdmEventAuditVo();
        audit.setParentId(parentAuditId);
        audit.setProjectId(handler.getProjectId());
        audit.setAppId(handler.getAppId());
        audit.setObjectType(event.getObjectType());
        audit.setObjectId(event.getAdapter().getObjectId(object));
        audit.setEvent(event.getName());
        audit.setHandler(handler.getHandler());
        audit.setHandlerName(handler.getName());
        audit.setEventHandlerId(handler.getId());
        audit.setConfig(handler.getConfig());
        audit.setStartTime(new Date());
        audit.setStatus(RdmEventStatus.RUNNING.getValue());
        return audit;
    }
    /** 业务插件直接操作具体对象；返回值必须保留类型、身份和归属。 */
    protected abstract T myTrigger(RdmEventHandlerVo handlerVo, RdmEventPluginVo pluginVo,
                                    T object, RdmEventAuditVo auditVo, RdmEventStatusVo statusVo)
            throws RdmEventHandlerTriggerException;
}
