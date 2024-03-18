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
