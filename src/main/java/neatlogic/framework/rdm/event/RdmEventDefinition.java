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

package neatlogic.framework.rdm.event;

import neatlogic.framework.rdm.exception.event.RdmEventAppTypesIsEmptyException;
import neatlogic.framework.rdm.exception.event.RdmEventNameIsEmptyException;
import neatlogic.framework.rdm.exception.event.RdmEventObjectClassIsEmptyException;
import neatlogic.framework.rdm.exception.event.RdmEventObjectTypeIsEmptyException;
import neatlogic.framework.util.$;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** 将持久化事件标识与管理对象类型绑定的不可变定义。 */
public final class RdmEventDefinition<T> {
    private final String name;
    private final String label;
    private final String description;
    private final Set<String> appTypes;
    private final String objectType;
    private final Class<T> objectClass;
    private final boolean deleted;

    /** 校验各项定义并抛出对应领域异常；删除事件沿用快照而不重载。 */
    public RdmEventDefinition(String name, String label, String description, Set<String> appTypes,
                              String objectType, Class<T> objectClass, boolean deleted) {
        if (name == null || name.trim().isEmpty()) {
            throw new RdmEventNameIsEmptyException();
        }
        if (appTypes == null || appTypes.isEmpty()) {
            throw new RdmEventAppTypesIsEmptyException();
        }
        if (objectClass == null) {
            throw new RdmEventObjectClassIsEmptyException();
        }
        if (objectType == null || objectType.trim().isEmpty()) {
            throw new RdmEventObjectTypeIsEmptyException();
        }
        this.name = name;
        this.label = label;
        this.description = description;
        this.appTypes = Collections.unmodifiableSet(new HashSet<>(appTypes));
        this.objectType = objectType;
        this.objectClass = objectClass;
        this.deleted = deleted;
    }
    /** 获取持久化事件标识。 */
    public String getName() { return name; }
    /** 获取事件显示名称。 */
    public String getLabel() { return $.t(label); }
    /** 获取事件说明。 */
    public String getDescription() { return $.t(description); }
    /** 获取允许发布事件的应用类型。 */
    public Set<String> getAppTypes() { return appTypes; }
    /** 获取业务对象适配器。 */
    public IRdmEventObjectAdapter<T> getAdapter() { return RdmEventObjectAdapterFactory.getAdapter(objectType, objectClass); }
    /** 获取事件绑定的精确对象类型。 */
    public Class<T> getObjectClass() { return objectClass; }
    /** 获取声明的对象标识，读取元数据时无需等待适配器注册。 */
    public String getObjectType() { return objectType; }
    /** 判断是否使用删除快照。 */
    public boolean isDeleted() { return deleted; }
}
