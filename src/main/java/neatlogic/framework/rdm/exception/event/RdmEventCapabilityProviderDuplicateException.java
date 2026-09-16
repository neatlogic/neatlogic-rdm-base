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

/** 研发事件运行能力提供方重复注册。 */
public class RdmEventCapabilityProviderDuplicateException extends ApiRuntimeException {
    /** 报告固定的事件能力错误。 */
    public RdmEventCapabilityProviderDuplicateException() { super("研发事件运行能力提供方重复注册"); }
}
