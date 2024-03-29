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

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.rdm.enums.IssueRelDirection;
import neatlogic.framework.restful.annotation.EntityField;

import java.util.Objects;

public class IssueRelVo extends BasePageVo {
    @EntityField(name = "来源应用id", type = ApiParamType.LONG)
    private Long fromAppId;
    @EntityField(name = "来源任务id", type = ApiParamType.LONG)
    private Long fromIssueId;
    @EntityField(name = "来源任务名称", type = ApiParamType.STRING)
    private String fromIssueName;
    @EntityField(name = "目标任务名称", type = ApiParamType.STRING)
    private String toIssueName;
    @EntityField(name = "目标应用id", type = ApiParamType.LONG)
    private Long toAppId;
    @EntityField(name = "目标任务id", type = ApiParamType.LONG)
    private Long toIssueId;
    @EntityField(name = "来源应用类型", type = ApiParamType.STRING)
    private String fromAppType;
    @EntityField(name = "目标应用类型", type = ApiParamType.STRING)
    private String toAppType;
    @EntityField(name = "关联方向", type = ApiParamType.ENUM, member = IssueRelDirection.class)
    private String direction;
    @EntityField(name = "关联类型", type = ApiParamType.STRING)
    private String relType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssueRelVo that = (IssueRelVo) o;
        return Objects.equals(fromIssueId, that.fromIssueId) && Objects.equals(toIssueId, that.toIssueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromIssueId, toIssueId);
    }

    public IssueRelVo() {

    }

    public IssueRelVo(Long fromAppId, Long fromIssueId, Long toAppId, Long toIssueId, String relType) {
        this.fromAppId = fromAppId;
        this.fromIssueId = fromIssueId;
        this.toAppId = toAppId;
        this.toIssueId = toIssueId;
        this.relType = relType;
    }

    public String getFromIssueName() {
        return fromIssueName;
    }

    public void setFromIssueName(String fromIssueName) {
        this.fromIssueName = fromIssueName;
    }

    public String getToIssueName() {
        return toIssueName;
    }

    public void setToIssueName(String toIssueName) {
        this.toIssueName = toIssueName;
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

    public String getFromAppType() {
        return fromAppType;
    }

    public void setFromAppType(String fromAppType) {
        this.fromAppType = fromAppType;
    }

    public String getToAppType() {
        return toAppType;
    }

    public void setToAppType(String toAppType) {
        this.toAppType = toAppType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getRelType() {
        return relType;
    }

    public void setRelType(String relType) {
        this.relType = relType;
    }
}
