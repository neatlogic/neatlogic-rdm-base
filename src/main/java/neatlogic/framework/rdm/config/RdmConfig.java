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

package neatlogic.framework.rdm.config;

import neatlogic.framework.common.config.IConfigListener;
import java.util.Properties;

/** 通过框架配置监听机制加载研发模块参数。 */
public class RdmConfig implements IConfigListener {
    private static final int DEFAULT_EVENT_THREAD_COUNT = 4;
    private static int RDM_EVENT_THREAD_COUNT = DEFAULT_EVENT_THREAD_COUNT;

    /** 获取事件执行线程数，配置加载前使用默认值。 */
    public static int RDM_EVENT_THREAD_COUNT() {
        return RDM_EVENT_THREAD_COUNT;
    }

    /** 加载事件线程数，空值、非数字及非正数均回退默认值。 */
    @Override
    public void loadConfig(Properties prop) {
        int threadCount = DEFAULT_EVENT_THREAD_COUNT;
        String value = prop.getProperty("rdm.events.thread.count");
        if (value != null) {
            try {
                int configuredCount = Integer.parseInt(value.trim());
                if (configuredCount > 0) {
                    threadCount = configuredCount;
                }
            } catch (NumberFormatException ignored) {
                // 非法配置不能令事件队列失去工作线程。
            }
        }
        RDM_EVENT_THREAD_COUNT = threadCount;
    }
}
