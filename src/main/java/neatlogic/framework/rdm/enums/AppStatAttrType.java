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

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.common.constvalue.IEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AppStatAttrType implements IEnum<JSONObject> {
    STORY_TOTAL(AppType.STORY.getName(), "story_total", "需求总数", "issue", false),
    STORY_OVERDUE(AppType.STORY.getName(), "story_overdue", "逾期需求总数", "endDate", false),
    STORY_COMPLETE_RATE(AppType.STORY.getName(), "story_complete_rate", "完成率", "status.isEnd", false),
    STORY_HIGH_RISK(AppType.STORY.getName(), "story_high_risk", "高风险需求数", "priority", false),
    STORY_STATUS_DISTRIBUTION(AppType.STORY.getName(), "story_status_distribution", "需求状态比例", "status", false),
    STORY_PRIORITY_DISTRIBUTION(AppType.STORY.getName(), "story_priority_distribution", "优先级分布", "priority", false),
    STORY_TREND(AppType.STORY.getName(), "story_trend", "近7个月需求/逾期趋势", "createDate", false),
    BUG_TOTAL(AppType.BUG.getName(), "bug_total", "缺陷总数", "issue", false),
    BUG_OPEN(AppType.BUG.getName(), "bug_open", "未关闭缺陷数", "status.isEnd", false),
    BUG_OVERDUE(AppType.BUG.getName(), "bug_overdue", "逾期缺陷数", "endDate", false),
    BUG_REOPEN_TOTAL(AppType.BUG.getName(), "bug_reopen_total", "重开缺陷数", "stat:bug_reopen_count", false),
    BUG_STATUS_DISTRIBUTION(AppType.BUG.getName(), "bug_status_distribution", "缺陷状态比例", "status", false),
    BUG_SEVERITY_DISTRIBUTION(AppType.BUG.getName(), "bug_severity_distribution", "严重程度分布", "stat:bug_severity", false),
    BUG_SOURCE_DISTRIBUTION(AppType.BUG.getName(), "bug_source_distribution", "缺陷来源分布", "stat:bug_source", false),
    BUG_TREND(AppType.BUG.getName(), "bug_trend", "近7个月新增/逾期趋势", "createDate", false),
    BUG_SEVERITY(AppType.BUG.getName(), "bug_severity", "严重程度", null, true, AttrType.SELECT.getType(), AttrType.TEXT.getType()),
    BUG_SOURCE(AppType.BUG.getName(), "bug_source", "缺陷来源", null, true, AttrType.SELECT.getType(), AttrType.TEXT.getType()),
    BUG_ENVIRONMENT(AppType.BUG.getName(), "bug_environment", "发现环境", null, true, AttrType.SELECT.getType(), AttrType.TEXT.getType()),
    BUG_FOUND_VERSION(AppType.BUG.getName(), "bug_found_version", "发现版本", null, true, AttrType.SELECT.getType(), AttrType.TEXT.getType()),
    BUG_FIXED_VERSION(AppType.BUG.getName(), "bug_fixed_version", "修复版本", null, true, AttrType.SELECT.getType(), AttrType.TEXT.getType()),
    BUG_ROOT_CAUSE(AppType.BUG.getName(), "bug_root_cause", "根因分类", null, true, AttrType.SELECT.getType(), AttrType.TEXT.getType()),
    BUG_MODULE(AppType.BUG.getName(), "bug_module", "所属模块", null, true, AttrType.SELECT.getType(), AttrType.TEXT.getType()),
    BUG_REOPEN_COUNT(AppType.BUG.getName(), "bug_reopen_count", "重开次数", null, true, AttrType.NUMBER.getType()),
    BUG_RESOLVED_DATE(AppType.BUG.getName(), "bug_resolved_date", "解决时间", null, true, AttrType.DATE.getType(), AttrType.DATETIME.getType()),
    BUG_CLOSED_DATE(AppType.BUG.getName(), "bug_closed_date", "关闭时间", null, true, AttrType.DATE.getType(), AttrType.DATETIME.getType());

    private final String appType;
    private final String value;
    private final String text;
    private final String systemField;
    private final boolean allowCustomAttr;
    private final List<String> attrTypeList;

    AppStatAttrType(String appType, String value, String text, String systemField, boolean allowCustomAttr, String... attrTypeArray) {
        this.appType = appType;
        this.value = value;
        this.text = text;
        this.systemField = systemField;
        this.allowCustomAttr = allowCustomAttr;
        this.attrTypeList = Arrays.asList(attrTypeArray);
    }

    public String getAppType() {
        return appType;
    }

    public String getText() {
        return text;
    }

    public List<String> getAttrTypeList() {
        return attrTypeList;
    }

    public String getSystemField() {
        return systemField;
    }

    public boolean getAllowCustomAttr() {
        return allowCustomAttr;
    }

    public boolean isSupportAttrType(String attrType) {
        return allowCustomAttr && StringUtils.isNotBlank(attrType) && attrTypeList.stream().anyMatch(type -> type.equalsIgnoreCase(attrType));
    }

    public static AppStatAttrType get(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (AppStatAttrType type : values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

    public static List<AppStatAttrType> getListByAppType(String appType) {
        List<AppStatAttrType> list = new ArrayList<>();
        if (StringUtils.isBlank(appType)) {
            return list;
        }
        for (AppStatAttrType type : values()) {
            if (type.getAppType().equalsIgnoreCase(appType)) {
                list.add(type);
            }
        }
        return list;
    }

    public JSONObject toJson() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("value", value);
        jsonObj.put("text", text);
        jsonObj.put("appType", appType);
        jsonObj.put("systemField", systemField);
        jsonObj.put("allowCustomAttr", allowCustomAttr);
        jsonObj.put("attrTypeList", attrTypeList);
        return jsonObj;
    }

    @Override
    public List<JSONObject> getValueTextList() {
        List<JSONObject> list = new ArrayList<>();
        for (AppStatAttrType type : values()) {
            list.add(type.toJson());
        }
        return list;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getEnumName() {
        return "RDM应用统计属性";
    }
}
