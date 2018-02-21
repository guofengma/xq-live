package com.xq.live.vo.in;
/**
 * 餐厅入参
 *
 * @author zhangpeng32
 * @create 2018-02-07 16:55
 **/
public class ShopInVo extends BaseInVo {
    private Long id;
    private String shopName;

    public String getShopName() {
        return shopName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
