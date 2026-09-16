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

import neatlogic.framework.rdm.dto.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static neatlogic.framework.rdm.event.RdmEventTestSupport.*;

/** 验证事件内顺序及插件返回对象传递。 */
public class RdmEventSerialTest {
    /** 相同排序号不丢失插件，失败停止当前事件。 */
    public static void main(String[] args) throws Exception {
        initMessages();
        RdmEventObjectAdapterFactory.register(ADAPTER);
        RdmEventRegistry.register(EVENT);
        IssueVo empty = new IssueVo();
        check(!empty.hasId(), "检查标识生成了新 ID");
        List<Long> executed = new ArrayList<>();
        IssueVo initial = issue(1L);
        IssueVo replacement = issue(1L);
        register(new Handler() {
            protected IssueVo myTrigger(RdmEventHandlerVo config, RdmEventPluginVo plugin, IssueVo current, RdmEventAuditVo audit, RdmEventStatusVo status) {
                executed.add(config.getId());
                if (config.getId() == 1L) {
                    check(current == initial, "首插件未收到原对象");
                    return replacement;
                }
                check(current == replacement, "没有传递上一插件返回对象");
                if (config.getId() == 3L) { throw new IllegalStateException("预期失败"); }
                return current;
            }
        });
        List<RdmEventHandlerVo> input = Arrays.asList(config(2L, 1, "test"), config(1L, 1, "test"));
        check(RdmEventManager.executeHandlers(input, EVENT, initial) == replacement, "最终结果对象不正确");
        check(executed.equals(Arrays.asList(1L, 2L)), "相同排序号执行顺序不正确");
        check(input.get(0).getId() == 2L, "排序修改了输入列表");
        executed.clear();
        try {
            RdmEventManager.executeHandlers(Arrays.asList(config(1L, 0, "test"), config(3L, 1, "test"), config(4L, 2, "test")), EVENT, initial);
            throw new AssertionError("业务异常未传播");
        } catch (IllegalStateException expected) {
            check(executed.equals(Arrays.asList(1L, 3L)), "失败后继续执行当前事件");
        }
        System.out.println("RdmEventSerialTest passed: stable ordering, object handoff, stop on failure");
    }
}
