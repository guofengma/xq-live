package com.xq.live.service.impl;/**
 * 商家sevice实现类
 *
 * @author zhangpeng32
 * @create 2018-01-17 17:57
 */

import com.xq.live.common.Pager;
import com.xq.live.dao.ShopMapper;
import com.xq.live.model.Shop;
import com.xq.live.model.User;
import com.xq.live.service.ShopService;
import com.xq.live.vo.in.ShopInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商家sevice实现类
 *
 * @author zhangpeng32
 * @create 2018-01-17 17:57
 **/
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopMapper shopMapper;

    @Override
    public Shop getShopById(Long id) {
        return shopMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long addShop(Shop shop) {
        int r = shopMapper.insert(shop);
        if(r < 1){
            return null;
        }
        return shop.getId();
    }

    /**
     * 更新一条商家记录
     *
     * @param shop
     * @return
     */
    @Override
    public int updateShop(Shop shop) {
        return shopMapper.updateByPrimaryKeySelective(shop);
    }

    /**
     * 根据id删除一条商家记录
     *
     * @param id
     * @return
     */
    @Override
    public int deleteShopById(Long id) {
        return shopMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Pager<Shop> list(ShopInVo inVo){
        Pager<Shop> result = new Pager<Shop>();
        int listTotal = shopMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if(listTotal > 0){
            List<Shop> list = shopMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<Shop> top(ShopInVo inVo){
        return shopMapper.list(inVo);
    }
}
