package com.xq.live.service.impl;

import com.xq.live.dao.ShopEnterMapper;
import com.xq.live.model.ShopEnter;
import com.xq.live.service.ShopEnterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lipeng on 2018/3/8.
 */
@Service
public class ShopEnterServiceImpl implements ShopEnterService{
    @Autowired
    private ShopEnterMapper shopEnterMapper;

    @Override
    public Long add(ShopEnter shopEnter) {
        int res = shopEnterMapper.insert(shopEnter);
        if(res < 1){
            return null;
        }
        return shopEnter.getId();
    }

    @Override
    public ShopEnter selectByUserNameAndShopNameAndMobileAndAddress(ShopEnter shopEnter) {
        ShopEnter res = shopEnterMapper.selectByUserNameAndShopNameAndMobileAndAddress(shopEnter);
        return res;
    }
}
