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

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.common.constvalue.IEnum;
import neatlogic.framework.util.$;

import java.util.ArrayList;
import java.util.List;

public enum AttrType implements IEnum<JSONObject> {
    NUMBER("number", "number", "common.number", false, false, null, "请输入合法数字"),
    TEXT("text", "text", "common.text", false, false, null, "请输入合法文本"),
    TEXTAREA("textarea", "textarea", "common.textbox", false, false, null, "请输入合法文本", true, true, false),
    SELECT("select", "select", "common.dropdownlist", false, true, null, "请输入合法选项，多个选项用,分隔"),
    DATE("date", "date", "common.date", false, false, null, "请输入日期，格式:yyyy-mm-dd"),
    DATETIME("datetime", "datetime", "common.datetime", false, false, null, "请输入日期时间"),
    TIME("time", "time", "common.time", false, false, null, "请输入时间"),
    FILE("file", "file", "common.attachment", false, true, null, null, false, false, false),
    PRIORITY("priority", "priority", "common.priority", true, false, null, "请输入优先级名称"),
    UESR("user", "user", "common.user", false, true, null, "请输入用户账号，多个账号用,分隔"),
    TAG("tag", "tagList", "common.tag", true, true, null, "请输入标签，多个标签用,分隔"),
    WORKER("worker", "userIdList", "common.worker", true, true, null, "请输入用户账号，多个账号用,分隔"),
    CATALOG("catalog", "catalog", "common.catalog", true, false, null, "请输入目录全路径，例如xx/yy/zz"),
    ITERATION("iteration", "iteration", "common.iteration", true, false, "iteration", "请输入迭代名称"),
    ENDDATE("enddate", "endDate", "term.rdm.enddate", true, false, null, "请输入日期，格式:yyyy-mm-dd"),
    STARTDATE("startdate", "startDate", "term.rdm.startdate", true, false, null, "请输入日期，格式:yyyy-mm-dd"),

    TIMECOST("timecost", "timecost", "term.rdm.plantimecost", true, false, null, "请输入正整数");

    private final String name;
    //private final String label;
    private final String type;
    private final String label;

    private final boolean isPrivate;

    private final boolean isArray;

    private final String belong;

    private final String importHelp;

    private boolean allowImport = true;
    private boolean allowSearch = true;

    private boolean allowSort = true;

    AttrType(String type, String name, String label, Boolean isPrivate, Boolean isArray, String belong, String importHelp, boolean allowImport, boolean allowSearch, boolean allowSort) {
        this.type = type;
        this.name = name;
        this.label = label;
        this.isPrivate = isPrivate;
        this.isArray = isArray;
        this.belong = belong;
        this.importHelp = importHelp;
        this.allowImport = allowImport;
        this.allowSearch = allowSearch;
        this.allowSort = allowSort;
    }


    AttrType(String type, String name, String label, Boolean isPrivate, Boolean isArray, String belong, String importHelp) {
        this.type = type;
        this.name = name;
        this.label = label;
        this.isPrivate = isPrivate;
        this.isArray = isArray;
        this.belong = belong;
        this.importHelp = importHelp;
    }


    public String getType() {
        return type;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return $.t(label);
    }

    public boolean isArray() {
        return isArray;
    }

    public String getImportHelp() {
        return importHelp;
    }

    public boolean getAllowImport() {
        return allowImport;
    }


    public boolean getAllowSearch() {
        return allowSearch;
    }

    public boolean getAllowSort() {
        return allowSort;
    }

    public static boolean getAllowImport(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getType().equalsIgnoreCase(name)) {
                return s.getAllowImport();
            }
        }
        return false;
    }

    public static boolean getAllowSort(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getType().equalsIgnoreCase(name)) {
                return s.getAllowSort();
            }
        }
        return false;
    }

    public static boolean getAllowSearch(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getType().equalsIgnoreCase(name)) {
                return s.getAllowSearch();
            }
        }
        return false;
    }

    public static String getLabel(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getType().equalsIgnoreCase(name)) {
                return s.getLabel();
            }
        }
        return "";
    }

    public static String getImportHelp(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getType().equalsIgnoreCase(name)) {
                return s.getImportHelp();
            }
        }
        return null;
    }

    public static boolean getIsArray(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getType().equalsIgnoreCase(name)) {
                return s.isArray();
            }
        }
        return false;
    }

    public static AttrType get(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getType().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public List<JSONObject> getValueTextList() {
        List<JSONObject> resultList = new ArrayList<>();
        for (AttrType e : values()) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("value", e.getName());
            jsonObj.put("text", e.getLabel());
            resultList.add(jsonObj);
        }
        return resultList;
    }

    public String getBelong() {
        return belong;
    }

    public static String getBelong(String name) {
        for (AttrType s : AttrType.values()) {
            if (s.getType().equals(name)) {
                return s.getBelong();
            }
        }
        return null;
    }

    @Override
    public String getValue() {
        return name;
    }

    @Override
    public String getEnumName() {
        return "项目对象属性类型";
    }
}
