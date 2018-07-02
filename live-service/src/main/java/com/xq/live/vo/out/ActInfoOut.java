package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-03-06 19:32
 * @copyright:hbxq
 **/
public class ActInfoOut {
    private Long id;
    @NotNull(message = "actName必填")
    private String actName;

    @NotNull(message = "startTime必填")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @NotNull(message = "endTime必填")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer actStatus;  //活动状态

    private String actDesc;//活动描述

    private String mainPic; //主图url

    private String actUrl;  //活动链接url

    private Integer isDeleted;  //是否删除0 否 1 是

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;    //更新时间

    private int voteNum;      //总投票数

    private int shopNum;     //参与商家数

    private int viewNum;    //浏览人数

    private Integer isSign;//是否报名

    private Integer isSignForUser;//选手是否报名

    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getActStatus() {
        return actStatus;
    }

    public void setActStatus(Integer actStatus) {
        this.actStatus = actStatus;
    }

    public String getActDesc() {
        return actDesc;
    }

    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    public String getActUrl() {
        return actUrl;
    }

    public void setActUrl(String actUrl) {
        this.actUrl = actUrl;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public int getShopNum() {
        return shopNum;
    }

    public void setShopNum(int shopNum) {
        this.shopNum = shopNum;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public Integer getIsSign() {
        return isSign;
    }

    public void setIsSign(Integer isSign) {
        this.isSign = isSign;
    }

    public Integer getIsSignForUser() {
        return isSignForUser;
    }

    public void setIsSignForUser(Integer isSignForUser) {
        this.isSignForUser = isSignForUser;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
