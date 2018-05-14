package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Coupon;
import com.xq.live.service.CouponService;
import com.xq.live.vo.in.CouponInVo;
import com.xq.live.vo.out.CouponOut;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 券
 *
 * @author zhangpeng32
 * @date 2018-02-08 21:32
 * @copyright:hbxq
 **/
@RestController
@RequestMapping(value = "/cp")
public class CouponController {
    @Autowired
    private CouponService couponService;

    /**
     * 根据id查一条记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<CouponOut> get(@PathVariable("id") Long id){
        CouponOut cp = couponService.selectById(id);
        return new BaseResp<CouponOut>(ResultStatus.SUCCESS, cp);
    }

    /**
     * 根据券码查一条记录
     * @param couponCode
     * @return
     */
        @RequestMapping(value = "/getByCode/{couponCode}", method = RequestMethod.GET)
    public BaseResp<CouponOut> getByCode(@PathVariable("couponCode")String couponCode){
        if(StringUtils.isEmpty(couponCode)){
            return new BaseResp<CouponOut>(ResultStatus.error_para_coupon_code_empty);
        }
        CouponOut cp = couponService.getByCouponCode(couponCode);
        return new BaseResp<CouponOut>(ResultStatus.SUCCESS, cp);
    }

    /**
     * 分页查询
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<CouponOut>> list(CouponInVo inVo){
        Pager<CouponOut> result = couponService.list(inVo);
        return new BaseResp<Pager<CouponOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查热门
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<CouponOut>> top(CouponInVo inVo){
        List<CouponOut> result = couponService.top(inVo);
        return new BaseResp<List<CouponOut>>(ResultStatus.SUCCESS, result);
    }

/*    *//**
     * 新增券
     * @param coupon
     * @param result
     * @return
     *//*
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid Coupon coupon, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long skuId = couponService.add(coupon);
        return new BaseResp<Long>(ResultStatus.SUCCESS, skuId);
    }*/
}
