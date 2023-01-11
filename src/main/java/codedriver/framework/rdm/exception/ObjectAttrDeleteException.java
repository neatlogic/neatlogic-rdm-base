/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.rdm.exception;

import codedriver.framework.exception.core.ApiRuntimeException;
import codedriver.framework.rdm.dto.ObjectAttrVo;

public class ObjectAttrDeleteException extends ApiRuntimeException {

    public ObjectAttrDeleteException(ObjectAttrVo objectAttrVo) {
        super("属性“" + objectAttrVo.getLabel() + "”是系统属性，不能删除");
    }
}
