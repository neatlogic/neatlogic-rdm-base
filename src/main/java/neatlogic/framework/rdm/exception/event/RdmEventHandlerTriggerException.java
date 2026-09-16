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

/** 研发事件插件执行异常，统一向调用方暴露领域错误并保留原始原因。 */
public class RdmEventHandlerTriggerException extends ApiRuntimeException {
    private static final long serialVersionUID = 1L;

    /** 包装执行异常，保留原因及其附带的回滚错误。 */
    public RdmEventHandlerTriggerException(Exception cause) {
        super(cause);
    }

    /** 创建事件校验或执行约束错误。 */
    public RdmEventHandlerTriggerException(String message) {
        super(message);
    }
}
