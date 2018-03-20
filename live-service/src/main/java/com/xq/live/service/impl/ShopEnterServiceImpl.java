package com.xq.live.service.impl;

import com.xq.live.dao.ShopEnterMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.ShopEnter;
import com.xq.live.model.User;
import com.xq.live.service.ShopEnterService;
import com.xq.live.vo.out.ShopEnterOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lipeng on 2018/3/8.
 */
@Service
public class ShopEnterServiceImpl implements ShopEnterService{
    @Autowired
    private ShopEnterMapper shopEnterMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Long add(ShopEnter shopEnter) {
        int res = shopEnterMapper.insert(shopEnter);
        if(res < 1){
            return null;
        }
        return shopEnter.getId();
    }

    @Override
    public ShopEnterOut searchByUserId(Long userId) {
        ShopEnterOut shopEnterOut = shopEnterMapper.selectByUserId(userId);
        if(shopEnterOut!=null&&shopEnterOut.getStatus()==1){
            User user = userMapper.selectByPrimaryKey(userId);
            String aa = "你好不好";
            String[] arr = aa.split("好");
            //userMapper.updateUserType(user);
        }
        return shopEnterOut;
    }
}
