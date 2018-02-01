package com.xq.live.dao;

import com.xq.live.model.Shop;

public interface ShopMapper {
    int deleteByPrimaryKey(Long id);

    Long insert(Shop record);

    int insertSelective(Shop record);

    Shop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Shop record);

    int updateByPrimaryKey(Shop record);
}