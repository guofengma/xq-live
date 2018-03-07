package com.xq.live.vo.in;

/**
 * 活动商家入参
 *
 * @author zhangpeng32
 * @date 2018-03-06 21:10
 * @copyright:hbxq
 **/
public class ActShopInVo extends BaseInVo{
    private Long id;

    private String shopCode;

    private Long actId;

    private Long shopId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
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
}
