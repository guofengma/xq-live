package com.xq.live.model;

import java.math.BigDecimal;
import java.util.Date;

public class So {

    //订单状态 1待支付 2 已支付 3已核销 10取消
    public final static int SO_STATUS_WAIT_PAID = 1;
    public final static int SO_STATUS_PAID = 2;
    public final static int SO_STATUS_APPLIED = 3;
    public final static int SO_STATUS_CANCEL = 10;

    //订单类型 1 普通订单
    public final static int SO_TYPE_PT = 1;

    //支付方式 1 微信支付
    public final static int SO_PAY_TYPE_WX = 1;

    private Long id;

    private BigDecimal soAmount;

    private Long userId;

    private String userName;

    private Integer payType;

    private Integer soStatus;

    private Integer soType;

    private Date createTime;

    private Date updateTime;

    private SoDetail soDetail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSoAmount() {
        return soAmount;
    }

    public void setSoAmount(BigDecimal soAmount) {
        this.soAmount = soAmount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getSoStatus() {
        return soStatus;
    }

    public void setSoStatus(Integer soStatus) {
        this.soStatus = soStatus;
    }

    public Integer getSoType() {
        return soType;
    }

    public void setSoType(Integer soType) {
        this.soType = soType;
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

    public SoDetail getSoDetail() {
        return soDetail;
    }

    public void setSoDetail(SoDetail soDetail) {
        this.soDetail = soDetail;
    }
}