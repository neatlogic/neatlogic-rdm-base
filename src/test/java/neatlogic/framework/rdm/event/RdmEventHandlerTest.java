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
import neatlogic.framework.rdm.dto.*;
import neatlogic.framework.transaction.util.TransactionUtil;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static neatlogic.framework.rdm.event.RdmEventTestSupport.*;

/** 使用真实 Spring 事务管理器及可观测 JDBC 替身验证插件边界，不读写数据库。 */
public class RdmEventHandlerTest {
    /** 插件测试动作，可选择调用子插件或抛出失败。 */
    interface Action {
        IssueVo execute(IssueVo issue, RdmEventAuditVo audit, RdmEventStatusVo status);
    }

    /** 封装每个用例独立的审计、业务查询和 JDBC 事务状态。 */
    static class Fixture {
        final List<RdmEventAuditVo> audits = new ArrayList<>();
        final List<String> transactions = new ArrayList<>();
        IssueVo loaded;
        RuntimeException loadFailure;
        RuntimeException insertFailure;
        RuntimeException updateFailure;
        boolean commitFailure;
        boolean rollbackFailure;
        boolean beginFailure;
        boolean disabledPlugin;
        int connections;
        int reloads;
        int updates;
        final RdmEventMapper mapper;
        RdmEventHandlerBase<IssueVo> cachedHandler;
        Action currentAction;

        /** 注册事务资源及审计替身，业务重载由事件适配器提供。 */
        @SuppressWarnings({"unchecked", "rawtypes"})
        Fixture() throws Exception {
            mapper = (RdmEventMapper) Proxy.newProxyInstance(RdmEventMapper.class.getClassLoader(), new Class<?>[]{RdmEventMapper.class}, (proxy, method, args) -> {
                if (method.getName().equals("insertAudit")) {
                    if (insertFailure != null) { throw insertFailure; }
                    RdmEventAuditVo audit = (RdmEventAuditVo) args[0];
                    audit.setId(100L + audits.size());
                    audits.add(audit);
                } else if (method.getName().equals("updateAudit")) {
                    updates++;
                    if (updateFailure != null) { throw updateFailure; }
                } else if (method.getName().equals("getPluginByName")) {
                    if (disabledPlugin) {
                        RdmEventPluginVo plugin = new RdmEventPluginVo();
                        plugin.setIsActive(0);
                        return plugin;
                    }
                    return null;
                } else {
                    throw new AssertionError("非预期 Mapper 调用：" + method.getName());
                }
                if (method.getReturnType() == int.class) { return 1; }
                return null;
            });
            new TransactionUtil(new DataSourceTransactionManager(new AbstractDataSource() {
                /** 记录每个独立连接的提交和回滚。 */
                public Connection getConnection() throws SQLException {
                    if (beginFailure) { throw new SQLException("测试开启事务失败"); }
                    final int id = ++connections;
                    transactions.add("begin:" + id);
                    return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class<?>[]{Connection.class}, (proxy, method, args) -> {
                        if (method.getName().equals("getAutoCommit")) { return true; }
                        if (method.getName().equals("getTransactionIsolation")) { return Connection.TRANSACTION_READ_COMMITTED; }
                        if (method.getName().equals("commit")) {
                            transactions.add("commit:" + id);
                            if (commitFailure) { commitFailure = false; throw new SQLException("测试提交失败"); }
                        }
                        if (method.getName().equals("rollback")) {
                            transactions.add("rollback:" + id);
                            if (rollbackFailure) { throw new SQLException("测试回滚失败"); }
                        }
                        if (method.getName().equals("equals")) { return proxy == args[0]; }
                        if (method.getName().equals("hashCode")) { return System.identityHashCode(proxy); }
                        if (method.getReturnType() == boolean.class) { return false; }
                        if (method.getReturnType() == int.class) { return 0; }
                        return null;
                    });
                }
                /** 事务管理器不需要凭据，统一返回替身连接。 */
                public Connection getConnection(String user, String password) throws SQLException { return getConnection(); }
            }));
        }

        /** 创建仅由动作决定行为的 Base 插件。 */
        RdmEventHandlerBase<IssueVo> handler(Action action) {
            currentAction = action;
            if (cachedHandler != null) { return cachedHandler; }
            cachedHandler = new RdmEventHandlerBase<IssueVo>() {
                { rdmEventMapper = mapper; }
                /** 获取对象类型。 */
                public Class<IssueVo> getObjectClass() { return IssueVo.class; }
                public String getName() { return "test"; }
                public String getLabel() { return "测试"; }
                public String getIcon() { return ""; }
                public String getDescription() { return "测试插件"; }
                public Set<String> supportEventTypes() { return Collections.singleton("ISSUE_UPDATE"); }
                public Set<String> supportParentHandler() { return Collections.emptySet(); }
                /** 将测试动作置于生产 Base 的事务和审计流程内。 */
                protected IssueVo myTrigger(RdmEventHandlerVo config, RdmEventPluginVo plugin, IssueVo issue,
                        RdmEventAuditVo audit, RdmEventStatusVo status) {
                    check(TransactionSynchronizationManager.isActualTransactionActive(), "插件不在事务内");
                    return currentAction.execute(issue, audit, status);
                }
            };
            register(cachedHandler);
            return cachedHandler;
        }

        /** 获取本用例最后一条审计。 */
        RdmEventAuditVo last() { return audits.get(audits.size() - 1); }
    }

    /** 构建具备可控重载行为的事件定义。 */
    private static RdmEventDefinition<IssueVo> event(Fixture f, boolean deleted) {
        IRdmEventObjectAdapter<IssueVo> adapter = new IRdmEventObjectAdapter<IssueVo>() {
            /** 获取业务类型。 */
            public Class<IssueVo> getObjectClass() { return IssueVo.class; }
            /** 获取对象类型标识。 */
            public String getObjectType() { return "issue"; }
            /** 提取对象标识。 */
            public String getObjectId(IssueVo vo) { return ADAPTER.getObjectId(vo); }
            /** 复制独立快照。 */
            public IssueVo snapshot(IssueVo vo) { return ADAPTER.snapshot(vo); }
            /** 在插件事务内读取最新对象。 */
            public IssueVo reload(Long projectId, Long appId, String id) {
                check(TransactionSynchronizationManager.isActualTransactionActive(), "重载未置于事务内");
                f.reloads++;
                if (f.loadFailure != null) { throw f.loadFailure; }
                return f.loaded;
            }
            /** 保持对象归属一致。 */
            public void validateScope(Long projectId, Long appId, IssueVo vo) { ADAPTER.validateScope(projectId, appId, vo); }
        };
        RdmEventObjectAdapterFactory.register(adapter);
        return new RdmEventDefinition<>("ISSUE_UPDATE", "更新", "测试", Collections.singleton("story"),
                adapter.getObjectType(), IssueVo.class, deleted);
    }

    /** 执行生产插件事务及审计回归。 */
    public static void main(String[] args) throws Exception {
        initMessages();
        Fixture f = new Fixture();
        f.loaded = issue(1L);
        f.loaded.setName("最新值");
        RdmEventDefinition<IssueVo> event = event(f, false);
        RdmEventRegistry.register(event);
        IssueVo actual = f.handler((vo, audit, status) -> {
            check("最新值".equals(vo.getName()), "插件未获取最新对象");
            status.setSkipped(true); return vo;
        }).trigger(config(1, 0, "test"), event, issue(1));
        check(actual == f.loaded && f.reloads == 1, "重载结果未传入插件");
        check("skipped".equals(f.last().getStatus()), "跳过状态被覆盖");
        check("issue".equals(f.last().getObjectType()) && "1".equals(f.last().getObjectId()), "通用审计对象标识错误");
        f.loaded = null;
        expectFailure(f.handler((vo,a,t) -> { throw new AssertionError("不存在对象执行了业务"); }), event);
        check("failed".equals(f.last().getStatus()), "对象不存在未记录失败");
        f.loaded = issue(1L);
        RuntimeException failure = new IllegalArgumentException("业务失败");
        check(expectFailure(f.handler((vo,a,t) -> { throw failure; }), event) == failure, "原异常被覆盖");
        check("failed".equals(f.last().getStatus()), "业务失败未记录");
        expectFailure(f.handler((vo,a,t) -> issue(2L)), event);
        expectFailure(f.handler((vo,a,t) -> null), event);
        RdmEventHandlerVo disabled = config(1,0,"test"); disabled.setIsActive(0);
        f.handler((vo,a,t) -> { throw new AssertionError("禁用配置执行了业务"); }).trigger(disabled,event,issue(1));
        check("disabled".equals(f.last().getStatus()), "禁用状态丢失");
        // 仅使业务事务提交失败，独立审计事务仍可正常收尾。
        expectFailure(f.handler((vo,a,t) -> { f.commitFailure = true; return vo; }), event);
        f.commitFailure = false;
        check("failed".equals(f.last().getStatus()), "提交失败未标记失败");
        System.out.println("RdmEventHandlerTest passed: 最新对象、身份、禁用、异常、通用审计与事务内重载");
    }

    /** 确认失败向调用链传播。 */
    private static RuntimeException expectFailure(RdmEventHandlerBase<IssueVo> handler, RdmEventDefinition<IssueVo> event) {
        try { handler.trigger(config(1,0,"test"),event,issue(1)); }
        catch (RuntimeException failure) { return failure; }
        throw new AssertionError("预期失败未抛出");
    }
}
