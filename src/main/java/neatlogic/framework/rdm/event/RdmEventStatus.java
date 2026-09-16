/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the Sustainable Use License (SUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.rdm.event;

import neatlogic.framework.util.$;

/** 研发事件通用执行状态。 */
public enum RdmEventStatus {
    RUNNING("running", "执行中"),
    SUCCEED("succeed", "已成功"),
    SKIPPED("skipped", "已跳过"),
    DISABLED("disabled", "已禁用"),
    FAILED("failed", "已失败");

    private final String value;
    private final String text;

    /** 初始化状态值和名称。 */
    RdmEventStatus(String _value, String _text) {
        this.value = _value;
        this.text = _text;
    }

    /** 获取状态值。 */
    public String getValue() {
        return value;
    }

    /** 获取状态名称。 */
    public String getText() {
        return $.t(text);
    }

    /** 按状态值获取名称。 */
    public static String getText(String name) {
        for (RdmEventStatus s : RdmEventStatus.values()) {
            if (s.getValue().equals(name)) {
                return s.getText();
            }
        }
        return "";
    }
}
