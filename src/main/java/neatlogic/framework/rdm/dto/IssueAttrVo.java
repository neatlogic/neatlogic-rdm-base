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
import neatlogic.framework.util.HtmlUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

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
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof IssueAttrVo)) {
            return false;
        }
        final IssueAttrVo attr = (IssueAttrVo) other;
        try {
            if (getAttrId().equals(attr.getAttrId())) {
                if (CollectionUtils.isNotEmpty(getValueList()) && CollectionUtils.isNotEmpty(attr.getValueList())) {
                    if (this.getValueList().size() == attr.getValueList().size()) {
                        for (int i = 0; i < this.getValueList().size(); i++) {
                            boolean isExists = false;
                            String v = this.getValueList().getString(i);
                            for (int j = 0; j < attr.getValueList().size(); j++) {
                                String v2 = attr.getValueList().getString(j);
                                switch (this.getAttrType()) {
                                    case "number":
                                        try {
                                            if (Double.parseDouble(v) == Double.parseDouble(v2)) {
                                                isExists = true;
                                                break;
                                            }
                                        } catch (Exception ignored) {

                                        }
                                        break;
                                    case "date": {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        try {
                                            if (sdf.parse(v).equals(sdf.parse(v2))) {
                                                isExists = true;
                                                break;
                                            }
                                        } catch (Exception ignored) {

                                        }
                                        break;
                                    }
                                    case "datetime": {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        try {
                                            if (sdf.parse(v).equals(sdf.parse(v2))) {
                                                isExists = true;
                                                break;
                                            }
                                        } catch (Exception ignored) {

                                        }
                                        break;
                                    }
                                    case "time": {
                                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                        try {
                                            if (sdf.parse(v).equals(sdf.parse(v2))) {
                                                isExists = true;
                                                break;
                                            }
                                        } catch (Exception ignored) {

                                        }
                                        break;
                                    }
                                    default:
                                        if (v.equalsIgnoreCase(v2)) {
                                            isExists = true;
                                        } else if (HtmlUtil.encodeHtml(v).equalsIgnoreCase(v2)) {
                                            isExists = true;
                                        }
                                        break;
                                }
                            }
                            if (!isExists) {
                                return false;
                            }
                        }
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return CollectionUtils.isEmpty(this.getValueList())
                            && CollectionUtils.isEmpty(attr.getValueList());
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        String key = "";
        if (getAttrId() != null) {
            key += getAttrId() + "_";
        }
        if (CollectionUtils.isNotEmpty(getValueList())) {
            key += getValueList().size() + "_";
            // 根据内容排序生成新数组
            List<String> sortedList = getValueList().stream().map(Object::toString).sorted().collect(Collectors.toList());
            key += String.join(",", sortedList);
        }
        return key.hashCode();
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
