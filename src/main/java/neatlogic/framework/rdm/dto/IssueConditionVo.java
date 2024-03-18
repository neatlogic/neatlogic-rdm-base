/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.framework.rdm.dto;

import neatlogic.framework.asynchronization.threadlocal.UserContext;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class IssueConditionVo extends IssueVo {
    private List<String> startDate;
    private List<String> endDate;

    private List<String> createDate;

    private List<Long> status;

    private Integer isMine;

    private Integer isMyCreated;

    private Integer isExpired;

    private Integer isProcessed;//曾经处理过

    private List<String> userIdList;

    private List<IssueSortVo> sortList;

    private String currentUser;

    public Integer getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(Integer isProcessed) {
        this.isProcessed = isProcessed;
    }

    public Integer getIsMyCreated() {
        return isMyCreated;
    }

    public void setIsMyCreated(Integer isMyCreated) {
        this.isMyCreated = isMyCreated;
    }


    @Override
    public Integer getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Integer isExpired) {
        this.isExpired = isExpired;
    }

    public List<Long> getStatusList() {
        return status;
    }

    public String getCurrentUser() {
        return UserContext.get().getUserUuid(true);
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

    public List<String> getCreateDateRange() {
        return createDate;
    }

    public void setCreateDate(List<String> createDate) {
        this.createDate = createDate;
    }

    public List<String> getEndDateRange() {
        return endDate;
    }

    public void setEndDate(List<String> endDate) {
        this.endDate = endDate;
    }

    public Integer getIsMine() {
        return isMine;
    }

    public void setIsMine(Integer isMine) {
        this.isMine = isMine;
    }

    @Override
    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public List<IssueSortVo> getSortList() {
        return sortList;
    }

    public void setSortList(List<IssueSortVo> sortList) {
        this.sortList = sortList;
    }

    public List<String> getUserIdList() {
        if (CollectionUtils.isNotEmpty(userIdList)) {
            for (int i = 0; i < userIdList.size(); i++) {
                String userId = userIdList.get(i);
                if (userId.equals("common#loginuser")) {
                    userIdList.set(i, UserContext.get().getUserUuid());
                } else if (userId.contains("#")) {
                    userIdList.set(i, userId.substring(userId.indexOf("#") + 1));
                }
            }
        }
        return userIdList;
    }
}
