/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.rdm.dto;

import codedriver.framework.asynchronization.threadlocal.TenantContext;
import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.rdm.enums.ObjectType;
import codedriver.framework.restful.annotation.EntityField;
import codedriver.framework.util.SnowflakeUtil;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/*
issue包括需求、任务、测试计划、测试用例、缺陷等
 */
public class ObjectVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "项目id", type = ApiParamType.LONG)
    private Long projectId;
    @EntityField(name = "类型", type = ApiParamType.ENUM, member = ObjectVo.class)
    private String type;
    @EntityField(name = "排序", type = ApiParamType.INTEGER)
    private int sort;
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "属性列表", type = ApiParamType.JSONARRAY)
    private List<ObjectAttrVo> attrList;

    @JSONField(serialize = false)
    public String getTableName() {
        return TenantContext.get().getDataDbName() + ".`rdm_object_" + this.getId() + "`";
    }

    public void addObjectAttr(ObjectAttrVo objectAttrVo) {
        if (this.attrList == null) {
            this.attrList = new ArrayList<>();
        }
        if (!this.attrList.contains(objectAttrVo)) {
            this.attrList.add(objectAttrVo);
        }
    }

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public String getName() {
        if (StringUtils.isBlank(name) && StringUtils.isNotBlank(type)) {
            name = ObjectType.getLabel(type);
        }
        return name;
    }

    public List<ObjectAttrVo> getAttrList() {
        return attrList;
    }

    public void setAttrList(List<ObjectAttrVo> attrList) {
        this.attrList = attrList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
