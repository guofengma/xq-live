package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.ActShopMapper;
import com.xq.live.dao.ActSignMapper;
import com.xq.live.dao.ActUserMapper;
import com.xq.live.dao.SkuMapper;
import com.xq.live.model.ActShop;
import com.xq.live.model.ActSign;
import com.xq.live.model.Sku;
import com.xq.live.service.ActShopService;
import com.xq.live.vo.in.ActShopInVo;
import com.xq.live.vo.in.ActSignInVo;
import com.xq.live.vo.in.ActUserInVo;
import com.xq.live.vo.out.ActInfoOut;
import com.xq.live.vo.out.ActShopByShopIdOut;
import com.xq.live.vo.out.ActShopOut;
import com.xq.live.vo.out.ActUserOut;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

/**
 * 活动商家表service（act_shop
 *
 * @author zhangpeng32
 * @date 2018-03-07 15:07
 * @copyright:hbxq
 **/
@Service
public class ActShopServiceImpl implements ActShopService {

    @Autowired
    private ActShopMapper actShopMapper;

    @Autowired
    private ActUserMapper actUserMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private ActSignMapper actSignMapper;

    private static Logger logger = Logger.getLogger(ActInfoServiceImpl.class);

    @Override
    public Pager<ActShopOut> list(ActShopInVo inVo) {
        Pager<ActShopOut> result = new Pager<ActShopOut>();
        int listTotal = actShopMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<ActShopOut> list = actShopMapper.list(inVo);
            Collections.sort(list);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public Pager<ActShopOut> listForNewAct(ActShopInVo inVo) {
        Pager<ActShopOut> result = new Pager<ActShopOut>();
        int listTotal = actShopMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<ActShopOut> list = actShopMapper.listForNewAct(inVo);
            //如果是查询分组的，把分组的关联信息加入进去
            if(inVo.getType()!=null&&inVo.getType()==ActShop.ACT_SHOP_GROUP){
                for (ActShopOut actShopOut : list) {
                    ActUserInVo actUserInVo = new ActUserInVo();
                    actUserInVo.setActId(inVo.getActId());
                    actUserInVo.setUserId(actShopOut.getUserId());
                    ActUserOut byInVo = actUserMapper.findByInVo(actUserInVo);
                    actShopOut.setIconUrl(byInVo.getIconUrl());
                    actShopOut.setActUserName(byInVo.getActUserName());
                }
            }
            for (ActShopOut actShopOut : list) {
                ActSignInVo actSignInVo = new ActSignInVo();
                actSignInVo.setType(ActSign.ACT_SIGN_TYPE_SHOP);
                actSignInVo.setRefId(actShopOut.getShopId());
                actSignInVo.setActId(actShopOut.getActId());
                ActSign sign = actSignMapper.isSign(actSignInVo);

                if(sign!=null) {
                    Sku sku = skuMapper.selectByPrimaryKey(sign.getSkuId());
                    if (sku != null) {
                        actShopOut.setSkuId(sku.getId());
                        actShopOut.setSkuName(sku.getSkuName());
                        actShopOut.setPicUrl(sku.getPicUrl());
                        actShopOut.setSellPrice(sku.getSellPrice());
                    }
                }
            }
            Collections.sort(list);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<ActShopOut> top(ActShopInVo inVo) {
        List<ActShopOut> list = actShopMapper.list(inVo);
        Collections.sort(list);
        return list;
    }

    @Override
    public Long add(ActShop actShop) {
        if(actShop==null||actShop.getActId()==null){
            return null;
        }
        int i = actShopMapper.countByActId(actShop.getActId());
        DecimalFormat mFormat = new DecimalFormat("000");//确定格式，把1转换为001
        String s = mFormat.format(i+1);
        actShop.setShopCode(s);
        int ret = actShopMapper.insert(actShop);
        if(ret > 0){
            return actShop.getId();
        }

        return null;
    }

    @Override
    public ActShop findByInVo(ActShopInVo inVo){
        ActShop byInVo = null;
        try {
            byInVo = actShopMapper.findByInVo(inVo);//主要是为了防止脏数据，其实也可以直接查询单个
            if (byInVo == null) {
                return null;
            }
            return byInVo;
        }catch (Exception e){
            logger.error("查询活动商家异常TooManyResultException ：" + e.getMessage());
            return new ActShop();
        }
    }

    @Override
    public List<ActShopByShopIdOut> listForActByShopId(ActShopInVo inVo) {
        List<ActShopByShopIdOut> res = actShopMapper.listForActByShopId(inVo);
        Collections.sort(res);
        return res;
    }

    @Override
    public Integer searchForShopId(Long shopId) {
        return actShopMapper.searchForShopId(shopId);
    }

    @Override
    public Integer searchForShopIdNew(ActShopInVo inVo) {
        return actShopMapper.searchForShopIdNew(inVo);
    }
}
