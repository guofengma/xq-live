package com.xq.live.vo.in;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 餐厅入参
 *
 * @author zhangpeng32
 * @create 2018-02-07 16:55
 **/
public class ShopInVo extends BaseInVo {
    @NotNull(message = "id必填")
    private Long id;

    private String shopName;
    @NotNull(message = "userId必填")
    private Long userId;
    @NotNull(message = "userName必填")
    private String userName;

    private String userIp;
    @NotNull(message = "sourceType必填")
    private Integer sourceType;

    /**
     * 搜索关键字
     */
    private String searcheKey;

    private Integer browSort;//综合排序-----0 口味 1服务 2 人气


    private BigDecimal locationX;

    private BigDecimal locationY;

    private Integer shopStatus;//店铺状态

    private Integer applyStatus;//审批状态

    private String businessCate;//经营品类


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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getSearcheKey() {
        return searcheKey;
    }

    public void setSearcheKey(String searcheKey) {
        this.searcheKey = searcheKey;
    }

    public Integer getBrowSort() {
        return browSort;
    }

    public void setBrowSort(Integer browSort) {
        this.browSort = browSort;
    }

    public BigDecimal getLocationX() {
        return locationX;
    }

    public void setLocationX(BigDecimal locationX) {
        this.locationX = locationX;
    }

    public BigDecimal getLocationY() {
        return locationY;
    }

    public void setLocationY(BigDecimal locationY) {
        this.locationY = locationY;
    }

    public Integer getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(Integer shopStatus) {
        this.shopStatus = shopStatus;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getBusinessCate() {
        return businessCate;
    }

    public void setBusinessCate(String businessCate) {
        this.businessCate = businessCate;
    }
}
