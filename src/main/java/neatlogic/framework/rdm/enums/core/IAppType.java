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
