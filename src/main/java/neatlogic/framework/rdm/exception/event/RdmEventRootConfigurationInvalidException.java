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

/** 根事件配置不能携带父配置标识。 */
public class RdmEventRootConfigurationInvalidException extends ApiRuntimeException {
    private static final long serialVersionUID = 1L;

    /** 创建固定语义的领域异常，调用方无需传入错误文本。 */
    public RdmEventRootConfigurationInvalidException() {
        super("根事件配置不能携带父配置标识");
    }
}
