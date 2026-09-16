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

package neatlogic.framework.rdm.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.event.RdmEventStatus;
import neatlogic.framework.restful.annotation.EntityField;

/** 插件向执行框架反馈最终状态。 */
public class RdmEventStatusVo {
    @EntityField(name = "状态", type = ApiParamType.STRING)
    private String status;

    /** 判断是否跳过。 */
    public boolean isSkipped() {
        return RdmEventStatus.SKIPPED.getValue().equals(status);
    }

    /** 设置或清除跳过状态。 */
    public void setSkipped(boolean skipped) {
        if (skipped) {
            status = RdmEventStatus.SKIPPED.getValue();
        } else if (isSkipped()) {
            status = null;
        }
    }

    /** 获取插件反馈状态。 */
    public String getStatus() {
        return status;
    }

    /** 设置插件反馈状态。 */
    public void setStatus(String status) {
        this.status = status;
    }
}
