package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.common.RandomStringUtil;
import com.xq.live.dao.SkuMapper;
import com.xq.live.dao.SoMapper;
import com.xq.live.model.Sku;
import com.xq.live.service.SkuService;
import com.xq.live.vo.in.SkuInVo;
import com.xq.live.vo.out.SkuForTscOut;
import com.xq.live.vo.out.SkuOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
            if(inVo!=null&&inVo.getUserId()!=null){
                int i = soMapper.selectByUserIdTotal(inVo.getUserId());//判断是否是新下单用户 0为首次下单
                if(i==0){
                    for (SkuOut skuOut : list) {
                        skuOut.setSellPrice(BigDecimal.ZERO);
                    }
                }
            }
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
    public List<SkuOut> top(SkuInVo inVo) {
        return skuMapper.list(inVo);
    }

    @Override
    public Long add(Sku sku) {
        sku.setSkuCode(RandomStringUtil.getRandomCode(8, 0));
        int res = skuMapper.insert(sku);
        if(res < 1){
            return null;
        }
        return sku.getId();
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
}
