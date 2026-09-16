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

package neatlogic.framework.rdm.crossover;

import neatlogic.framework.crossover.ICrossoverService;
import neatlogic.framework.rdm.dto.IssueVo;

/** 向事件框架提供业务模块的需求查询及属性补全能力。 */
public interface IRdmIssueCrossoverService extends ICrossoverService {
    /** 查询并补全需求，不存在时返回空值。 */
    IssueVo getIssueById(Long id);
}
