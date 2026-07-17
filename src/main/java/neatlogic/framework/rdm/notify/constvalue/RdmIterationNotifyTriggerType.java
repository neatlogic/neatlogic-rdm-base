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
 * 迭代通知触发点。
 */
public enum RdmIterationNotifyTriggerType implements INotifyTriggerType {
    CREATED("created", new I18n("迭代创建"), new I18n("迭代创建后触发")),
    UPDATED("updated", new I18n("迭代更新"), new I18n("迭代基本信息更新后触发")),
    OPENED("opened", new I18n("迭代开启"), new I18n("迭代开启后触发")),
    CLOSED("closed", new I18n("迭代关闭"), new I18n("迭代关闭后触发"));

    private final String trigger;
    private final I18n text;
    private final I18n description;

    RdmIterationNotifyTriggerType(String trigger, I18n text, I18n description) {
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
