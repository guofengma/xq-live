package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.CouponMapper;
import com.xq.live.model.Coupon;
import com.xq.live.service.CouponService;
import com.xq.live.vo.in.CouponInVo;
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
    public Pager<Coupon> list(CouponInVo inVo) {
        Pager<Coupon> result = new Pager<Coupon>();
        int total = couponMapper.listTotal(inVo);
        if(total > 0){
            List<Coupon> list = couponMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        result.setTotal(total);
        return result;
    }

    @Override
    public List<Coupon> top(CouponInVo inVo) {
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
}
