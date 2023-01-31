/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.rdm.exception;

import codedriver.framework.exception.core.ApiRuntimeException;

public class DeleteAttrSchemaException extends ApiRuntimeException {
    public DeleteAttrSchemaException(String attrName) {
        super("无法删除属性“" + attrName + "”，具体错误请查看系统日志");
    }
}
