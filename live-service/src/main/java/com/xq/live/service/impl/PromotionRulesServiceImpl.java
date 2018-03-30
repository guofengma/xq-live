package com.xq.live.service.impl;

import com.xq.live.dao.PromotionRulesMapper;
import com.xq.live.service.PromotionRulesService;
import com.xq.live.vo.out.PromotionRulesOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lipeng on 2018/3/30.
 */
@Service
public class PromotionRulesServiceImpl implements PromotionRulesService{

    @Autowired
    private PromotionRulesMapper promotionRulesMapper;

    @Override
    public List<PromotionRulesOut> selectByShopId(Integer shopId) {
        List<PromotionRulesOut> promotionRulesOuts = promotionRulesMapper.selectByShopId(shopId);
        return promotionRulesOuts;
    }
}
