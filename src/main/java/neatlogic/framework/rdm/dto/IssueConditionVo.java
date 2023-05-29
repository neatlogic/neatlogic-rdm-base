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

import java.util.List;

public class IssueConditionVo extends IssueVo {
    private List<String> startDate;
    private List<String> endDate;

    private List<Long> status;

    public List<Long> getStatusList() {
        return status;
    }

    public void setStatus(List<Long> status) {
        this.status = status;
    }

    public List<String> getStartDateRange() {
        return startDate;
    }

    public void setStartDate(List<String> startDate) {
        this.startDate = startDate;
    }

    public List<String> getEndDateRange() {
        return endDate;
    }

    public void setEndDate(List<String> endDate) {
        this.endDate = endDate;
    }

}
