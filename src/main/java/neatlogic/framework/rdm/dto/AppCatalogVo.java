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
