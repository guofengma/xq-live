package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Coupon;
import com.xq.live.model.Shop;
import com.xq.live.model.SoWriteOff;
import com.xq.live.model.User;
import com.xq.live.service.CouponService;
import com.xq.live.service.ShopService;
import com.xq.live.service.SoWriteOffService;
import com.xq.live.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author lipeng
 * @date 2018-02-21 18:18
 * @copyright:hbxq
 **/
@RestController
@RequestMapping(value = "/hxForApp")
public class SoWriteOffForAppController {

    @Autowired
    private SoWriteOffService soWriteOffService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CouponService couponService;

    /**
     * 根据id查询记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<SoWriteOff> get(@PathVariable("id") Long id) {
        SoWriteOff soWriteOff = soWriteOffService.get(id);
        return new BaseResp<SoWriteOff>(ResultStatus.SUCCESS, soWriteOff);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid SoWriteOff soWriteOff, BindingResult result) {
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        //1、参数校验--验证券是否被核销过
        Coupon coupon = couponService.get(soWriteOff.getCouponId());
        if(coupon == null || coupon.getIsUsed() == Coupon.COUPON_IS_USED_YES){
            return new BaseResp<Long>(ResultStatus.error_coupon_is_used);
        }

        //验证扫码人id
        User cashier = userService.getUserById(soWriteOff.getCashierId());
        if(cashier == null){
            return new BaseResp<Long>(ResultStatus.error_para_cashier_id);
        }

        //验证账号类型：商家账号
        if(cashier.getUserType() != User.USER_TYPE_SJ){
            return new BaseResp<Long>(ResultStatus.error_para_cashier_user_type);
        }

        //验证商家信息
        if(cashier.getShopId() == null){
            return new BaseResp<Long>(ResultStatus.error_para_user_shop_id);
        }
        Shop shop = shopService.getShopByUserId(cashier.getId());
        if(shop ==  null){
            return new BaseResp<Long>(ResultStatus.error_shop_info_empty);
        }



        soWriteOff.setShopId(shop.getId());
        soWriteOff.setShopName(shop.getShopName());
        soWriteOff.setPaidAmount(soWriteOff.getShopAmount().subtract(soWriteOff.getCouponAmount()));
        //2、核销抵用券
        Long id = soWriteOffService.add(soWriteOff);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }
}
