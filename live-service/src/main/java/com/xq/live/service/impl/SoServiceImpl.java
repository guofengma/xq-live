package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.common.RandomStringUtil;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.SoService;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.out.SoOut;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-09 14:29
 * @copyright:hbxq
 **/
@Service
public class SoServiceImpl implements SoService {

    private Logger logger = Logger.getLogger(SoServiceImpl.class);

    @Autowired
    private SoMapper soMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private CouponSkuMapper couponSkuMapper;

    @Autowired
    private SoDetailMapper soDetailMapper;

    @Autowired
    private SoLogMapper soLogMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private PaidLogMapper paidLogMapper;

    @Override
    public Pager<SoOut> list(SoInVo inVo) {
        Pager<SoOut> ret = new Pager<SoOut>();
        int total = soMapper.listTotal(inVo);
        if(total > 0){
            List<SoOut> list = soMapper.list(inVo);
            ret.setList(list);
        }
        ret.setTotal(total);
        ret.setPage(inVo.getPage());
        return ret;
    }

    @Override
    public List<SoOut> findSoList(SoInVo inVo) {
        return soMapper.list(inVo);
    }

    @Override
    public Long create(SoInVo inVo) {
        //1、查询SKU信息
        Sku sku = skuMapper.selectByPrimaryKey(inVo.getSkuId());
        //2、查询券信息
        CouponSku couponSku = couponSkuMapper.selectBySkuId(inVo.getSkuId());

        //3、计算订单金额
        BigDecimal soAmount = BigDecimal.valueOf(inVo.getSkuNum()).multiply(sku.getSellPrice());

        //4、保存订单信息
        inVo.setSoAmount(soAmount);
        inVo.setSoStatus(So.SO_STATUS_WAIT_PAID);
        inVo.setSoType(So.SO_TYPE_PT);
        int ret = soMapper.insert(inVo);
        if(ret < 1){
            return null;
        }
        Long id = inVo.getId();

        //5、保存订单明细信息
        SoDetail sd = new SoDetail();
        sd.setSoId(id);
        sd.setSkuId(inVo.getSkuId());
        sd.setSkuCode(sku.getSkuCode());
        sd.setSkuName(sku.getSkuName());
        sd.setSkuNum(inVo.getSkuNum());
        sd.setUnitPrice(sku.getSellPrice());
        soDetailMapper.insert(sd);

        //6、生成抵用券
        for(int i = 0; i < inVo.getSkuNum(); i++){
            Coupon coupon = new Coupon();
            coupon.setSoId(id);
            coupon.setCouponCode(RandomStringUtil.getRandomCode(12, 2));
            coupon.setSkuId(sku.getId());
            coupon.setSkuCode(sku.getSkuCode());
            coupon.setSkuName(sku.getSkuName());
            coupon.setCouponAmount(couponSku.getAmount());
            coupon.setType(Coupon.COUPON_TYPE_PLAT);
            coupon.setUserId(inVo.getUserId());
            coupon.setUserName(inVo.getUserName());
            couponMapper.insert(coupon);
        }


        //7、保存订单日志
        SoLog soLog = new SoLog();
        soLog.setSoId(id);
        soLog.setUserId(inVo.getUserId());
        soLog.setUserName(inVo.getUserName());
        soLog.setOperateType(SoLog.SO_STATUS_WAIT_PAID);
        try {
            soLogMapper.insert(soLog);
        }catch (Exception e){
            logger.error("下单保存订单日志失败");
        }

        return id;
    }

    @Override
    public SoOut get(Long id) {
        return soMapper.selectByPk(id);
    }

    @Override
    public Integer paid(SoInVo inVo) {
        //1、更新订单状态
        inVo.setSoStatus(So.SO_STATUS_PAID);
        int ret = soMapper.paid(inVo);

        if(ret > 0){
            try {
                //2、支付日志
                PaidLog paidLog = new PaidLog();
                paidLog.setSoId(inVo.getId());
                paidLog.setPaidType(So.SO_PAY_TYPE_WX);
                paidLog.setUserId(inVo.getUserId());
                paidLog.setUserName(inVo.getUserName());
                paidLogMapper.insert(paidLog);

                //3、订单日志
                SoLog soLog = new SoLog();
                soLog.setSoId(inVo.getId());
                soLog.setUserId(inVo.getUserId());
                soLog.setUserName(inVo.getUserName());
                soLog.setOperateType(SoLog.SO_STATUS_PAID);
                soLogMapper.insert(soLog);
            }catch (Exception e){
                logger.error("支付保存订单日志失败,soId : "+ inVo.getId());
            }
        }
        return ret;
    }

    @Override
    public Integer finished(SoInVo inVo) {
        return null;
    }

    @Override
    public Integer cancel(SoInVo inVo) {
        //1、更新订单状态
        inVo.setSoStatus(So.SO_STATUS_CANCEL);
        int ret = soMapper.cancel(inVo);

        if(ret > 0){
            try {
                //2、订单日志
                SoLog soLog = new SoLog();
                soLog.setSoId(inVo.getId());
                soLog.setUserId(inVo.getUserId());
                soLog.setUserName(inVo.getUserName());
                soLog.setOperateType(SoLog.SO_STATUS_CANCEL);
                soLogMapper.insert(soLog);
            }catch (Exception e){
                logger.error("取消订单保存日志失败,soId : "+ inVo.getId());
            }
        }
        return ret;
    }
}
