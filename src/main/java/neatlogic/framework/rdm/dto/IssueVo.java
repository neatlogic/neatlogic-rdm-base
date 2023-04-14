/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package neatlogic.framework.rdm.dto;

import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.asynchronization.threadlocal.TenantContext;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.fulltextindex.utils.FullTextIndexUtil;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class IssueVo extends BasePageVo {
    @JSONField(serialize = false)//作为搜索条件使用
    private List<Long> idList;
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "目录id", type = ApiParamType.LONG)
    private Long catalog;
    @JSONField(serialize = false)
    private Integer catalogLft;
    @JSONField(serialize = false)
    private Integer catalogRht;
    @EntityField(name = "目录名称", type = ApiParamType.STRING)
    private String catalogName;
    @EntityField(name = "应用id", type = ApiParamType.LONG)
    private Long appId;
    @EntityField(name = "项目id", type = ApiParamType.LONG)
    private Long projectId;
    @EntityField(name = "创建者", type = ApiParamType.STRING)
    private String createUser;
    @EntityField(name = "创建者名称", type = ApiParamType.STRING)
    private String createUserName;
    @EntityField(name = "创建日期", type = ApiParamType.LONG)
    private Date createDate;
    @EntityField(name = "优先级", type = ApiParamType.LONG)
    private Long priority;
    @EntityField(name = "优先级名称", type = ApiParamType.STRING)
    private String priorityName;
    @EntityField(name = "状态", type = ApiParamType.LONG)
    private Long status;
    @EntityField(name = "状态颜色", type = ApiParamType.STRING)
    private String statusColor;
    @EntityField(name = "状态名称", type = ApiParamType.STRING)
    private String statusName;
    @EntityField(name = "内容", type = ApiParamType.STRING)
    private String content;
    @EntityField(name = "用户id列表", type = ApiParamType.JSONARRAY)
    private List<String> userIdList;
    @EntityField(name = "用户信息列表", type = ApiParamType.JSONARRAY)
    private List<UserVo> userList;
    @EntityField(name = "标签列表", type = ApiParamType.JSONARRAY)
    private List<String> tagList;

    @EntityField(name = "属性列表", type = ApiParamType.JSONARRAY)
    private List<IssueAttrVo> attrList;
    private HashMap<Long, ?> attrMap;

    @JSONField(serialize = false)
    private List<String> startTimeRange;
    @JSONField(serialize = false)
    private List<String> endTimeRange;

    public List<String> getUserIdList() {
        if (userIdList == null && userList != null) {
            userIdList = new ArrayList<>();
            for (UserVo userVo : userList) {
                userIdList.add(GroupSearch.USER.getValuePlugin() + userVo.getUserId());
            }
        }
        return userIdList;
    }

    public boolean hasAttr(IssueAttrVo attrVo) {
        if (CollectionUtils.isNotEmpty(attrList)) {
            return attrList.contains(attrVo);
        }
        return false;
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

    public final Set<String> getWordList() {
        if (StringUtils.isNotBlank(this.getKeyword())) {
            return FullTextIndexUtil.sliceKeyword(this.getKeyword());
        }
        return null;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public IssueAttrVo getAttr(Long attrId) {
        if (CollectionUtils.isNotEmpty(attrList)) {
            Optional<IssueAttrVo> op = attrList.stream().filter(d -> d.getAttrId().equals(attrId)).findFirst();
            if (op.isPresent()) {
                return op.get();
            }
        }
        return null;
    }

    public void addAttr(IssueAttrVo attrVo) {
        if (this.attrList == null) {
            this.attrList = new ArrayList<>();
        }
        if (!attrList.contains(attrVo)) {
            attrList.add(attrVo);
        }
    }

    public List<IssueAttrVo> getAttrList() {
        return attrList;
    }

    public void setAttrList(List<IssueAttrVo> attrList) {
        this.attrList = attrList;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public List<UserVo> getUserList() {
        return userList;
    }

    public void setUserList(List<UserVo> userList) {
        this.userList = userList;
    }

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    @JSONField(serialize = false)
    public String getTableName() {
        return TenantContext.get().getDataDbName() + ".`rdm_app_" + this.getAppId() + "`";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCatalog() {
        return catalog;
    }

    public void setCatalog(Long catalog) {
        this.catalog = catalog;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
