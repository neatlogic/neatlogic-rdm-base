/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package neatlogic.framework.rdm.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.restful.annotation.EntityField;

public class IssueRelVo extends BasePageVo {
    @EntityField(name = "来源应用id", type = ApiParamType.LONG)
    private Long fromAppId;
    @EntityField(name = "来源任务id", type = ApiParamType.LONG)
    private Long fromIssueId;
    @EntityField(name = "目标应用id", type = ApiParamType.LONG)
    private Long toAppId;
    @EntityField(name = "目标任务id", type = ApiParamType.LONG)
    private Long toIssueId;

    public IssueRelVo() {

    }

    public IssueRelVo(Long fromAppId, Long fromIssueId, Long toAppId, Long toIssueId) {
        this.fromAppId = fromAppId;
        this.fromIssueId = fromIssueId;
        this.toAppId = toAppId;
        this.toIssueId = toIssueId;
    }

    public Long getFromAppId() {
        return fromAppId;
    }

    public void setFromAppId(Long fromAppId) {
        this.fromAppId = fromAppId;
    }

    public Long getFromIssueId() {
        return fromIssueId;
    }

    public void setFromIssueId(Long fromIssueId) {
        this.fromIssueId = fromIssueId;
    }

    public Long getToAppId() {
        return toAppId;
    }

    public void setToAppId(Long toAppId) {
        this.toAppId = toAppId;
    }

    public Long getToIssueId() {
        return toIssueId;
    }

    public void setToIssueId(Long toIssueId) {
        this.toIssueId = toIssueId;
    }
}
