/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 *
 */

package neatlogic.framework.rdm.notify.constvalue;

import neatlogic.framework.common.constvalue.ParamType;
import neatlogic.framework.notify.core.INotifyParam;
import neatlogic.framework.util.I18n;

/**
 * RDM通知公共模板参数。
 */
public enum RdmNotifyParam implements INotifyParam {
    BIZ_TYPE("bizType", new I18n("业务分类"), ParamType.STRING),
    PROJECT_ID("projectId", new I18n("项目ID"), ParamType.NUMBER),
    PROJECT_NAME("projectName", new I18n("项目名称"), ParamType.STRING),
    PROJECT_TYPE("projectType", new I18n("项目类型"), ParamType.STRING),
    PROJECT_START_DATE("projectStartDate", new I18n("项目开始日期"), ParamType.DATE),
    PROJECT_END_DATE("projectEndDate", new I18n("项目结束日期"), ParamType.DATE),
    PROJECT_URL("projectUrl", new I18n("项目地址"), ParamType.STRING),
    PROJECT_CONFIG_URL("projectConfigUrl", new I18n("项目配置地址"), ParamType.STRING),
    OLD_PROJECT_NAME("oldProjectName", new I18n("原项目名称"), ParamType.STRING),
    PROJECT_DESCRIPTION("projectDescription", new I18n("项目说明"), ParamType.STRING),
    OLD_PROJECT_DESCRIPTION("oldProjectDescription", new I18n("原项目说明"), ParamType.STRING),
    OLD_PROJECT_START_DATE("oldProjectStartDate", new I18n("原项目开始日期"), ParamType.DATE),
    OLD_PROJECT_END_DATE("oldProjectEndDate", new I18n("原项目结束日期"), ParamType.DATE),
    PROJECT_MEMBER_SUMMARY("projectMemberSummary", new I18n("项目成员摘要"), ParamType.ARRAY),
    OLD_PROJECT_MEMBER_SUMMARY("oldProjectMemberSummary", new I18n("原项目成员摘要"), ParamType.ARRAY),
    PROJECT_IS_CLOSE("projectIsClose", new I18n("项目是否关闭"), ParamType.NUMBER),
    OPERATOR("rdmOperator", new I18n("当前操作人"), ParamType.STRING),
    CHANGED_FIELD_LIST("changedFieldList", new I18n("变更字段列表"), ParamType.ARRAY),
    OLD_VALUE("oldValue", new I18n("变更前摘要"), ParamType.STRING),
    NEW_VALUE("newValue", new I18n("变更后摘要"), ParamType.STRING),
    ITERATION_ID("iterationId", new I18n("迭代ID"), ParamType.NUMBER),
    ITERATION_NAME("iterationName", new I18n("迭代名称"), ParamType.STRING),
    ITERATION_DESCRIPTION("iterationDescription", new I18n("迭代说明"), ParamType.STRING),
    ITERATION_START_DATE("iterationStartDate", new I18n("迭代开始日期"), ParamType.DATE),
    ITERATION_END_DATE("iterationEndDate", new I18n("迭代结束日期"), ParamType.DATE),
    ITERATION_IS_OPEN("iterationIsOpen", new I18n("迭代是否开启"), ParamType.NUMBER),
    ITERATION_ISSUE_COUNT("iterationIssueCount", new I18n("迭代事项总数"), ParamType.NUMBER),
    ITERATION_DONE_ISSUE_COUNT("iterationDoneIssueCount", new I18n("迭代已完成事项数"), ParamType.NUMBER),
    ITERATION_COMPLETE_RATE("iterationCompleteRate", new I18n("迭代完成率"), ParamType.NUMBER),
    ITERATION_URL("iterationUrl", new I18n("迭代地址"), ParamType.STRING),
    ISSUE_ID("issueId", new I18n("事项ID"), ParamType.NUMBER),
    ISSUE_NAME("issueName", new I18n("事项名称"), ParamType.STRING),
    APP_TYPE("appType", new I18n("App类型"), ParamType.STRING),
    APP_NAME("appName", new I18n("App名称"), ParamType.STRING),
    STATUS_NAME("statusName", new I18n("当前状态"), ParamType.STRING),
    OLD_STATUS_NAME("oldStatusName", new I18n("原状态"), ParamType.STRING),
    WORKER_LIST("workerList", new I18n("当前处理人"), ParamType.ARRAY),
    OLD_WORKER_LIST("oldWorkerList", new I18n("原处理人"), ParamType.ARRAY),
    CREATOR("creator", new I18n("创建人"), ParamType.STRING),
    PRIORITY_NAME("priorityName", new I18n("优先级"), ParamType.STRING),
    ISSUE_CONTENT("issueContent", new I18n("事项内容"), ParamType.STRING),
    COMMENT_CONTENT("commentContent", new I18n("评论内容"), ParamType.STRING),
    ISSUE_URL("issueUrl", new I18n("事项地址"), ParamType.STRING);

    private final String value;
    private final I18n text;
    private final ParamType paramType;

    RdmNotifyParam(String value, I18n text, ParamType paramType) {
        this.value = value;
        this.text = text;
        this.paramType = paramType;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text.toString();
    }

    @Override
    public ParamType getParamType() {
        return paramType;
    }
}
