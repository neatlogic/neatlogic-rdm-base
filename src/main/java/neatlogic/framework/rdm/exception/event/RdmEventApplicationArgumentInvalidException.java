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

package neatlogic.framework.rdm.exception.event;

import neatlogic.framework.exception.core.ApiRuntimeException;

/** 项目、应用或已注册事件定义无效。 */
public class RdmEventApplicationArgumentInvalidException extends ApiRuntimeException {
    private static final long serialVersionUID = 1L;

    /** 创建固定语义的领域异常，调用方无需传入错误文本。 */
    public RdmEventApplicationArgumentInvalidException() {
        super("项目、应用或已注册事件定义无效");
    }
}
