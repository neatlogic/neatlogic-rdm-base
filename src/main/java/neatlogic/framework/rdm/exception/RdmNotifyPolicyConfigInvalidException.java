/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.framework.rdm.exception;

import neatlogic.framework.exception.core.ApiRuntimeException;

/**
 * RDM通知策略配置不合法。
 */
public class RdmNotifyPolicyConfigInvalidException extends ApiRuntimeException {
    public RdmNotifyPolicyConfigInvalidException(String message) {
        super(message);
    }
}
