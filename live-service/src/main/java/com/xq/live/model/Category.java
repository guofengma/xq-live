package com.xq.live.model;

import java.math.BigDecimal;
import java.util.Date;

public class Category {
    private Long id;

    private String categoryCode;

    private String categoryName;

    private Long categoryParentId;

    private String categoryDescription;

    private String categoryKeyword;

    private Byte categoryIsLeaf;

    private String backOperatorId;

    private Short categoryLevel;

    private Integer listOrder;

    private String categorySearchName;

    private String categorySeoTitle;

    private String categorySeoKeyword;

    private String categorySeoDescription;

    private Byte isDelete;

    private Long forumId;

    private Byte isVisible;

    private Byte dataExchangeFlag;

    private Byte searchVisible;

    private String landingPage;

    private BigDecimal canTranOrderLimitPrice;

    private BigDecimal canTranOrderLimitWeigth;

    private BigDecimal rebate;

    private BigDecimal qualityAssuranceDayPercent;

    private Short daysOnWayTrans;

    private Integer smaxDay;

    private Integer sminDay;

    private BigDecimal saleTrendModulus;

    private String categoryContacterEmail;

    private Byte isSent;

    private Byte isMustInvoice;

    private Byte invoiceUpdateFlag;

    private Long deliveryPartitionId;

    private Byte seprateSoUpdateFlag;

    private Long updateOperatorId;

    private Date updateTime;

    private Long showGuideNum;

    private Long brandOrder;

    private Byte brandVisible;

    private Long priceOrder;

    private Byte priceVisible;

    private Long maxCancelDay;

    private Long maxBarterDay;

    private Date createTime;

    private BigDecimal sbyRate;

    private Short attrLockStatus;

    private Byte isNeworoldCategoryattrFlag;

    private String issueCaption;

    private String issuePicUrl;

    private Byte isSbyRate;

    private Byte isFqdeliveryOntimereach;

    private Byte isOnekeyBuy;

    private String fullPath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode == null ? null : categoryCode.trim();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public Long getCategoryParentId() {
        return categoryParentId;
    }

    public void setCategoryParentId(Long categoryParentId) {
        this.categoryParentId = categoryParentId;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription == null ? null : categoryDescription.trim();
    }

    public String getCategoryKeyword() {
        return categoryKeyword;
    }

    public void setCategoryKeyword(String categoryKeyword) {
        this.categoryKeyword = categoryKeyword == null ? null : categoryKeyword.trim();
    }

    public Byte getCategoryIsLeaf() {
        return categoryIsLeaf;
    }

    public void setCategoryIsLeaf(Byte categoryIsLeaf) {
        this.categoryIsLeaf = categoryIsLeaf;
    }

    public String getBackOperatorId() {
        return backOperatorId;
    }

    public void setBackOperatorId(String backOperatorId) {
        this.backOperatorId = backOperatorId == null ? null : backOperatorId.trim();
    }

    public Short getCategoryLevel() {
        return categoryLevel;
    }

    public void setCategoryLevel(Short categoryLevel) {
        this.categoryLevel = categoryLevel;
    }

    public Integer getListOrder() {
        return listOrder;
    }

    public void setListOrder(Integer listOrder) {
        this.listOrder = listOrder;
    }

    public String getCategorySearchName() {
        return categorySearchName;
    }

    public void setCategorySearchName(String categorySearchName) {
        this.categorySearchName = categorySearchName == null ? null : categorySearchName.trim();
    }

    public String getCategorySeoTitle() {
        return categorySeoTitle;
    }

    public void setCategorySeoTitle(String categorySeoTitle) {
        this.categorySeoTitle = categorySeoTitle == null ? null : categorySeoTitle.trim();
    }

    public String getCategorySeoKeyword() {
        return categorySeoKeyword;
    }

    public void setCategorySeoKeyword(String categorySeoKeyword) {
        this.categorySeoKeyword = categorySeoKeyword == null ? null : categorySeoKeyword.trim();
    }

    public String getCategorySeoDescription() {
        return categorySeoDescription;
    }

    public void setCategorySeoDescription(String categorySeoDescription) {
        this.categorySeoDescription = categorySeoDescription == null ? null : categorySeoDescription.trim();
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Byte getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Byte isVisible) {
        this.isVisible = isVisible;
    }

    public Byte getDataExchangeFlag() {
        return dataExchangeFlag;
    }

    public void setDataExchangeFlag(Byte dataExchangeFlag) {
        this.dataExchangeFlag = dataExchangeFlag;
    }

    public Byte getSearchVisible() {
        return searchVisible;
    }

    public void setSearchVisible(Byte searchVisible) {
        this.searchVisible = searchVisible;
    }

    public String getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(String landingPage) {
        this.landingPage = landingPage == null ? null : landingPage.trim();
    }

    public BigDecimal getCanTranOrderLimitPrice() {
        return canTranOrderLimitPrice;
    }

    public void setCanTranOrderLimitPrice(BigDecimal canTranOrderLimitPrice) {
        this.canTranOrderLimitPrice = canTranOrderLimitPrice;
    }

    public BigDecimal getCanTranOrderLimitWeigth() {
        return canTranOrderLimitWeigth;
    }

    public void setCanTranOrderLimitWeigth(BigDecimal canTranOrderLimitWeigth) {
        this.canTranOrderLimitWeigth = canTranOrderLimitWeigth;
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
    }

    public BigDecimal getQualityAssuranceDayPercent() {
        return qualityAssuranceDayPercent;
    }

    public void setQualityAssuranceDayPercent(BigDecimal qualityAssuranceDayPercent) {
        this.qualityAssuranceDayPercent = qualityAssuranceDayPercent;
    }

    public Short getDaysOnWayTrans() {
        return daysOnWayTrans;
    }

    public void setDaysOnWayTrans(Short daysOnWayTrans) {
        this.daysOnWayTrans = daysOnWayTrans;
    }

    public Integer getSmaxDay() {
        return smaxDay;
    }

    public void setSmaxDay(Integer smaxDay) {
        this.smaxDay = smaxDay;
    }

    public Integer getSminDay() {
        return sminDay;
    }

    public void setSminDay(Integer sminDay) {
        this.sminDay = sminDay;
    }

    public BigDecimal getSaleTrendModulus() {
        return saleTrendModulus;
    }

    public void setSaleTrendModulus(BigDecimal saleTrendModulus) {
        this.saleTrendModulus = saleTrendModulus;
    }

    public String getCategoryContacterEmail() {
        return categoryContacterEmail;
    }

    public void setCategoryContacterEmail(String categoryContacterEmail) {
        this.categoryContacterEmail = categoryContacterEmail == null ? null : categoryContacterEmail.trim();
    }

    public Byte getIsSent() {
        return isSent;
    }

    public void setIsSent(Byte isSent) {
        this.isSent = isSent;
    }

    public Byte getIsMustInvoice() {
        return isMustInvoice;
    }

    public void setIsMustInvoice(Byte isMustInvoice) {
        this.isMustInvoice = isMustInvoice;
    }

    public Byte getInvoiceUpdateFlag() {
        return invoiceUpdateFlag;
    }

    public void setInvoiceUpdateFlag(Byte invoiceUpdateFlag) {
        this.invoiceUpdateFlag = invoiceUpdateFlag;
    }

    public Long getDeliveryPartitionId() {
        return deliveryPartitionId;
    }

    public void setDeliveryPartitionId(Long deliveryPartitionId) {
        this.deliveryPartitionId = deliveryPartitionId;
    }

    public Byte getSeprateSoUpdateFlag() {
        return seprateSoUpdateFlag;
    }

    public void setSeprateSoUpdateFlag(Byte seprateSoUpdateFlag) {
        this.seprateSoUpdateFlag = seprateSoUpdateFlag;
    }

    public Long getUpdateOperatorId() {
        return updateOperatorId;
    }

    public void setUpdateOperatorId(Long updateOperatorId) {
        this.updateOperatorId = updateOperatorId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getShowGuideNum() {
        return showGuideNum;
    }

    public void setShowGuideNum(Long showGuideNum) {
        this.showGuideNum = showGuideNum;
    }

    public Long getBrandOrder() {
        return brandOrder;
    }

    public void setBrandOrder(Long brandOrder) {
        this.brandOrder = brandOrder;
    }

    public Byte getBrandVisible() {
        return brandVisible;
    }

    public void setBrandVisible(Byte brandVisible) {
        this.brandVisible = brandVisible;
    }

    public Long getPriceOrder() {
        return priceOrder;
    }

    public void setPriceOrder(Long priceOrder) {
        this.priceOrder = priceOrder;
    }

    public Byte getPriceVisible() {
        return priceVisible;
    }

    public void setPriceVisible(Byte priceVisible) {
        this.priceVisible = priceVisible;
    }

    public Long getMaxCancelDay() {
        return maxCancelDay;
    }

    public void setMaxCancelDay(Long maxCancelDay) {
        this.maxCancelDay = maxCancelDay;
    }

    public Long getMaxBarterDay() {
        return maxBarterDay;
    }

    public void setMaxBarterDay(Long maxBarterDay) {
        this.maxBarterDay = maxBarterDay;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getSbyRate() {
        return sbyRate;
    }

    public void setSbyRate(BigDecimal sbyRate) {
        this.sbyRate = sbyRate;
    }

    public Short getAttrLockStatus() {
        return attrLockStatus;
    }

    public void setAttrLockStatus(Short attrLockStatus) {
        this.attrLockStatus = attrLockStatus;
    }

    public Byte getIsNeworoldCategoryattrFlag() {
        return isNeworoldCategoryattrFlag;
    }

    public void setIsNeworoldCategoryattrFlag(Byte isNeworoldCategoryattrFlag) {
        this.isNeworoldCategoryattrFlag = isNeworoldCategoryattrFlag;
    }

    public String getIssueCaption() {
        return issueCaption;
    }

    public void setIssueCaption(String issueCaption) {
        this.issueCaption = issueCaption == null ? null : issueCaption.trim();
    }

    public String getIssuePicUrl() {
        return issuePicUrl;
    }

    public void setIssuePicUrl(String issuePicUrl) {
        this.issuePicUrl = issuePicUrl == null ? null : issuePicUrl.trim();
    }

    public Byte getIsSbyRate() {
        return isSbyRate;
    }

    public void setIsSbyRate(Byte isSbyRate) {
        this.isSbyRate = isSbyRate;
    }

    public Byte getIsFqdeliveryOntimereach() {
        return isFqdeliveryOntimereach;
    }

    public void setIsFqdeliveryOntimereach(Byte isFqdeliveryOntimereach) {
        this.isFqdeliveryOntimereach = isFqdeliveryOntimereach;
    }

    public Byte getIsOnekeyBuy() {
        return isOnekeyBuy;
    }

    public void setIsOnekeyBuy(Byte isOnekeyBuy) {
        this.isOnekeyBuy = isOnekeyBuy;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath == null ? null : fullPath.trim();
    }
}