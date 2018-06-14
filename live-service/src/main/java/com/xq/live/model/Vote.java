package com.xq.live.model;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class Vote {
    /**
     * 投票操作  1投票 2取消投票
     */
    public final static int VOTE_ADD = 1;   //投票

    public final static int VOTE_DELETE = 2;   //取消投票

    private Long id;

    @NotNull(message = "actId必填")
    private Long actId;

    //@NotNull(message = "shopId必填")
    private Long shopId;

    @NotNull(message = "userId必填")
    private Long userId;

    private Long playerUserId;//参与选手的用户id

    private Long skuId;//推荐菜的sku_id

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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getPlayerUserId() {
        return playerUserId;
    }

    public void setPlayerUserId(Long playerUserId) {
        this.playerUserId = playerUserId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
