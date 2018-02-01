package com.xq.live.service.impl;/**
 * 商家sevice实现类
 *
 * @author zhangpeng32
 * @create 2018-01-17 17:57
 */

import com.xq.live.dao.ShopMapper;
import com.xq.live.model.Shop;
import com.xq.live.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商家sevice实现类
 * @author zhangpeng32
 * @create 2018-01-17 17:57
 **/
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Override
    public Shop getShopById(Long id){
        return shopMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long addShop(Shop shop){
        return shopMapper.insert(shop);
    }

    /**
     * 更新一条商家记录
     * @param shop
     * @return
     */
    @Override
    public int updateShop(Shop shop){
        return shopMapper.updateByPrimaryKey(shop);
    }

    /**
     * 根据id删除一条商家记录
     * @param id
     * @return
     */
    @Override
    public int deleteShopById(Long id){
        return shopMapper.deleteByPrimaryKey(id);
    }
}
