package com.xq.live.model;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class Zan {
    private Long id;

    @NotNull(message = "refId必填")
    private Long refId;

    @NotNull(message = "type点赞类型必填")
    private Integer type;

    @NotNull(message = "userId必填")
    private Long userId;

    private Byte isDeleted;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}