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

/** 由业务模块提供对象身份、快照和最新状态读取能力。 */
public interface IRdmEventObjectAdapter<T> {
    /** 获取管理对象的精确类型。 */
    Class<T> getObjectClass();
    /** 获取跨应用稳定的对象类型标识。 */
    String getObjectType();
    /** 获取已有对象标识，不得生成新标识。 */
    String getObjectId(T object);
    /** 创建与原对象无可变引用共享的快照。 */
    T snapshot(T object);
    /** 在当前事务读取最新对象，不存在时返回空。 */
    T reload(Long projectId, Long appId, String objectId);
    /** 验证对象确实属于指定项目和应用。 */
    void validateScope(Long projectId, Long appId, T object);
}
