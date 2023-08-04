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

package neatlogic.framework.rdm.enums;

import neatlogic.framework.rdm.enums.core.IAppType;
import neatlogic.framework.util.$;

public enum AppType implements IAppType {
    ITERATION("iteration", "common.iteration", "#87CEEB", null, false, 1),
    STORY("story", "common.request", "#1670f0", new AttrType[]{AttrType.ITERATION, AttrType.CATALOG, AttrType.WORKER, AttrType.TAG, AttrType.PRIORITY, AttrType.TIMECOST, AttrType.STARTDATE, AttrType.ENDDATE}, true, 2),
    TASK("task", "common.task", "#25b864", new AttrType[]{AttrType.ITERATION, AttrType.TAG, AttrType.PRIORITY, AttrType.WORKER, AttrType.TIMECOST, AttrType.STARTDATE, AttrType.ENDDATE}, true, 3),
    BUG("bug", "common.bug", "#f33b3b", new AttrType[]{AttrType.ITERATION, AttrType.TAG, AttrType.PRIORITY, AttrType.WORKER, AttrType.TIMECOST, AttrType.STARTDATE, AttrType.ENDDATE}, true, 4);

    private final String name;
    private final String label;
    private final String color;
    private final AttrType[] attrList;

    private final int sort;

    private final boolean hasIssue;


    AppType(String name, String label, String color, AttrType[] attrList, Boolean hasIssue, int sort) {
        this.name = name;
        this.label = label;
        this.color = color;
        this.attrList = attrList;
        this.hasIssue = hasIssue;
        this.sort = sort;
    }


    public String getName() {
        return name;
    }

    public boolean getHasIssue() {
        return hasIssue;
    }

    public String getLabel() {
        return $.t(label);
    }

    public String getColor() {
        return color;
    }

    public AttrType[] getAttrList() {
        return attrList;
    }

    @Override
    public IAppType[] getAppType() {
        return AppType.values();
    }

    @Override
    public int getSort() {
        return sort;
    }

}
