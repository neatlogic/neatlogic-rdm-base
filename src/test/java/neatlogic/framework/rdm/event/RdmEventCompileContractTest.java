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

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** 使用真实 javac 验证泛型发布入口，错误对象必须在编译阶段被拒绝。 */
public class RdmEventCompileContractTest {
    /** 正例、错误载荷反例分别独立编译，不依赖运行时类型检查。 */
    public static void main(String[] args) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) { throw new AssertionError("必须使用 JDK 运行类型契约测试"); }
        Path directory = Files.createTempDirectory("rdm-event-compile-");
        String prefix = "import neatlogic.framework.rdm.event.*; import neatlogic.framework.rdm.dto.IssueVo; ";
        String valid = prefix + "class Valid { void call(RdmEventDefinition<IssueVo> event, IssueVo issue) { RdmEventManager.doEvent(1L,2L,event,issue); } }";
        String invalid = prefix + "class Invalid { void call(RdmEventDefinition<IssueVo> event) { RdmEventManager.doEvent(1L,2L,event,\"wrong object\"); } }";
        check(compile(compiler, directory, "Valid", valid) == 0, "合法对象无法编译");
        check(compile(compiler, directory, "Invalid", invalid) != 0, "错误对象通过了编译");
        System.out.println("RdmEventCompileContractTest passed: 正例成功，错误载荷编译失败");
    }

    /** 在临时目录编译一份独立调用代码。 */
    private static int compile(JavaCompiler compiler, Path directory, String name, String source) throws Exception {
        Path file = directory.resolve(name + ".java");
        Files.write(file, source.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream diagnostics = new ByteArrayOutputStream();
        return compiler.run(null, diagnostics, diagnostics, "-proc:none", "-classpath", System.getProperty("java.class.path"), "-d", directory.toString(), file.toString());
    }

    /** 明确断言失败原因。 */
    private static void check(boolean success, String message) {
        if (!success) { throw new AssertionError(message); }
    }
}
