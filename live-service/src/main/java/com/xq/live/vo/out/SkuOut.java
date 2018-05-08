package com.xq.live.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xq.live.model.CouponSku;
import com.xq.live.model.PromotionRules;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品出参sku + promotion_rules
 *
 * @author zhangpeng32
 * @date 2018-03-09 16:14
 * @copyright:hbxq
 **/
public class SkuOut {
    private Long id;
    private String skuCode;
    private String skuName;
    private Integer skuType;   //sku类型 1 券 2 其他
    private BigDecimal sellPrice;
    private BigDecimal inPrice;
    private BigDecimal agioPrice;//折扣价格
    private Integer stockNum;
    private String picUrl;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Long opreatorId;
    private String opreatorName;
    private Integer isDeleted;
    private Integer amount;  //卡面值
    private Integer sellNum;    //已售张数

    private List<PromotionRules> promotionRules;

    private String skuInfo;//推荐菜详情

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

    public BigDecimal getAgioPrice() {
        return agioPrice;
    }

    public void setAgioPrice(BigDecimal agioPrice) {
        this.agioPrice = agioPrice;
    }

    public void setInPrice(BigDecimal inPrice) {
        this.inPrice = inPrice;
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
        this.opreatorName = opreatorName;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<PromotionRules> getPromotionRules() {
        return promotionRules;
    }

    public void setPromotionRules(List<PromotionRules> promotionRules) {
        this.promotionRules = promotionRules;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getSellNum() {
        return sellNum;
    }

    public void setSellNum(Integer sellNum) {
        this.sellNum = sellNum;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(String skuInfo) {
        this.skuInfo = skuInfo;
    }
}
