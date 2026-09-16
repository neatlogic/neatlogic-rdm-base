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

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.common.config.Config;
import neatlogic.framework.dao.plugin.CompressHandler;
import neatlogic.framework.rdm.dao.mapper.RdmEventMapper;
import neatlogic.framework.rdm.dto.RdmEventAuditVo;
import neatlogic.framework.rdm.dto.RdmEventHandlerVo;
import neatlogic.framework.rdm.dto.RdmEventPluginVo;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/** 使用独立临时 MySQL 验证真实 Mapper、建表脚本和压缩字段，禁止指向租户数据库。 */
public class RdmEventPersistenceTest {
    /** 参数依次为隔离实例 JDBC 地址、建表脚本和 Mapper XML；实例使用临时空密码 root。 */
    public static void main(String[] args) throws Exception {
        if (args.length != 3 || !args[0].matches("jdbc:mysql://127\\.0\\.0\\.1:(?!3306/)[0-9]+/rdm_event_test(?:\\?.*)?")) {
            throw new IllegalArgumentException("仅允许非 3306 端口的本机 rdm_event_test 临时数据库");
        }
        Field gzipField = Config.class.getDeclaredField("ENABLE_GZIP");
        gzipField.setAccessible(true);
        Object oldGzip = gzipField.get(null);
        gzipField.set(null, true);
        try {
            UnpooledDataSource ds = new UnpooledDataSource("com.mysql.cj.jdbc.Driver", args[0], "root", "");
            try (Connection connection = ds.getConnection(); Statement statement = connection.createStatement();
                 ResultSet tables = statement.executeQuery("show tables")) {
                check(!tables.next(), "临时数据库必须为空，避免覆盖已有测试记录");
            }
            try (Connection connection = ds.getConnection(); Statement statement = connection.createStatement()) {
                String ddl = new String(Files.readAllBytes(Paths.get(args[1])), StandardCharsets.UTF_8);
                for (String sql : ddl.split(";")) {
                    if (sql.trim().isEmpty()) { continue; }
                    try { statement.execute(sql); }
                    catch (SQLException e) {
                        // 仅允许新建表后的重复列、重复索引或已移除索引，其他 SQL 错误必须失败。
                        if (e.getErrorCode() != 1060 && e.getErrorCode() != 1061 && e.getErrorCode() != 1091) { throw e; }
                    }
                }
            }
            Configuration config = new Configuration(new Environment("isolated-rdm-event", new JdbcTransactionFactory(), ds));
            config.getTypeAliasRegistry().registerAlias("CompressHandler", CompressHandler.class);
            try (Reader xml = Files.newBufferedReader(Paths.get(args[2]), StandardCharsets.UTF_8)) {
                new XMLMapperBuilder(xml, config, args[2], config.getSqlFragments()).parse();
            }
            try (SqlSession session = new SqlSessionFactoryBuilder().build(config).openSession(true)) {
                RdmEventMapper mapper = session.getMapper(RdmEventMapper.class);
                verifyHandlers(mapper, session.getConnection());
                verifyAudits(mapper, session.getConnection());
            }
            System.out.println("RdmEventPersistenceTest passed: DDL、作用范围、UUID、审计分页和压缩读写");
        } finally {
            gzipField.set(null, oldGzip);
        }
    }

    /** 验证根配置范围、同序号顺序、子配置引用、全局配置以及压缩前后兼容读取。 */
    private static void verifyHandlers(RdmEventMapper mapper, Connection connection) throws Exception {
        JSONObject payload = payload();
        RdmEventHandlerVo root = handler(101L, null, payload);
        mapper.saveHandler(root);
        mapper.saveHandler(handler(102L, null, payload));
        mapper.saveHandler(handler(103L, 101L, payload));
        RdmEventHandlerVo other = handler(104L, null, payload);
        other.setProjectId(77L);
        mapper.saveHandler(other);
        RdmEventHandlerVo global = handler(105L, null, payload);
        global.setProjectId(null);
        global.setAppId(null);
        mapper.saveHandler(global);
        check(mapper.getHandlerByEvent("ISSUE_UPDATE", 77L, 8L).size() == 1, "其他项目配置串用");
        check(mapper.getHandlerByEvent("ISSUE_UPDATE", 9L, 88L).isEmpty(), "其他应用或全局配置被匹配");
        check(mapper.getHandlerByEvent("ISSUE_UPDATE", 9L, 8L).size() == 2, "根配置范围或父子过滤错误");
        check(mapper.getHandlerByEvent("ISSUE_UPDATE", 9L, 8L).get(0).getId().equals(101L), "同 sort 未按 id 排序");
        check(mapper.getHandlerByParentId(101L).size() == 1, "子配置查询错误");
        check(payload.equals(mapper.getHandlerByUuid("test-103").getConfig()), "UUID 查询未解压配置");
        check(payload.equals(mapper.getHandlerById(101L).getConfig()), "ID 查询未解压配置");
        check(payload.equals(mapper.getHandlerByEvent("ISSUE_UPDATE", 9L, 8L).get(0).getConfig()), "事件查询未解压配置");
        check(payload.equals(mapper.getHandlerByParentId(101L).get(0).getConfig()), "父配置查询未解压配置");
        assertCompressed(connection, "rdm_event_handler", "config", "id=101");
        root.setName("更新配置");
        root.setConfigStr("{\"small\":true}");
        mapper.saveHandler(root);
        check(mapper.getHandlerById(101L).getConfig().getBooleanValue("small"), "短 JSON 更新或旧对象缓存错误");
        check(root.getId().equals(101L), "已有配置 ID 被替换");

        RdmEventPluginVo plugin = new RdmEventPluginVo();
        plugin.setName("test-plugin");
        plugin.setConfig(payload);
        plugin.setIsActive(1);
        mapper.savePlugin(plugin);
        check(payload.equals(mapper.getPluginByName("test-plugin").getConfig()), "全局配置未解压");
        assertCompressed(connection, "rdm_event_plugin", "config", "name='test-plugin'");
        plugin.setConfigStr("{\"changed\":true}");
        mapper.savePlugin(plugin);
        check(mapper.getPluginByName("test-plugin").getConfig().getBooleanValue("changed"), "全局配置更新错误");
    }

    /** 验证审计根分页与数量一致、子审计关联以及配置、结果、异常压缩映射。 */
    private static void verifyAudits(RdmEventMapper mapper, Connection connection) throws Exception {
        RdmEventAuditVo root = audit(900L, null, 55L);
        mapper.insertAudit(root);
        mapper.insertAudit(audit(901L, 900L, 55L));
        mapper.insertAudit(audit(902L, null, 56L));
        mapper.insertAudit(audit(903L, null, 55L));
        RdmEventAuditVo external = audit(904L, null, 55L);
        external.setObjectType("merge_request");
        external.setObjectId("repo-a!42");
        mapper.insertAudit(external);
        check(mapper.searchAuditCount(9L, 8L, "merge_request", "repo-a!42") == 1, "外部字符串标识查询失败");
        check(mapper.searchAuditCount(99L, 8L, "merge_request", "repo-a!42") == 0, "审计项目范围串用");
        check(mapper.searchAuditCount(9L, 8L, "issue", "55") == 2, "根审计数量包含子审计或其他需求");
        check(mapper.searchAudit(9L, 8L, "issue", "55", 0, 1).size() == 1, "根审计分页错误");
        check(!mapper.searchAudit(9L, 8L, "issue", "55", 0, 1).get(0).getId().equals(mapper.searchAudit(9L, 8L, "issue", "55", 1, 1).get(0).getId()), "分页记录重复");
        check(mapper.getChildAuditByParentId(900L).get(0).getId().equals(901L), "父审计关联错误");
        root.setResult(payload());
        root.setError(payload().toJSONString());
        root.setStatus("failed");
        root.setEndTime(new Date());
        mapper.updateAudit(root);
        RdmEventAuditVo loaded = mapper.getAuditById(900L);
        check(root.getConfig().equals(loaded.getConfig()), "审计配置未解压");
        check(root.getResult().equals(loaded.getResult()), "审计结果未解压");
        check(root.getError().equals(loaded.getError()), "审计异常未解压");
        check("failed".equals(loaded.getStatus()) && loaded.getEndTime() != null, "审计终态未写入");
        check(loaded.getId().equals(900L), "数据库映射生成了新审计 ID");
        assertCompressed(connection, "rdm_event_audit", "config", "id=900");
        assertCompressed(connection, "rdm_event_audit", "result", "id=900");
        assertCompressed(connection, "rdm_event_audit", "error", "id=900");
        for (RdmEventAuditVo row : mapper.searchAudit(9L, 8L, "issue", "55", 0, 10)) {
            check(payload().equals(row.getConfig()), "根审计列表未解压配置");
        }
        check(payload().equals(mapper.getChildAuditByParentId(900L).get(0).getConfig()), "子审计列表未解压配置");
    }

    /** 构造带固定标识和范围的配置，不依赖运行环境的 ID 初始化。 */
    private static RdmEventHandlerVo handler(Long id, Long parentId, JSONObject payload) {
        RdmEventHandlerVo vo = new RdmEventHandlerVo();
        vo.setId(id);
        vo.setUuid("test-" + id);
        vo.setParentId(parentId);
        vo.setProjectId(9L);
        vo.setAppId(8L);
        vo.setHandler("test-plugin");
        vo.setEvent("ISSUE_UPDATE");
        vo.setSort(1);
        vo.setConfig(payload);
        return vo;
    }

    /** 构造独立根或子审计。 */
    private static RdmEventAuditVo audit(Long id, Long parentId, Long issueId) {
        RdmEventAuditVo vo = new RdmEventAuditVo();
        vo.setId(id);
        vo.setParentId(parentId);
        vo.setProjectId(9L);
        vo.setAppId(8L);
        vo.setObjectType("issue");
        vo.setObjectId(String.valueOf(issueId));
        vo.setEventHandlerId(101L);
        vo.setHandler("test-plugin");
        vo.setEvent("ISSUE_UPDATE");
        vo.setStatus("running");
        vo.setStartTime(new Date());
        vo.setConfig(payload());
        return vo;
    }

    /** 生成超过压缩阈值的中文 JSON。 */
    private static JSONObject payload() {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 1200; i++) {
            content.append("研发");
        }
        JSONObject payload = new JSONObject();
        payload.put("text", content.toString());
        return payload;
    }

    /** 原始 JDBC 读取确认为压缩值，避免仅测试普通 JSON 往返。 */
    private static void assertCompressed(Connection connection, String table, String column, String predicate) throws SQLException {
        try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery("select " + column + " from " + table + " where " + predicate)) {
            check(rs.next() && rs.getString(1).startsWith("GZIP:"), "字段未真正压缩：" + table + "." + column);
        }
    }

    /** 校验测试结果。 */
    private static void check(boolean passed, String message) {
        if (!passed) {
            throw new AssertionError(message);
        }
    }
}
