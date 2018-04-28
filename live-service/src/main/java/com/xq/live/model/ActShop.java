package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class ActShop {
    /**
     * 参与活动申请审批状态 0 待审批 1 审批通过 2 审批不通过
     */
    public final static int ACT_SHOP_APPLY_STATUS_WAIT_APPLIED = 0;

    public final static int ACT_SHOP_APPLY_STATUS_APPLIED = 1;

    public final static int ACT_SHOP_APPLY_STATUS_REFUSED = 2;

    /**
     * 判断商家是否已经参加该活动 0 未报名 1已报名
     */
    public final static int ACT_SHOP_IS_SIGN = 1;

    public final static int ACT_SHOP_NO_SIGN = 0;

    private Long id;
    @NotNull(message = "actId必填")
    private Long actId;
    @NotNull(message = "shopId必填")
    private Long shopId;

    private String shopCode;

    private Integer applyStatus;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Integer voteNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActId() {
        return actId;
    }

    public void setActId(Long actId) {
        this.actId = actId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Integer getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(Integer voteNum) {
        this.voteNum = voteNum;
    }
}
