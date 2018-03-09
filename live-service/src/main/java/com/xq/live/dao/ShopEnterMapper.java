package com.xq.live.dao;

import com.xq.live.model.Shop;
import com.xq.live.model.ShopEnter;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopEnterMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopEnter record);

    int insertSelective(ShopEnter record);

    ShopEnter selectByPrimaryKey(Long id);

    ShopEnter selectByUserNameAndShopNameAndMobileAndAddress(ShopEnter record);

    ShopEnter selectByToken(ShopEnter record);

    int updateByPrimaryKeySelective(ShopEnter record);

    int updateByPrimaryKey(ShopEnter record);
}
