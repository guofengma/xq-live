package com.xq.live.service;

import com.xq.live.model.Shop;
import com.xq.live.model.ShopEnter;
import com.xq.live.vo.out.ShopEnterOut;

import java.util.List;

/**
 * Created by lipeng on 2018/3/8.
 */
public interface ShopEnterService {

    Long add(ShopEnter shopEnter);

    /**
     * 根据userId查询用户的商家入驻信息
     * @param userId
     * @return
     */
    ShopEnterOut searchByUserId(Long userId);

    /**
     * 根据userId查询入驻信息,加入到商家表中
     * @param shopEnter
     * @return
     */
    Integer addShop(ShopEnter shopEnter);

    /**
     * 查看用户入驻信息
     * @param shopEnter
     * @return
     */
    ShopEnterOut selectByUserIdAndShopName(ShopEnter shopEnter);

    /**
     * 删除审批不通过的商家入驻信息
     * @param shopEnter
     * @return
     */
    Integer deleteShopEnterForStatus(ShopEnter shopEnter);

    /**
     * 根据店名查询是否有相同店子
     * @param shopName
     * @return
     */
    Shop searchByShopName(String shopName);
}
