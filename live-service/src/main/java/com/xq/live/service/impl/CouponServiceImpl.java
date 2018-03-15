package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.CouponMapper;
import com.xq.live.model.Coupon;
import com.xq.live.service.CouponService;
import com.xq.live.vo.in.CouponInVo;
import com.xq.live.vo.out.CouponOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-08 21:36
 * @copyright:hbxq
 **/
@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public Coupon get(Long id) {
        return couponMapper.selectByPrimaryKey(id);
    }

    @Override
    public Pager<CouponOut> list(CouponInVo inVo) {
        Pager<CouponOut> result = new Pager<CouponOut>();
        int total = couponMapper.listTotal(inVo);
        if(total > 0){
            List<CouponOut> list = couponMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(total);
        return result;
    }

    @Override
    public List<CouponOut> top(CouponInVo inVo) {
        return couponMapper.list(inVo);
    }

    @Override
    public Long add(Coupon coupon) {
        int res = couponMapper.insert(coupon);
        if(res < 1){
            return null;
        }
        return coupon.getId();
    }

    @Override
    public CouponOut getByCouponCode(String couponCode) {
        return couponMapper.getByCouponCode(couponCode);
    }

    @Override
    public CouponOut selectById(Long id){
        return couponMapper.selectById(id);
    }
}
