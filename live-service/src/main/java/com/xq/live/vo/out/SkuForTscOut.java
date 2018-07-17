package com.xq.live.vo.out;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lipeng on 2018/3/22.
 */
public class SkuForTscOut {

    /**
     * sku_type 1 平台券  2 特色菜
     */
    public final static int SKU_TYPE_XQQ = 1;   //享七券

    public final static int SKU_TYPE_TSC = 2;   //特色菜

    private Long id;
    private String skuCode;
    @NotNull(message = "skuName必填")
    private String skuName;
    @NotNull(message = "skuType必填")
    private Integer skuType;   //sku类型 1 券 2 其他

    private BigDecimal sellPrice;

    private BigDecimal inPrice;

    private BigDecimal agioPrice;

    @NotNull(message = "stockNum必填")
    private Integer stockNum;

    private Date createTime;

    private Date updateTime;

    private Long opreatorId;

    private String opreatorName;

    private Integer isDeleted;

    private String picUrl;

    private String skuInfo;//推荐菜信息

    private Integer zan;//点赞数

    private Integer isZan;//是否点赞

    private BigDecimal agioLevel;// 折扣力度

    private Integer isSign;//查看推荐是否已报名活动

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

    public Integer getZan() {
        return zan;
    }

    public void setZan(Integer zan) {
        this.zan = zan;
    }

    public Integer getIsZan() {
        return isZan;
    }

    public void setIsZan(Integer isZan) {
        this.isZan = isZan;
    }

    public BigDecimal getAgioLevel() {
        return agioLevel;
    }

    public void setAgioLevel(BigDecimal agioLevel) {
        this.agioLevel = agioLevel;
    }

    public Integer getIsSign() {
        return isSign;
    }

    public void setIsSign(Integer isSign) {
        this.isSign = isSign;
    }
}
