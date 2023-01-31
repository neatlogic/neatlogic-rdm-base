/*
 * Copyright(c) 2023 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.rdm.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;

public class ObjectStatusRelVo {
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "上一状态id", type = ApiParamType.LONG)
    private Long fromStatusId;
    @EntityField(name = "下一状态id", type = ApiParamType.LONG)
    private Long toStatusId;
    @EntityField(name = "项目id", type = ApiParamType.LONG)
    private Long objectId;

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromStatusId() {
        return fromStatusId;
    }

    public void setFromStatusId(Long fromStatusId) {
        this.fromStatusId = fromStatusId;
    }

    public Long getToStatusId() {
        return toStatusId;
    }

    public void setToStatusId(Long toStatusId) {
        this.toStatusId = toStatusId;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }
}
