package com.xq.live.vo.in;

/**
 * sku 入参
 *
 * @author zhangpeng32
 * @date 2018-02-09 10:39
 * @copyright:hbxq
 **/
public class SkuInVo extends BaseInVo {

    private Long id;

    private String skuCode;

    private String skuName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}
