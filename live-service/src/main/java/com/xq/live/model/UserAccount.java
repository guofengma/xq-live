package com.xq.live.model;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name="user_account")
public class UserAccount {

    /**
     * 用户账户类型 0 享七 1 微信 2 支付宝
     */
    public final static int ACCOUNT_TYPE_XQ = 0;

    public final static int ACCOUNT_TYPE_WX = 1;

    public final static int ACCOUNT_TYPE_ZFB = 2;

    /**
     * 账户状态  1 正常 2 冻结
     */
    public final static int ACCOUNT_STATUS_ACTIVE = 1;

    public final static int ACCOUNT_STATUS_FROZEN = 2;

    private Long id;    //用户账户主键

    private Long userId;    //用户表ID

    private String userName;    //用户账号

    private String accountName;    //支付账号

    private Integer accountType;   //账户类型 0 享七  1 微信  2支付宝

    private BigDecimal accountAmount;   //账户余额

    private Integer accountStatus; //账户状态 1 正常 2 冻结

    private Integer isDeleted;     //是否删除 0 否 1 是

    private Date createTime;    //创建时间

    private Date updateTime;    //更新时间

    private Integer versionNo;  //版本号

    public Long getId() {
        return id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setId(Long id) {
        this.id = id;
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
        this.userName = userName == null ? null : userName.trim();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }
}