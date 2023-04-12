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

import com.alibaba.fastjson.JSONArray;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.enums.AttrType;
import neatlogic.framework.restful.annotation.EntityField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class IssueAttrVo {
    @EntityField(name = "属性id", type = ApiParamType.LONG)
    private Long attrId;
    @EntityField(name = "属性类型", type = ApiParamType.STRING)
    private String attrType;
    @EntityField(name = "任务id", type = ApiParamType.LONG)
    private Long issueId;
    @EntityField(name = "属性值", type = ApiParamType.JSONARRAY)
    private JSONArray valueList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssueAttrVo that = (IssueAttrVo) o;
        return Objects.equals(attrId, that.attrId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attrId);
    }

    public IssueAttrVo() {

    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    public IssueAttrVo(Long attrId, String attrType) {
        this.attrId = attrId;
        this.attrType = attrType;
    }

    public IssueAttrVo(Long attrId, Long issueId, String attrType) {
        this.attrId = attrId;
        this.issueId = issueId;
        this.attrType = attrType;
    }

    public String getValue() {
        if (CollectionUtils.isNotEmpty(valueList) && StringUtils.isNotBlank(attrType)) {
            if (AttrType.getIsArray(attrType)) {
                return valueList.toString();
            } else {
                if (valueList.size() > 1) {
                    return valueList.toString();
                } else if (valueList.size() == 1) {
                    return valueList.getString(0);
                }
            }
        }
        return null;
    }

    public Long getAttrId() {
        return attrId;
    }

    public void setAttrId(Long attrId) {
        this.attrId = attrId;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public JSONArray getValueList() {
        return valueList;
    }

    public void setValueList(JSONArray valueList) {
        this.valueList = valueList;
    }
}
