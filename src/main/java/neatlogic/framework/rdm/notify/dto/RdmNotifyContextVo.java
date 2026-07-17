/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 *
 */

package neatlogic.framework.rdm.notify.dto;

import neatlogic.framework.notify.dto.InvokeNotifyPolicyConfigVo;
import neatlogic.framework.rdm.dto.CommentVo;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.dto.IterationVo;
import neatlogic.framework.rdm.dto.ProjectVo;

import java.util.ArrayList;
import java.util.List;

/**
 * RDM通知运行上下文。
 * 删除类事件必须在删除数据前填充完整快照，保证事务提交后仍能生成通知内容。
 */
public class RdmNotifyContextVo {
    private String bizType;
    private ProjectVo projectVo;
    private ProjectVo oldProjectVo;
    private AppVo appVo;
    private IterationVo iterationVo;
    private IterationVo oldIterationVo;
    private IssueVo issueVo;
    private IssueVo oldIssueVo;
    private CommentVo commentVo;
    private String operatorUuid;
    private String operatorName;
    private String replyUserUuid;
    private List<String> changedFieldList = new ArrayList<>();
    private InvokeNotifyPolicyConfigVo notifyPolicyConfig;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public ProjectVo getProjectVo() {
        return projectVo;
    }

    public void setProjectVo(ProjectVo projectVo) {
        this.projectVo = projectVo;
    }

    public ProjectVo getOldProjectVo() {
        return oldProjectVo;
    }

    public void setOldProjectVo(ProjectVo oldProjectVo) {
        this.oldProjectVo = oldProjectVo;
    }

    public AppVo getAppVo() {
        return appVo;
    }

    public void setAppVo(AppVo appVo) {
        this.appVo = appVo;
    }

    public IterationVo getIterationVo() {
        return iterationVo;
    }

    public void setIterationVo(IterationVo iterationVo) {
        this.iterationVo = iterationVo;
    }

    public IterationVo getOldIterationVo() {
        return oldIterationVo;
    }

    public void setOldIterationVo(IterationVo oldIterationVo) {
        this.oldIterationVo = oldIterationVo;
    }

    public IssueVo getIssueVo() {
        return issueVo;
    }

    public void setIssueVo(IssueVo issueVo) {
        this.issueVo = issueVo;
    }

    public IssueVo getOldIssueVo() {
        return oldIssueVo;
    }

    public void setOldIssueVo(IssueVo oldIssueVo) {
        this.oldIssueVo = oldIssueVo;
    }

    public CommentVo getCommentVo() {
        return commentVo;
    }

    public void setCommentVo(CommentVo commentVo) {
        this.commentVo = commentVo;
    }

    public String getOperatorUuid() {
        return operatorUuid;
    }

    public void setOperatorUuid(String operatorUuid) {
        this.operatorUuid = operatorUuid;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getReplyUserUuid() {
        return replyUserUuid;
    }

    public void setReplyUserUuid(String replyUserUuid) {
        this.replyUserUuid = replyUserUuid;
    }

    public List<String> getChangedFieldList() {
        return changedFieldList;
    }

    public void setChangedFieldList(List<String> changedFieldList) {
        this.changedFieldList = changedFieldList;
    }

    public InvokeNotifyPolicyConfigVo getNotifyPolicyConfig() {
        return notifyPolicyConfig;
    }

    public void setNotifyPolicyConfig(InvokeNotifyPolicyConfigVo notifyPolicyConfig) {
        this.notifyPolicyConfig = notifyPolicyConfig;
    }
}
