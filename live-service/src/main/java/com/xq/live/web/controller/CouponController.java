package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Coupon;
import com.xq.live.service.CouponService;
import com.xq.live.vo.in.CouponInVo;
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
    public BaseResp<Coupon> get(@PathVariable("id") Long id){
        Coupon cp = couponService.get(id);
        return new BaseResp<Coupon>(ResultStatus.SUCCESS, cp);
    }

    /**
     * 分页查询
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<Coupon>> list(CouponInVo inVo){
        Pager<Coupon> result = couponService.list(inVo);
        return new BaseResp<Pager<Coupon>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查热门
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<Coupon>> top(CouponInVo inVo){
        List<Coupon> result = couponService.top(inVo);
        return new BaseResp<List<Coupon>>(ResultStatus.SUCCESS, result);
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
