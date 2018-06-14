package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.service.ActSkuService;
import com.xq.live.vo.in.ActSkuInVo;
import com.xq.live.vo.out.ActSkuOut;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 推荐菜活动Service实现类
 * Created by lipeng on 2018/6/13.
 */
@Service
public class ActSkuServiceImpl implements ActSkuService{
    @Override
    public Pager<ActSkuOut> listForNewAct(ActSkuInVo inVo) {
        /*Pager<ActUserOut> result = new Pager<ActUserOut>();
        int listTotal = actUserMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<ActUserOut> list = actUserMapper.listForNewAct(inVo);
            for (ActUserOut actUserOut : list) {
                actUserOut = getPicUrls(actUserOut);
            }
            //如果是查询分组的，把分组的关联信息加入进去
            if(inVo.getType()!=null&&inVo.getType()== ActUser.ACT_USER_GROUP){
                for (ActUserOut actUserOut : list) {
                    Shop shop = shopMapper.selectByPrimaryKey(actUserOut.getShopId());
                    actUserOut.setLogoUrl(shop.getLogoUrl());
                    actUserOut.setShopName(shop.getShopName());


                    ActSignInVo actSignInVo = new ActSignInVo();
                    actSignInVo.setType(ActSign.ACT_SIGN_TYPE_SHOP);
                    actSignInVo.setRefId(shop.getId());
                    actSignInVo.setActId(actUserOut.getActId());
                    ActSign sign = actSignMapper.isSign(actSignInVo);

                    if(sign!=null) {
                        Sku sku = skuMapper.selectByPrimaryKey(sign.getSkuId());
                        if (sku != null) {
                            actUserOut.setSkuName(sku.getSkuName());
                        }
                    }
                }
            }
            //Collections.sort(list);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;*/
        return null;
    }
}
