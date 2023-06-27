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
import neatlogic.framework.file.dto.FileVo;
import neatlogic.framework.fulltextindex.utils.FullTextIndexUtil;
import neatlogic.framework.rdm.enums.core.AppTypeManager;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import neatlogic.framework.util.TimeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class IssueVo extends BasePageVo {
    @JSONField(serialize = false)//作为搜索条件使用
    private List<Long> idList;
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "nmrai.searchissueapi.input.param.desc.fromid", type = ApiParamType.LONG)
    private Long fromId;
    @EntityField(name = "nmrai.searchissueapi.input.param.desc.toid", type = ApiParamType.LONG)
    private Long toId;
    @EntityField(name = "term.rdm.parenttaskid", type = ApiParamType.LONG)
    private Long parentId;
    @EntityField(name = "common.name", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "common.catalogid", type = ApiParamType.LONG)
    private Long catalog;
    @EntityField(name = "term.rdm.iterationid", type = ApiParamType.LONG)
    private Long iteration;

    @EntityField(name = "term.rdm.iterationname", type = ApiParamType.STRING)
    private String iterationName;
    @JSONField(serialize = false)//搜索条件专用，显示模式
    private String mode;
    @JSONField(serialize = false)
    private Integer catalogLft;
    @JSONField(serialize = false)
    private Integer catalogRht;
    @EntityField(name = "common.catalogname", type = ApiParamType.STRING)
    private String catalogName;
    @EntityField(name = "nmraa.getappapi.input.param.desc", type = ApiParamType.LONG)
    private Long appId;

    @EntityField(name = "term.rdm.apptype", type = ApiParamType.LONG)
    private String appType;

    @EntityField(name = "term.rdm.appcolor", type = ApiParamType.LONG)
    private String appColor;
    @EntityField(name = "term.rdm.projectid", type = ApiParamType.LONG)
    private Long projectId;
    @EntityField(name = "common.createuser", type = ApiParamType.STRING)
    private String createUser;
    @EntityField(name = "common.createusername", type = ApiParamType.STRING)
    private String createUserName;
    @EntityField(name = "common.createdate", type = ApiParamType.LONG)
    private Date createDate;
    @EntityField(name = "common.priority", type = ApiParamType.LONG)
    private Long priority;

    @EntityField(name = "common.priorityname", type = ApiParamType.STRING)
    private String priorityName;

    @EntityField(name = "common.prioritycolor", type = ApiParamType.STRING)
    private String priorityColor;
    @EntityField(name = "common.commentcount", type = ApiParamType.INTEGER)
    private int commentCount;

    @EntityField(name = "common.status", type = ApiParamType.LONG)
    private Long status;
    @EntityField(name = "common.statuscolor", type = ApiParamType.STRING)
    private String statusColor;
    @EntityField(name = "common.statusuniquename", type = ApiParamType.STRING)
    private String statusName;
    @EntityField(name = "common.comment", type = ApiParamType.STRING)
    private String comment;
    @EntityField(name = "term.rdm.childrentaskcount", type = ApiParamType.INTEGER)
    private int childrenCount;
    @EntityField(name = "term.rdm.startdate", type = ApiParamType.STRING)
    private String startDate;
    @EntityField(name = "term.rdm.enddate", type = ApiParamType.STRING)
    private String endDate;

    @EntityField(name = "common.statusname", type = ApiParamType.STRING)
    private String statusLabel;
    @EntityField(name = "common.content", type = ApiParamType.STRING)
    private String content;
    @EntityField(name = "nfrd.issuevo.entityfield.name", type = ApiParamType.JSONOBJECT)
    private List<IssueWebhookVo> webhookList;
    @EntityField(name = "common.useridlist", type = ApiParamType.JSONARRAY)
    private List<String> userIdList;
    @EntityField(name = "common.userinfolist", type = ApiParamType.JSONARRAY)
    private List<UserVo> userList;
    @EntityField(name = "common.taglist", type = ApiParamType.JSONARRAY)
    private List<String> tagList;

    @EntityField(name = "common.attributelist", type = ApiParamType.JSONARRAY)
    private List<IssueAttrVo> attrList;
    @JSONField(serialize = false)
    private List<AppAttrVo> appAttrList;//搜索时生成字段
    @JSONField(serialize = false)//自定义属性搜索条件
    private List<IssueAttrVo> attrFilterList;

    private HashMap<Long, ?> attrMap;
    @EntityField(name = "common.filelist", type = ApiParamType.JSONARRAY)
    private List<FileVo> fileList;

    @JSONField(serialize = false)
    private List<String> startTimeRange;
    @JSONField(serialize = false)
    private List<String> endTimeRange;
    @EntityField(name = "term.rdm.relativetasklist", type = ApiParamType.JSONARRAY)
    private List<IssueRelVo> issueRelList;
    @EntityField(name = "term.rdm.auditcount", type = ApiParamType.INTEGER)
    private int auditCount;

    @EntityField(name = "term.rdm.isend", type = ApiParamType.INTEGER)
    private Integer isEnd;
    @EntityField(name = "common.isexpired", type = ApiParamType.INTEGER)
    private Integer isExpired;
    @EntityField(name = "是否收藏", type = ApiParamType.INTEGER)
    private Integer isFavorite;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Long getFromId() {
        return fromId;
    }

    public void formatAttr() {
        if (CollectionUtils.isNotEmpty(attrList)) {
            for (IssueAttrVo attr : attrList) {
                attr.format();
            }
        }
    }


    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Integer getIsFavorite() {
        return isFavorite;
    }

    public List<IssueWebhookVo> getWebhookList() {
        return webhookList;
    }

    public void setWebhookList(List<IssueWebhookVo> webhookList) {
        this.webhookList = webhookList;
    }

    public void setIsFavorite(Integer isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppColor() {
        if (StringUtils.isBlank(appColor) && StringUtils.isNotBlank(appType)) {
            appColor = AppTypeManager.getColor(appType);
        }
        return appColor;
    }


    public String getStartDate() {
        return startDate;
    }


    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getIsExpired() {
        if (isExpired == null) {
            if (StringUtils.isNotBlank(endDate)) {
                SimpleDateFormat sdf = new SimpleDateFormat(TimeUtil.YYYY_MM_DD);
                try {
                    Date d = sdf.parse(endDate);
                    isExpired = d.before(new Date()) ? 1 : 0;
                } catch (Exception ignored) {
                }
            }
        }
        return this.isExpired;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public String getIterationName() {
        return iterationName;
    }

    public void setIterationName(String iterationName) {
        this.iterationName = iterationName;
    }

    public List<IssueAttrVo> getAttrFilterList() {
        return attrFilterList;
    }

    public void setAttrFilterList(List<IssueAttrVo> attrFilterList) {
        this.attrFilterList = attrFilterList;
    }

    public List<FileVo> getFileList() {
        return fileList;
    }

    //此属性只是为了对比
    public List<Long> getFileIdList() {
        if (CollectionUtils.isNotEmpty(this.fileList)) {
            return this.fileList.stream().map(FileVo::getId).sorted(Long::compareTo).collect(Collectors.toList());
        }
        return null;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

    public List<AppAttrVo> getAppAttrList() {
        return appAttrList;
    }

    public void setAppAttrList(List<AppAttrVo> appAttrList) {
        this.appAttrList = appAttrList;
    }


    public Integer getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Integer isEnd) {
        this.isEnd = isEnd;
    }

    public String getComment() {
        return comment;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setFileList(List<FileVo> fileList) {
        this.fileList = fileList;
    }

    public List<String> getUserIdList() {
        if (userIdList == null && userList != null) {
            userIdList = new ArrayList<>();
            for (UserVo userVo : userList) {
                userIdList.add(GroupSearch.USER.getValuePlugin() + userVo.getUuid());
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

    public String getPriorityColor() {
        return priorityColor;
    }

    public void setPriorityColor(String priorityColor) {
        this.priorityColor = priorityColor;
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

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public void addAppAttr(AppAttrVo attrVo) {
        if (this.appAttrList == null) {
            this.appAttrList = new ArrayList<>();
        }
        if (!appAttrList.contains(attrVo)) {
            appAttrList.add(attrVo);
        }
    }

    public Long getIteration() {
        return iteration;
    }

    public void setIteration(Long iteration) {
        this.iteration = iteration;
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
        if (this.getAppId() != null) {
            return TenantContext.get().getDataDbName() + ".`rdm_app_" + this.getAppId() + "`";
        } else {
            return null;
        }
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

    public List<IssueRelVo> getIssueRelList() {
        return issueRelList;
    }

    public void setIssueRelList(List<IssueRelVo> issueRelList) {
        this.issueRelList = issueRelList;
    }

    public int getAuditCount() {
        return auditCount;
    }

    public void setAuditCount(int auditCount) {
        this.auditCount = auditCount;
    }
}
