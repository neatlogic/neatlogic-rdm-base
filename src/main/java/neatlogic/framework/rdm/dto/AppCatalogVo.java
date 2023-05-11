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

import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;

import java.util.ArrayList;
import java.util.List;

public class AppCatalogVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "应用id", type = ApiParamType.LONG)
    private Long appId;
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "父节点id", type = ApiParamType.LONG)
    private Long parentId;
    @EntityField(name = "左编码", type = ApiParamType.INTEGER)
    private Integer lft;
    @EntityField(name = "右编码", type = ApiParamType.INTEGER)
    private Integer rht;
    @EntityField(name = "是否展开", type = ApiParamType.BOOLEAN)
    private boolean expand;
    @EntityField(name = "是否被关联", type = ApiParamType.INTEGER)
    private int isInUsed;
    @JSONField(serialize = false)//是否包含父节点
    private Boolean hasParent;
    @JSONField(serialize = false)//是否需要计算是否被关联
    private Boolean needCheckIsInUsed;


    public void setHasParent(Boolean hasParent) {
        this.hasParent = hasParent;
    }

    public Boolean getHasParent() {
        return hasParent;
    }


    private List<AppCatalogVo> children;

    public Boolean getNeedCheckIsInUsed() {
        return needCheckIsInUsed;
    }

    public void setNeedCheckIsInUsed(Boolean needCheckIsInUsed) {
        this.needCheckIsInUsed = needCheckIsInUsed;
    }

    public void addChild(AppCatalogVo appCatalogVo) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(appCatalogVo);
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public int getIsInUsed() {
        return isInUsed;
    }

    public void setIsInUsed(int isInUsed) {
        this.isInUsed = isInUsed;
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

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public List<AppCatalogVo> getChildren() {
        return children;
    }

    public void setChildren(List<AppCatalogVo> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getLft() {
        return lft;
    }

    public void setLft(Integer lft) {
        this.lft = lft;
    }

    public Integer getRht() {
        return rht;
    }

    public void setRht(Integer rht) {
        this.rht = rht;
    }
}
