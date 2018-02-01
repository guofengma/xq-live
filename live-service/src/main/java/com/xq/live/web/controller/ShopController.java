package com.xq.live.web.controller;/**
 * 商家controller
 *
 * @author zhangpeng32
 * @create 2018-01-17 18:01
 */

import com.xq.live.model.Shop;
import com.xq.live.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商家controller
 * @author zhangpeng32
 * @create 2018-01-17 18:01
 **/
@RestController
@RequestMapping(value = "/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 根据商家id查询商家信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}",  method = RequestMethod.GET)
    public Shop  getShopById(@PathVariable(value="id") Long id){
        Shop result =shopService.getShopById(id);
        return result;
    }

    /**
     * 新增一条商家记录
     * @param shop
     * @return
     */
    @RequestMapping(value = "/put",  method = RequestMethod.POST)
    public Long  put(Shop shop){
        Long result = shopService.addShop(shop);
        return result;
    }

    /**
     * 删除一条商家记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}",  method = RequestMethod.DELETE)
    public int  delete(@PathVariable(value="id") Long id){
        int result = shopService.deleteShopById(id);
        return result;
    }

    /**
     * 删除一条商家记录
     * @param shop
     * @return
     */
    @RequestMapping(value = "/update",  method = RequestMethod.PUT)
    public int  update(Shop shop){
        int result = shopService.updateShop(shop);
        return result;
    }
}
