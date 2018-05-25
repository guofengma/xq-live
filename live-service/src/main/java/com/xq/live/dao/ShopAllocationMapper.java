package com.xq.live.dao;

import com.xq.live.model.ShopAllocation;

public interface ShopAllocationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopAllocation record);

    int insertSelective(ShopAllocation record);

    ShopAllocation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopAllocation record);

    int updateByPrimaryKey(ShopAllocation record);
}