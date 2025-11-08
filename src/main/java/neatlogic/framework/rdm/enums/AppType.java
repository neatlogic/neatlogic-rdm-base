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

package neatlogic.framework.rdm.enums;

import neatlogic.framework.rdm.enums.core.IAppType;
import neatlogic.framework.util.$;

public enum AppType implements IAppType {
    ITERATION("iteration", "common.iteration", "#87CEEB", null, false, 1),
    STORY("story", "common.request", "#1670f0", new AttrType[]{AttrType.ITERATION, AttrType.CATALOG, AttrType.WORKER, AttrType.TAG, AttrType.PRIORITY, AttrType.TIMECOST, AttrType.STARTDATE, AttrType.ENDDATE}, true, 2),
    TASK("task", "common.task", "#25b864", new AttrType[]{AttrType.ITERATION, AttrType.TAG, AttrType.PRIORITY, AttrType.WORKER, AttrType.TIMECOST, AttrType.STARTDATE, AttrType.ENDDATE}, true, 3),
    BUG("bug", "common.bug", "#f33b3b", new AttrType[]{AttrType.ITERATION, AttrType.TAG, AttrType.PRIORITY, AttrType.WORKER, AttrType.TIMECOST, AttrType.STARTDATE, AttrType.ENDDATE}, true, 4);

    //STORYWALL("storywall", "term.rdm.storywall", null, null, false, 5);

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
