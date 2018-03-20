package com.xq.live.service;

import com.xq.live.model.ShopEnter;
import com.xq.live.vo.out.ShopEnterOut;

/**
 * Created by lipeng on 2018/3/8.
 */
public interface ShopEnterService {

    Long add(ShopEnter shopEnter);

    ShopEnterOut searchByUserId(Long userId);

}
