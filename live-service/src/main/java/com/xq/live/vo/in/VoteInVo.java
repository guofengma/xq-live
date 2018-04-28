package com.xq.live.vo.in;

import java.util.Date;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-21 21:14
 * @copyright:hbxq
 **/
public class VoteInVo extends  BaseInVo {
    private Long id;

    private Long actId;

    private Long shopId;

    private Long userId;

    private Long playUserId;//参与选手的用户id

    private Date createTime;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getPlayUserId() {
        return playUserId;
    }

    public void setPlayUserId(Long playUserId) {
        this.playUserId = playUserId;
    }
}
