/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the Sustainable Use License (SUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.rdm.dto;

/** 事件配置页面使用的展示元数据，不暴露内部适配器。 */
public class RdmEventHandlerMetaVo {
    private String name;
    private String label;
    private String description;
    private String icon;
    private Integer sort;
    private Boolean requiresConfigEditor;

    /** 从注册定义构造页面展示字段。 */
    public RdmEventHandlerMetaVo(neatlogic.framework.rdm.event.IRdmEventHandler<?> handler) {
        this.name = handler.getName();
        this.label = handler.getLabel();
        this.description = handler.getDescription();
        this.icon = handler.getIcon();
        this.sort = handler.getSort();
        this.requiresConfigEditor = handler.requiresConfigEditor();
    }

    /** 获取 name。 */
    public String getName() { return name; }
    /** 设置 name。 */
    public void setName(String name) { this.name = name; }

    /** 获取 label。 */
    public String getLabel() { return label; }
    /** 设置 label。 */
    public void setLabel(String label) { this.label = label; }

    /** 获取 description。 */
    public String getDescription() { return description; }
    /** 设置 description。 */
    public void setDescription(String description) { this.description = description; }

    /** 获取 icon。 */
    public String getIcon() { return icon; }
    /** 设置 icon。 */
    public void setIcon(String icon) { this.icon = icon; }

    /** 获取 sort。 */
    public Integer getSort() { return sort; }
    /** 设置 sort。 */
    public void setSort(Integer sort) { this.sort = sort; }

    /** 获取 requiresConfigEditor。 */
    public Boolean getRequiresConfigEditor() { return requiresConfigEditor; }
    /** 设置 requiresConfigEditor。 */
    public void setRequiresConfigEditor(Boolean requiresConfigEditor) { this.requiresConfigEditor = requiresConfigEditor; }
}
