package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.dao.FavoritesMapper;
import com.xq.live.model.Favorites;
import com.xq.live.model.Shop;
import com.xq.live.service.FavoritesService;
import com.xq.live.service.ShopService;
import com.xq.live.vo.in.FavoritesInVo;
import com.xq.live.vo.out.ShopOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by lipeng on 2018/3/4.
 */
@RestController
@RequestMapping(value = "/fvs")
public class FavoritesController {

    @Autowired
    private FavoritesService favoritesService;


    /**
     * 根据主键查询一条记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<Favorites> get(@PathVariable("id") Long id) {
        Favorites cp = favoritesService.get(id);
        return new BaseResp<Favorites>(ResultStatus.SUCCESS, cp);
    }


    /**
     * 根据用户id查询收藏列表，并且查询商家详情 分页查询
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<Shop>> list(FavoritesInVo inVo) {
        Pager<Shop> result = favoritesService.list(inVo);
        return new BaseResp<Pager<Shop>>(ResultStatus.SUCCESS, result);
    }


    /**
     * 根据用户id和商家id,增加一条记录到收藏列表中
     *
     * @param favorites
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> put(@Valid Favorites favorites, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Boolean collected = favoritesService.isCollected(favorites);
        if(collected==true){
            return new BaseResp<Long>(0, "该商品已在商品列表", null);
        }

        Long id = favoritesService.add(favorites);
        if (id == null) {
            return new BaseResp<Long>(ResultStatus.FAIL, id);
        }
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 根据用户id查询收藏列表，并且查询商家详情 分页查询(修改版)
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/getSCForList", method = RequestMethod.GET)
    public BaseResp<Pager<ShopOut>> SCForList(FavoritesInVo inVo) {
        Pager<ShopOut> result = favoritesService.getSCForList(inVo);
        return new BaseResp<Pager<ShopOut>>(ResultStatus.SUCCESS, result);
    }


    /**
     * 根据用户id和商家id,删除一条记录
     *
     * @param favorites
     * @param result
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResp<Integer> delete(@Valid Favorites favorites, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Integer>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Integer res = favoritesService.delete(favorites);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, res);
    }

    /**
     * 根据商家id查询商家详细信息
     */
    @RequestMapping(value = "/shopDetail", method = RequestMethod.GET)
    public BaseResp<Shop> shopDetail(@RequestParam(value = "shopId") Long shopId) {
        Shop res = favoritesService.shopDetail(shopId);
        return new BaseResp<Shop>(ResultStatus.SUCCESS, res);
    }

    /**
     * 根据用户id和商家id，查询是否已经收藏
     */
    @RequestMapping(value = "/isCollected", method = RequestMethod.POST)
    public BaseResp<Boolean> isCollected(Favorites favorites) {
        Boolean res = favoritesService.isCollected(favorites);
        return new BaseResp<Boolean>(ResultStatus.SUCCESS, res);
    }

    /**
     * 根据用户id查询是否有动态消息
     * @param favorites
     * @return
     */
    @RequestMapping(value = "isActive",method = RequestMethod.GET)
    public BaseResp<Boolean> isActive(Favorites favorites){
        Boolean res = favoritesService.isActive(favorites);
        return new BaseResp<Boolean>(ResultStatus.SUCCESS, res);
    }

}
