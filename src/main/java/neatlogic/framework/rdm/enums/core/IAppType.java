/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.rdm.enums.core;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.common.constvalue.IEnum;
import neatlogic.framework.rdm.enums.AttrType;

import java.util.ArrayList;
import java.util.List;

public interface IAppType extends IEnum<JSONObject> {
    String getName();

    boolean getHasIssue();

    String getLabel();

    String getColor();

    AttrType[] getAttrList();

    IAppType[] getAppType();

    int getSort();

    default String getValue() {
        return this.getName();
    }

    default boolean isValid() {
        return true;
    }

    @Override
    default List<JSONObject> getValueTextList() {
        List<JSONObject> array = new ArrayList<>();
        for (IAppType appType : this.getAppType()) {
            array.add(new JSONObject() {
                {
                    this.put("value", appType.getName());
                    this.put("text", appType.getLabel());
                }
            });
        }
        return array;
    }


    @Override
    default String getEnumName() {
        return "nfre.apptype.getenumname";
    }
}
