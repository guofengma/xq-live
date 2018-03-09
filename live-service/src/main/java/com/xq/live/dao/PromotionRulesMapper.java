package com.xq.live.dao;

import com.xq.live.model.PromotionRules;

public interface PromotionRulesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PromotionRules record);

    int insertSelective(PromotionRules record);

    PromotionRules selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PromotionRules record);

    int updateByPrimaryKey(PromotionRules record);
}