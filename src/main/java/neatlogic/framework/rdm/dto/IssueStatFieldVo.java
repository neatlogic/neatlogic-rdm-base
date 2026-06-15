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

package neatlogic.framework.rdm.dto;

import neatlogic.framework.rdm.enums.AppStatAttrType;

import java.io.Serializable;

public class IssueStatFieldVo implements Serializable {
    private String statKey;
    private AppStatAttrType statAttrType;
    private String sourceType;
    private String systemField;
    private AppAttrVo appAttr;

    public String getStatKey() {
        return statKey;
    }

    public void setStatKey(String statKey) {
        this.statKey = statKey;
    }

    public AppStatAttrType getStatAttrType() {
        return statAttrType;
    }

    public void setStatAttrType(AppStatAttrType statAttrType) {
        this.statAttrType = statAttrType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSystemField() {
        return systemField;
    }

    public void setSystemField(String systemField) {
        this.systemField = systemField;
    }

    public AppAttrVo getAppAttr() {
        return appAttr;
    }

    public void setAppAttr(AppAttrVo appAttr) {
        this.appAttr = appAttr;
    }
}
