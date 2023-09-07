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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.ValueTextVo;
import neatlogic.framework.matrix.constvalue.SearchExpression;
import neatlogic.framework.rdm.attrhandler.code.AttrHandlerFactory;
import neatlogic.framework.rdm.attrhandler.code.IAttrValueHandler;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.Md5Util;
import neatlogic.framework.util.SnowflakeUtil;
import neatlogic.framework.util.UuidUtil;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppAttrVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "nmraa.getappapi.input.param.desc", type = ApiParamType.LONG)
    private Long appId;
    @EntityField(name = "common.type", type = ApiParamType.STRING)
    private String type;
    @EntityField(name = "common.typename", type = ApiParamType.STRING)
    private String typeText;
    @EntityField(name = "common.uniquename", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "common.name", type = ApiParamType.STRING)
    private String label;
    @EntityField(name = "common.description", type = ApiParamType.STRING)
    private String description;
    @EntityField(name = "common.isrequired", type = ApiParamType.INTEGER)
    private Integer isRequired;
    @EntityField(name = "common.sort", type = ApiParamType.INTEGER)
    private int sort;
    @EntityField(name = "term.rdm.isprivateattr", type = ApiParamType.INTEGER)
    private Integer isPrivate;
    @EntityField(name = "common.config", type = ApiParamType.JSONOBJECT)
    private JSONObject config;
    @EntityField(name = "common.isactive", type = ApiParamType.INTEGER)
    private Integer isActive;
    @JSONField(serialize = false)
    private String configStr;
    @EntityField(name = "term.rdm.attrbelong", type = ApiParamType.STRING)
    private String appType;
    @EntityField(name = "uuid", type = ApiParamType.STRING)
    private String uuid;
    @EntityField(name = "term.rdm.allowimport", type = ApiParamType.BOOLEAN)
    private boolean allowImport;
    @EntityField(name = "term.rdm.allowsearch", type = ApiParamType.BOOLEAN)
    private boolean allowSearch;
    @EntityField(name = "term.rdm.allowsort", type = ApiParamType.BOOLEAN)
    private boolean allowSort;
    @EntityField(name = "nfrd.appattrvo.entityfield.expressionlist", type = ApiParamType.JSONARRAY)
    private List<ValueTextVo> expressionList;

    @JSONField(serialize = false)
    public String getTableName() {
        return TenantContext.get().getDataDbName() + ".`rdm_app_" + this.getAppId() + "`";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppAttrVo that = (AppAttrVo) o;
        return getId().equals(that.getId());
    }


    public List<ValueTextVo> getExpressionList() {
        if (CollectionUtils.isEmpty(this.expressionList) && StringUtils.isNotBlank(this.type)) {
            expressionList = new ArrayList<>();
            IAttrValueHandler handler = AttrHandlerFactory.getHandler(type);
            if (handler != null) {
                for (SearchExpression expression : handler.getSupportExpression()) {
                    expressionList.add(new ValueTextVo(expression.getExpression(), expression.getText()));
                }
            }
        }
        return expressionList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getUuid() {
        if (StringUtils.isBlank(uuid)) {
            if (id != null) {
                uuid = Md5Util.encryptMD5(id.toString());
            } else {
                uuid = UuidUtil.randomUuid();
            }
        }
        return uuid;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTypeText() {
        if (StringUtils.isNotBlank(type) && StringUtils.isBlank(typeText)) {
            IAttrValueHandler handler = AttrHandlerFactory.getHandler(type);
            if (handler != null) {
                typeText = handler.getLabel();
            }
        }
        return typeText;
    }

    public String getImportHelp() {
        if (StringUtils.isNotBlank(type)) {
            IAttrValueHandler handler = AttrHandlerFactory.getHandler(type);
            if (handler != null) {
                return handler.getImportHelp();
            }
        }
        return null;
    }

    public boolean getAllowSearch() {
        if (StringUtils.isNotBlank(type)) {
            IAttrValueHandler handler = AttrHandlerFactory.getHandler(type);
            if (handler != null) {
                return handler.getAllowSearch();
            }
        }
        return false;
    }

    public boolean getAllowSort() {
        if (StringUtils.isNotBlank(type)) {
            IAttrValueHandler handler = AttrHandlerFactory.getHandler(type);
            if (handler != null) {
                return handler.getAllowSort();
            }
        }
        return false;
    }


    public boolean getAllowImport() {
        if (StringUtils.isNotBlank(type)) {
            IAttrValueHandler handler = AttrHandlerFactory.getHandler(type);
            if (handler != null) {
                return handler.getAllowImport();
            }
        }
        return false;
    }

    public String getAppType() {
        if (StringUtils.isNotBlank(type) && StringUtils.isBlank(appType)) {
            IAttrValueHandler handler = AttrHandlerFactory.getHandler(type);
            if (handler != null) {
                appType = handler.getBelong();
            }
        }
        return appType;
    }


    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Integer isPrivate) {
        this.isPrivate = isPrivate;
    }

    public JSONObject getConfig() {
        if (MapUtils.isEmpty(config) && StringUtils.isNotBlank(configStr)) {
            try {
                config = JSONObject.parseObject(configStr);
            } catch (Exception ignored) {

            }
        }
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }

    public String getConfigStr() {
        if (MapUtils.isNotEmpty(config)) {
            configStr = config.toJSONString();
        }
        return configStr;
    }

    public void setConfigStr(String configStr) {
        this.configStr = configStr;
    }
}
