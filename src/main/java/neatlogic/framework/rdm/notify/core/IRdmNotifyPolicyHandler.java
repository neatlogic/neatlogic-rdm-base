/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 *
 */

package neatlogic.framework.rdm.notify.core;

import neatlogic.framework.notify.core.INotifyPolicyHandler;

/**
 * RDM通知策略处理器扩展契约。
 */
public interface IRdmNotifyPolicyHandler extends INotifyPolicyHandler {

    /**
     * 返回处理器对应的业务分类，项目固定为project，其余分类使用App_type。
     */
    String getBizType();
}
