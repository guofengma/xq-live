package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.ActShopMapper;
import com.xq.live.model.ActShop;
import com.xq.live.service.ActShopService;
import com.xq.live.vo.in.ActShopInVo;
import com.xq.live.vo.out.ActInfoOut;
import com.xq.live.vo.out.ActShopByShopIdOut;
import com.xq.live.vo.out.ActShopOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Pager<ActShopOut> list(ActShopInVo inVo) {
        Pager<ActShopOut> result = new Pager<ActShopOut>();
        int listTotal = actShopMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<ActShopOut> list = actShopMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<ActShopOut> top(ActShopInVo inVo) {
        return actShopMapper.list(inVo);
    }

    @Override
    public Long add(ActShop actShop) {
        int ret = actShopMapper.insert(actShop);
        if(ret > 0){
            return actShop.getId();
        }

        return null;
    }

    @Override
    public ActShop findByInVo(ActShopInVo inVo){
        return actShopMapper.findByInVo(inVo);
    }

    @Override
    public List<ActShopByShopIdOut> listForActByShopId(ActShopInVo inVo) {
        List<ActShopByShopIdOut> res = actShopMapper.listForActByShopId(inVo);
        Collections.sort(res);
        return res;
    }
}
