package com.xq.live.web.controller;/**
 * 商家controller
 *
 * @author zhangpeng32
 * @create 2018-01-17 18:01
 */

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Shop;
import com.xq.live.service.ShopService;
import com.xq.live.vo.in.ShopInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 商家controller
 *
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
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<Shop> getShopById(@PathVariable(value = "id") Long id) {
        Shop result = shopService.getShopById(id);
        return new BaseResp<Shop>(ResultStatus.SUCCESS, result);
    }

    /**
     * 新增一条商家记录
     *
     * @param shop
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid Shop shop, BindingResult result) {
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id = shopService.addShop(shop);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 删除一条商家记录
     *
     * @param id
     * @return
     */
/*    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public BaseResp<Integer> delete(@PathVariable(value = "id") Long id) {
        int result = shopService.deleteShopById(id);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }*/

    /**
     * 删除一条商家记录
     *
     * @param shop
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(Shop shop) {
        int result = shopService.updateShop(shop);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 根据条件查询商家列表信息
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<Shop>> list(ShopInVo inVo){
        Pager<Shop> result = shopService.list(inVo);
        return new BaseResp<Pager<Shop>>(ResultStatus.SUCCESS, result);
    }

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<Shop>> top(ShopInVo inVo){
        List<Shop> result = shopService.top(inVo);
        return new BaseResp<List<Shop>>(ResultStatus.SUCCESS, result);
    }
}
