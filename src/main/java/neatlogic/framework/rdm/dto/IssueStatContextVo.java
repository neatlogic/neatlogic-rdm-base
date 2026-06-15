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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class IssueStatContextVo implements Serializable {
    private Long projectId;
    private Long appId;
    private Integer catalogLft;
    private Integer catalogRht;
    private String statKey;
    private IssueStatFieldVo statField;
    private final Map<String, Object> cache = new HashMap<>();

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Integer getCatalogLft() {
        return catalogLft;
    }

    public void setCatalogLft(Integer catalogLft) {
        this.catalogLft = catalogLft;
    }

    public Integer getCatalogRht() {
        return catalogRht;
    }

    public void setCatalogRht(Integer catalogRht) {
        this.catalogRht = catalogRht;
    }

    public String getStatKey() {
        return statKey;
    }

    public void setStatKey(String statKey) {
        this.statKey = statKey;
    }

    public IssueStatFieldVo getStatField() {
        return statField;
    }

    public void setStatField(IssueStatFieldVo statField) {
        this.statField = statField;
    }

    public Map<String, Object> getCache() {
        return cache;
    }

    public IssueVo toIssueVo() {
        IssueVo issueVo = new IssueVo();
        issueVo.setProjectId(projectId);
        issueVo.setAppId(appId);
        issueVo.setCatalogLft(catalogLft);
        issueVo.setCatalogRht(catalogRht);
        return issueVo;
    }
}
