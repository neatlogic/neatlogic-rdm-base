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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.attrhandler.code.AttrHandlerFactory;
import neatlogic.framework.rdm.attrhandler.code.IAttrValueHandler;
import neatlogic.framework.restful.annotation.EntityField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
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
    private List<Object> valueList;
    @EntityField(name = "配置", type = ApiParamType.JSONOBJECT)
    private JSONObject config;
    private boolean isFormat = false;

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
                return this.getValue().equals(attr.getValue());
                /*if (CollectionUtils.isNotEmpty(getValueList()) && CollectionUtils.isNotEmpty(attr.getValueList())) {
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
                }*/
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

    public IssueAttrVo setAttrType(String attrType) {
        this.attrType = attrType;
        return this;
    }


    public IssueAttrVo(Long attrId, Long issueId, String attrType, JSONObject config) {
        this.attrId = attrId;
        this.issueId = issueId;
        this.attrType = attrType;
        this.config = config;
    }

    public JSONObject getConfig() {
        return config;
    }

    public IssueAttrVo setConfig(JSONObject config) {
        this.config = config;
        return this;
    }

    public String getValue() {
        if (CollectionUtils.isNotEmpty(valueList) && StringUtils.isNotBlank(attrType)) {
            JSONArray newValueList = JSONArray.parseArray(JSONArray.toJSONString(valueList));
            newValueList.sort(Comparator.comparing(Object::toString));
            IAttrValueHandler handler = AttrHandlerFactory.getHandler(attrType);
            if (handler != null) {
                for (int i = 0; i < newValueList.size(); i++) {
                    Object value = newValueList.get(i);
                    Object newValue = handler.format(value, this.config);
                    newValueList.set(i, newValue);
                }
                if (handler.getIsArray()) {
                    return newValueList.toString();
                } else {
                    if (newValueList.size() > 1) {
                        return newValueList.toString();
                    } else if (newValueList.size() == 1) {
                        return newValueList.getString(0);
                    }
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

    public List<Object> getValueList() {
        return valueList;
    }

    public void format() {
        if (!this.isFormat && StringUtils.isNotBlank(this.attrType)) {
            IAttrValueHandler handler = AttrHandlerFactory.getHandler(this.attrType);
            if (handler != null) {
                List<Object> newValueList = new ArrayList<>();
                for (Object o : valueList) {
                    newValueList.add(handler.format(o, this.config));
                }
                this.valueList = newValueList;
                this.isFormat = true;
            }
        }
    }

    public void setValueList(List<Object> valueList) {
        this.valueList = valueList;
    }
}
