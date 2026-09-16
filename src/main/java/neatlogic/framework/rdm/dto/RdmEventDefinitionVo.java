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
public class RdmEventDefinitionVo {
    private String name;
    private String label;
    private String description;
    private String objectType;
    private Integer handlerCount;

    /** 从注册定义构造页面展示字段。 */
    public RdmEventDefinitionVo(neatlogic.framework.rdm.event.RdmEventDefinition<?> event) {
        this.name = event.getName();
        this.label = event.getLabel();
        this.description = event.getDescription();
        this.objectType = event.getObjectType();
        this.handlerCount = 0;
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

    /** 获取 objectType。 */
    public String getObjectType() { return objectType; }
    /** 设置 objectType。 */
    public void setObjectType(String objectType) { this.objectType = objectType; }

    /** 获取 handlerCount。 */
    public Integer getHandlerCount() { return handlerCount; }
    /** 设置 handlerCount。 */
    public void setHandlerCount(Integer handlerCount) { this.handlerCount = handlerCount; }
}
