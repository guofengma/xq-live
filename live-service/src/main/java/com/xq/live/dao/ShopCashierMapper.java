package com.xq.live.dao;

import com.xq.live.model.ShopCashier;
import com.xq.live.vo.in.ShopCashierInVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopCashierMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopCashierInVo record);

    int insertSelective(ShopCashierInVo record);

    ShopCashier selectByPrimaryKey(Long id);

    ShopCashier adminByShopId(Long shopId);

    ShopCashier isHave(ShopCashierInVo inVo);

    int updateByPrimaryKeySelective(ShopCashierInVo record);

    int updateByPrimaryKey(ShopCashierInVo record);
}
