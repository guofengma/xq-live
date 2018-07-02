package com.xq.live.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class ShopEnter {
    /**
     * 审批状态 0 审批中  1 审批通过  2审批不通过
     */
    public final static int SHOP_ENTER_WAIT = 0;

    public final static int SHOP_ENTER_CAN = 1;

    public final static int SHOP_ENTER_NO_CAN =2;

    private Long id;
    @NotNull(message = "userName必填")
    private String userName;//用户名称
    @NotNull(message = "mobile必填")
    private String mobile;//联系方式
    @NotNull(message = "shopName必填")
    private String shopName;//店铺名称
    @NotNull(message = "address必填")
    private String address;//店铺地址
    @NotNull(message = "businessCate必填")
    private String businessCate;//经营品类
    @NotNull(message = "licensePic必填")
    private String licensePic;//营业执照
    @NotNull(message = "healthPic必填")
    private String healthPic;//卫生许可证
    @NotNull(message = "doorPic必填")
    private String doorPic;//门头照
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//更新时间
    @NotNull(message = "userId必填")
    private Long userId;//用户id

    private Integer status;//审核状态 0 待审批 1 审批通过 2审批不通过

    private BigDecimal locationX;//经度

    private BigDecimal locationY;//维度

    private String city;//城市
    @NotNull(message = "logoPic必填")
    private String logoPic;//主图
    @NotNull(message = "shopHours必填")
    private String shopHours;//商家营业时间
    @NotNull(message = "otherService必填")
    private String otherService;//其他服务

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getBusinessCate() {
        return businessCate;
    }

    public void setBusinessCate(String businessCate) {
        this.businessCate = businessCate == null ? null : businessCate.trim();
    }

    public String getLicensePic() {
        return licensePic;
    }

    public void setLicensePic(String licensePic) {
        this.licensePic = licensePic == null ? null : licensePic.trim();
    }

    public String getHealthPic() {
        return healthPic;
    }

    public void setHealthPic(String healthPic) {
        this.healthPic = healthPic == null ? null : healthPic.trim();
    }

    public String getDoorPic() {
        return doorPic;
    }

    public void setDoorPic(String doorPic) {
        this.doorPic = doorPic == null ? null : doorPic.trim();
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLogoPic() {
        return logoPic;
    }

    public void setLogoPic(String logoPic) {
        this.logoPic = logoPic;
    }

    public String getShopHours() {
        return shopHours;
    }

    public void setShopHours(String shopHours) {
        this.shopHours = shopHours;
    }

    public String getOtherService() {
        return otherService;
    }

    public void setOtherService(String otherService) {
        this.otherService = otherService;
    }
}
