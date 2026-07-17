/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 *
 */

package neatlogic.framework.rdm.notify.constvalue;

import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.util.I18n;

/**
 * Issue通知触发点。
 */
public enum RdmIssueNotifyTriggerType implements INotifyTriggerType {
    CREATED("created", new I18n("创建"), new I18n("创建后触发")),
    UPDATED("updated", new I18n("更新"), new I18n("普通字段更新后触发")),
    STATUS_CHANGED("statuschanged", new I18n("状态变更"), new I18n("状态变更后触发")),
    WORKER_CHANGED("workerchanged", new I18n("处理人变更"), new I18n("处理人变更后触发")),
    COMMENTED("commented", new I18n("新增评论"), new I18n("新增一级评论后触发")),
    REPLIED("replied", new I18n("回复评论"), new I18n("回复评论后触发")),
    DELETED("deleted", new I18n("删除"), new I18n("删除后触发"));

    private final String trigger;
    private final I18n text;
    private final I18n description;

    RdmIssueNotifyTriggerType(String trigger, I18n text, I18n description) {
        this.trigger = trigger;
        this.text = text;
        this.description = description;
    }

    @Override
    public String getTrigger() {
        return trigger;
    }

    @Override
    public String getText() {
        return text.toString();
    }

    @Override
    public String getDescription() {
        return description.toString();
    }
}
