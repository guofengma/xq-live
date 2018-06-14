package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.common.RandomStringUtil;
import com.xq.live.dao.CouponSkuMapper;
import com.xq.live.dao.SkuMapper;
import com.xq.live.dao.SoMapper;
import com.xq.live.model.CouponSku;
import com.xq.live.model.Sku;
import com.xq.live.service.SkuService;
import com.xq.live.vo.in.SkuInVo;
import com.xq.live.vo.out.SkuForTscOut;
import com.xq.live.vo.out.SkuOut;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-09 10:34
 * @copyright:hbxq
 **/
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SoMapper soMapper;

    @Autowired
    private CouponSkuMapper couponSkuMapper;

    private Logger logger = Logger.getLogger(SoServiceImpl.class);

    @Override
    public Sku get(Long id) {
        return skuMapper.selectByPrimaryKey(id);
    }

    @Override
    public Pager<SkuOut> list(SkuInVo inVo) {
        Pager<SkuOut> result =  new Pager<SkuOut>();
        int total = skuMapper.listTotal(inVo);
        if(total > 0){
            List<SkuOut> list = skuMapper.list(inVo);
            /*if(inVo!=null&&inVo.getUserId()!=null){
                int i = soMapper.selectByUserIdTotal(inVo.getUserId());//判断是否是新下单用户 0为首次下单
                if(i==0){
                    for (SkuOut skuOut : list) {
                        skuOut.setSellPrice(BigDecimal.ZERO);
                    }
                }
            }*/
            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public Pager<SkuForTscOut> queryTscList(SkuInVo inVo){
        Pager<SkuForTscOut> result =  new Pager<SkuForTscOut>();
        int total = skuMapper.tscListTotal(inVo);
        if(total > 0){
            List<SkuForTscOut> list = skuMapper.queryTscList(inVo);
            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public SkuForTscOut getTscForZan(SkuInVo inVo) {
        SkuForTscOut tscForZan = skuMapper.getTscForZan(inVo);
        return tscForZan;
    }


    @Override
    public List<SkuOut> top(SkuInVo inVo) {
        return skuMapper.list(inVo);
    }

    @Override
    public Long add(Sku sku) {
        sku.setSkuCode(RandomStringUtil.getRandomCode(8, 0));
        int res = skuMapper.insert(sku);

        //将推荐菜与shop_id关联起来
        SkuInVo vo = new SkuInVo();
        vo.setShopId(sku.getShopId());
        vo.setId(sku.getId());
        int i = skuMapper.insertSkuShop(vo);

        if(res < 1 || i < 1){
            return null;
        }
        return sku.getId();
    }

    @Override
    @Transactional
    public Long addSku(Sku sku) {
        sku.setSkuCode(RandomStringUtil.getRandomCode(8, 0));
        sku.setIsDeleted(Sku.SKU_NO_DELETED);
        int res = skuMapper.insert(sku);
        if(res<1){
            return null;
        }
        Sku sku1 = skuMapper.selectByPrimaryKey(sku.getId());
        CouponSku couponSku = new CouponSku();
        couponSku.setIsDeleted(CouponSku.COUPON_SKU_NO_DELETED);
        couponSku.setSkuId(sku.getId());
        couponSku.setAmount(sku.getSellPrice().intValue());
        //couponSku.setCreateTime(new Date());
        couponSku.setSkuCode(sku1.getSkuCode());
        couponSku.setSkuName(sku1.getSkuName());
        couponSku.setType(Sku.SKU_TYPE_HDQ);
        //couponSku.setUpdateTime(new Date());
        int insert = couponSkuMapper.insert(couponSku);
        //如果插入失败，直接回滚
        if(insert<1){
            logger.error("票券id："+ sku.getId() + " 插入信息失败");
            throw new RuntimeException("票券id："+ sku.getId() + " 插入信息失败");
        }
        return sku.getId();
    }

    @Override
    @Transactional
    public Integer updateSku(Sku skuForAct) {
        skuForAct.setIsDeleted(Sku.SKU_NO_DELETED);
        int i = skuMapper.updateByPrimaryKeySelective(skuForAct);

        CouponSku couponSku = new CouponSku();
        couponSku.setSkuId(skuForAct.getId());
        couponSku.setIsDeleted(CouponSku.COUPON_SKU_IS_DELETED);
        CouponSku couponSkuNew = couponSkuMapper.selectBySkuId(couponSku);
        couponSkuNew.setIsDeleted(CouponSku.COUPON_SKU_NO_DELETED);
        int j = couponSkuMapper.updateByPrimaryKeySelective(couponSkuNew);
        if(i<1||j<1){
            logger.error("票券id："+ skuForAct.getId() + " 更新信息失败");
            throw new RuntimeException("票券id："+ skuForAct.getId() + " 更新信息失败");
        }
        return 1;
    }

    @Override
    public Sku selectForActSku(Sku inVo) {
        Sku sku = skuMapper.selectForActSku(inVo);
        return sku;
    }

    @Override
    public SkuOut selectById(Long id) {
        return skuMapper.selectById(id);
    }

    @Override
    public Integer isNewUser(Long userId) {
        int i = soMapper.selectByUserIdTotal(userId);
        return i;
    }

    @Override
    public Integer delete(Long id) {
        Sku sku = new Sku();
        sku.setId(id);
        sku.setIsDeleted(Sku.SKU_IS_DELETED);
        int i = skuMapper.updateByPrimaryKeySelective(sku);
        return i;
    }

    @Override
    public Integer update(Sku sku) {
        int i = skuMapper.updateByPrimaryKeySelective(sku);
        return i;
    }

}
