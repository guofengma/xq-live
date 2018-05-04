package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 活动商家出参
 *
 * @author zhangpeng32
 * @date 2018-03-06 21:12
 * @copyright:hbxq
 **/
public class ActShopOut implements Comparable<ActShopOut>{

    private Long id;

    private Long actId;

    private Long shopId;

    private String shopCode;

    private Integer applyStatus;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String logoUrl;

    private String shopName;

    private String shopInfo;    //商家简介

    private int voteNum;

    private int isVote;//是否已投

    private Integer isLuoxuan;//是否落选

    private Long userId;//分组中与商家绑定的选手用户id

    private String groupCode;//分组编号

    private String groupName;//分组名称

    private Integer groupVoteNum;//分组的投票数

    private String iconUrl;//分组中与商家绑定选手的用户头像

    private String actUserName;//分组中与商家绑定选手的用户填写名称

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

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(String shopInfo) {
        this.shopInfo = shopInfo;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public int getIsVote() {
        return isVote;
    }

    public void setIsVote(int isVote) {
        this.isVote = isVote;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Integer getIsLuoxuan() {
        return isLuoxuan;
    }

    public void setIsLuoxuan(Integer isLuoxuan) {
        this.isLuoxuan = isLuoxuan;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupVoteNum() {
        return groupVoteNum;
    }

    public void setGroupVoteNum(Integer groupVoteNum) {
        this.groupVoteNum = groupVoteNum;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getActUserName() {
        return actUserName;
    }

    public void setActUserName(String actUserName) {
        this.actUserName = actUserName;
    }

    @Override
    public int compareTo(ActShopOut o) {
        int a = this.getVoteNum();
        int b = o.getVoteNum();
        if(a>b){
            return -1;
        }else {
            return 1;
        }

    }
}
