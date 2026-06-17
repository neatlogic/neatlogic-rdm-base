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
import neatlogic.framework.restful.annotation.EntityField;

public class WebhookConfigVo {
    @EntityField(name = "nmraa.getappapi.input.param.desc", type = ApiParamType.LONG)
    private Long appId;
    @EntityField(name = "Web URL", type = ApiParamType.STRING)
    private String webhookUrl;
    @EntityField(name = "Webhook URL前缀", type = ApiParamType.STRING)
    private String webhookUrlPrefix;
    @EntityField(name = "完整Webhook URL", type = ApiParamType.STRING)
    private String webhookUrlFull;
    @EntityField(name = "Secret Token", type = ApiParamType.STRING)
    private String secretToken;


    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getWebhookUrlPrefix() {
        return webhookUrlPrefix;
    }

    public void setWebhookUrlPrefix(String webhookUrlPrefix) {
        this.webhookUrlPrefix = webhookUrlPrefix;
    }

    public String getWebhookUrlFull() {
        return webhookUrlFull;
    }

    public void setWebhookUrlFull(String webhookUrlFull) {
        this.webhookUrlFull = webhookUrlFull;
    }

    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }
}
