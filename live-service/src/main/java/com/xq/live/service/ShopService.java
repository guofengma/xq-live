package com.xq.live.service;/**
 * 商家/餐厅service
 *
 * @author zhangpeng32
 * @create 2018-01-17 17:56
 */

import com.xq.live.model.Shop;

/**
 * 商家/餐厅service
 * @author zhangpeng32
 * @create 2018-01-17 17:56
 **/
public interface ShopService {
    /**
     * 根据id查询商家信息
     * @param id
     * @return
     */
    public Shop getShopById(Long id);

    /**
     * 新增商家
     * @param shop
     * @return
     */
    public Long addShop(Shop shop);

    /**
     * 更新一条商家记录
     * @param shop
     * @return
     */
    public  int updateShop(Shop shop);

    /**
     * 根据id删除一条商家记录
     * @param id
     * @return
     */
    public  int deleteShopById(Long id);
}
