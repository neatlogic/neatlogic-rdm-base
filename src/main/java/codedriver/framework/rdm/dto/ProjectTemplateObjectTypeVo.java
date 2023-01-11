/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.rdm.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.rdm.enums.ObjectType;
import codedriver.framework.restful.annotation.EntityField;
import org.apache.commons.lang3.StringUtils;

public class ProjectTemplateObjectTypeVo {
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String label;
    @EntityField(name = "唯一标识", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "排序", type = ApiParamType.INTEGER)
    private Integer sort;

    public String getLabel() {
        if (StringUtils.isBlank(label) && StringUtils.isNotBlank(name)) {
            label = ObjectType.getLabel(name);
        }
        return label;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
