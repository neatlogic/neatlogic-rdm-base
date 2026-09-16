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

/** 研发事件插件不能为空。 */
public class RdmEventHandlerIsEmptyException extends ApiRuntimeException {
    /** 使用固定语义说明失败原因。 */
    public RdmEventHandlerIsEmptyException() { super("研发事件插件不能为空"); }
}
