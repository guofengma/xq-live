package com.xq.live.model;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品SKU 实体entity
 */
public class Sku {
    /**
     * sku_type 1 平台券  2 特色菜 3 活动券 5商家套餐
     */
    public final static int SKU_TYPE_XQQ = 1;   //享七券

    public final static int SKU_TYPE_TSC = 2;   //特色菜

    public final static int SKU_TYPE_HDQ = 3;   //食典券

    /*public final static int SKU_TYPE_AGIO = 4;   //平台折扣券*/

    public final static int SKU_TYPE_SJTC = 5;   //商家套餐

    /**
     * 是否删除
     */
    public final static int SKU_NO_DELETED = 0; //未删除

    public final static int SKU_IS_DELETED = 1;//已删除

    private Long id;
    private String skuCode;
    @NotNull(message = "skuName必填")
    private String skuName;
    @NotNull(message = "skuType必填")
    private Integer skuType;   //sku类型 1 券  2 其他

    private BigDecimal sellPrice;//相当于原价

    private BigDecimal inPrice;//相当于进价

    private BigDecimal agioPrice;//折扣价

    @NotNull(message = "stockNum必填")
    private Integer stockNum;

    private Date createTime;

    private Date updateTime;

    @NotNull(message = "opreatorId必填")
    private Long opreatorId;//操作人的用户id

    @NotNull(message = "opreatorName必填")
    private String opreatorName;//操作人的用户名称

    private Integer isDeleted;

    private String picUrl;

    private String skuInfo;

    private Long shopId;//关联的shopId

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
        this.skuCode = skuCode == null ? null : skuCode.trim();
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName == null ? null : skuName.trim();
    }

    public Integer getSkuType() {
        return skuType;
    }

    public void setSkuType(Integer skuType) {
        this.skuType = skuType;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getInPrice() {
        return inPrice;
    }

    public void setInPrice(BigDecimal inPrice) {
        this.inPrice = inPrice;
    }

    public BigDecimal getAgioPrice() {
        return agioPrice;
    }

    public void setAgioPrice(BigDecimal agioPrice) {
        this.agioPrice = agioPrice;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
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

    public Long getOpreatorId() {
        return opreatorId;
    }

    public void setOpreatorId(Long opreatorId) {
        this.opreatorId = opreatorId;
    }

    public String getOpreatorName() {
        return opreatorName;
    }

    public void setOpreatorName(String opreatorName) {
        this.opreatorName = opreatorName == null ? null : opreatorName.trim();
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public String getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(String skuInfo) {
        this.skuInfo = skuInfo;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
}
