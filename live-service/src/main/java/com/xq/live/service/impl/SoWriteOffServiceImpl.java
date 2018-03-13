package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.SoWriteOffService;
import com.xq.live.vo.in.CouponInVo;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.in.SoWriteOffInVo;
import com.xq.live.vo.out.SoOut;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-21 18:37
 * @copyright:hbxq
 **/
@Service
public class SoWriteOffServiceImpl implements SoWriteOffService {

    private Logger logger = Logger.getLogger(SoWriteOffServiceImpl.class);

    @Autowired
    private SoWriteOffMapper soWriteOffMapper;

    @Autowired
    private SoMapper soMapper;

    @Autowired
    private SoLogMapper soLogMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public Pager<SoWriteOff> list(SoWriteOffInVo inVo) {
        Pager<SoWriteOff> ret = new Pager<SoWriteOff>();
        int total = soWriteOffMapper.listTotal(inVo);
        if(total > 0){
            List<SoWriteOff> list = soWriteOffMapper.list(inVo);
            ret.setList(list);
        }
        ret.setTotal(total);
        ret.setPage(inVo.getPage());
        return ret;
    }

    @Override
    public List<SoWriteOff> top(SoWriteOffInVo inVo) {
        return soWriteOffMapper.list(inVo);
    }

    @Override
    public Long add(SoWriteOff soWriteOff) {
        //1、新增核销记录
        Integer ret = soWriteOffMapper.insert(soWriteOff);
        if(ret < 1){
            return null;
        }
        Long id = soWriteOff.getId();

        //2、更新订单表状态为已核销，记录订单状态变更日志
        SoInVo soInVo = new SoInVo();
        soInVo.setId(soWriteOff.getSoId());
        soInVo.setSoStatus(So.SO_STATUS_APPLIED);       //已核销
        soInVo.setUserId(soWriteOff.getUserId());
        soInVo.setUserName(soWriteOff.getUserName());
        int soRet = soMapper.hexiao(soInVo);
        if(soRet > 0){
            try {
                //2.1 订单日志
                SoLog soLog = new SoLog();
                soLog.setSoId(soInVo.getId());
                soLog.setUserId(soInVo.getUserId());
                soLog.setUserName(soInVo.getUserName());
                soLog.setOperateType(SoLog.SO_STATUS_APPLIED);
                soLogMapper.insert(soLog);
            }catch (Exception e){
                logger.error("核销保存订单日志失败,soId : "+ soInVo.getId());
            }
        }

        //3、更新抵用券状态为已使用
        Coupon coupon = new Coupon();
        coupon.setId(soWriteOff.getCouponId());
        coupon.setIsUsed(Coupon.COUPON_IS_USED_YES);
        coupon.setUsedTime(new Date());
        coupon.setUserId(soWriteOff.getUserId());
        coupon.setShopId(soWriteOff.getShopId());   //使用券的商家
        coupon.setShopCashierId(soWriteOff.getCashierId()); //核销人
        couponMapper.useCoupon(coupon);

        return id;
    }

    @Override
    public SoWriteOff get(Long id) {
        return soWriteOffMapper.selectByPrimaryKey(id);
    }

    @Override
    public int validInput(SoWriteOff soWriteOff) {
        Coupon coupon = couponMapper.selectByPrimaryKey(soWriteOff.getCouponId());
        if(coupon == null){
            return 1;  //券不存在
        }

        if(coupon.getUserId().compareTo(soWriteOff.getUserId()) != 0){
            return 1;  //券不存在
        }

        if(coupon.getIsUsed() == Coupon.COUPON_IS_USED_YES){
            return 2; //券已使用
        }
        return 0;
    }
}
