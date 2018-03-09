package com.xq.live.service;

import com.xq.live.model.ShopEnter;

/**
 * Created by lipeng on 2018/3/8.
 */
public interface ShopEnterService {

    Long add(ShopEnter shopEnter);

    ShopEnter selectByUserNameAndShopNameAndMobileAndAddress(ShopEnter shopEnter);

    ShopEnter selectByToken(ShopEnter shopEnter);
}
