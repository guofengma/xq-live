package com.xq.live.service;

import com.xq.live.vo.out.PromotionRulesOut;

import java.util.List;

/**
 * Created by lipeng on 2018/3/30.
 */
public interface PromotionRulesService {

    /**
     * 根据商家id查询商家所需券规则
     * @param shopId
     * @return
     */
    List<PromotionRulesOut> selectByShopId(Integer shopId);
}
