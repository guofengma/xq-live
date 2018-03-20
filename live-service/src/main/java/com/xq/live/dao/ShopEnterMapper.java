package com.xq.live.dao;

import com.xq.live.model.ShopEnter;
import com.xq.live.vo.out.ShopEnterOut;

public interface ShopEnterMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopEnter record);

    int insertSelective(ShopEnter record);

    ShopEnter selectByPrimaryKey(Long id);

    ShopEnterOut selectByUserId(Long id);

    int updateByPrimaryKeySelective(ShopEnter record);

    int updateByPrimaryKey(ShopEnter record);
}
