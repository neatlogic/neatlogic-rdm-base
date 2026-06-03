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

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.restful.annotation.EntityField;
import org.apache.commons.lang3.StringUtils;

public class IssueCopyRelVo {
    @EntityField(name = "副本issue id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "副本名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "来源issue id", type = ApiParamType.LONG)
    private Long sourceIssueId;
    @EntityField(name = "应用id", type = ApiParamType.LONG)
    private Long appId;
    @EntityField(name = "应用类型", type = ApiParamType.STRING)
    private String appType;
    @EntityField(name = "应用颜色", type = ApiParamType.STRING)
    private String appColor;
    @EntityField(name = "项目id", type = ApiParamType.LONG)
    private Long projectId;
    @EntityField(name = "状态id", type = ApiParamType.LONG)
    private Long status;
    @EntityField(name = "状态名称", type = ApiParamType.STRING)
    private String statusName;
    @EntityField(name = "状态文本", type = ApiParamType.STRING)
    private String statusLabel;
    @EntityField(name = "状态颜色", type = ApiParamType.STRING)
    private String statusColor;
    @EntityField(name = "关联issue id", type = ApiParamType.LONG)
    private Long relIssueId;
    @EntityField(name = "关联issue名称", type = ApiParamType.STRING)
    private String relIssueName;
    @EntityField(name = "关联应用id", type = ApiParamType.LONG)
    private Long relAppId;
    @EntityField(name = "关联应用类型", type = ApiParamType.STRING)
    private String relAppType;
    @EntityField(name = "关联应用颜色", type = ApiParamType.STRING)
    private String relAppColor;
    @EntityField(name = "关联项目id", type = ApiParamType.LONG)
    private Long relProjectId;
    @EntityField(name = "关联类型", type = ApiParamType.STRING)
    private String relType;
    @EntityField(name = "关联方向", type = ApiParamType.STRING)
    private String direction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSourceIssueId() {
        return sourceIssueId;
    }

    public void setSourceIssueId(Long sourceIssueId) {
        this.sourceIssueId = sourceIssueId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppColor() {
        if (StringUtils.isBlank(appColor) && StringUtils.isNotBlank(appType)) {
            appColor = AppTypeManager.getColor(appType);
        }
        return appColor;
    }

    public void setAppColor(String appColor) {
        this.appColor = appColor;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public Long getRelIssueId() {
        return relIssueId;
    }

    public void setRelIssueId(Long relIssueId) {
        this.relIssueId = relIssueId;
    }

    public String getRelIssueName() {
        return relIssueName;
    }

    public void setRelIssueName(String relIssueName) {
        this.relIssueName = relIssueName;
    }

    public Long getRelAppId() {
        return relAppId;
    }

    public void setRelAppId(Long relAppId) {
        this.relAppId = relAppId;
    }

    public String getRelAppType() {
        return relAppType;
    }

    public void setRelAppType(String relAppType) {
        this.relAppType = relAppType;
    }

    public String getRelAppColor() {
        if (StringUtils.isBlank(relAppColor) && StringUtils.isNotBlank(relAppType)) {
            relAppColor = AppTypeManager.getColor(relAppType);
        }
        return relAppColor;
    }

    public void setRelAppColor(String relAppColor) {
        this.relAppColor = relAppColor;
    }

    public Long getRelProjectId() {
        return relProjectId;
    }

    public void setRelProjectId(Long relProjectId) {
        this.relProjectId = relProjectId;
    }

    public String getRelType() {
        return relType;
    }

    public void setRelType(String relType) {
        this.relType = relType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
