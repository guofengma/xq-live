package com.xq.live.dao;

import com.xq.live.model.PromotionRules;
import com.xq.live.vo.in.ProRuInVo;

public interface PromotionRulesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PromotionRules record);

    int insertSelective(PromotionRules record);

    PromotionRules selectByPrimaryKey(Long id);

    PromotionRules selectBySkuIdAndSkuCode(ProRuInVo inVo);

    int updateByPrimaryKeySelective(PromotionRules record);

    int updateByPrimaryKey(PromotionRules record);
}
