package com.xq.live.dao;

import com.xq.live.model.ShopEnter;
import com.xq.live.vo.out.ShopEnterOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopEnterMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopEnter record);

    int insertSelective(ShopEnter record);

    ShopEnter selectByPrimaryKey(Long id);

    ShopEnterOut selectByUserIdAndShopName(ShopEnter record);

    List<ShopEnterOut> selectByUserId(Long id);

    int updateByPrimaryKeySelective(ShopEnter record);

    int updateByPrimaryKey(ShopEnter record);
}
