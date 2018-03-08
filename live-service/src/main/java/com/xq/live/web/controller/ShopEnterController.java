package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ShopEnter;
import com.xq.live.service.ShopEnterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by lipeng on 2018/3/8.
 */
@RestController
@RequestMapping(value = "shopEnter")
public class ShopEnterController {

    @Autowired
    private ShopEnterService shopEnterService;

    @RequestMapping(value = "add",method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid ShopEnter shopEnter, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        ShopEnter sp = shopEnterService.selectByUserNameAndShopNameAndMobileAndAddress(shopEnter);
        if(sp!=null){
            return new BaseResp<Long>(0, "该商家已入驻", null);
        }
        Long skuId = shopEnterService.add(shopEnter);
        return new BaseResp<Long>(ResultStatus.SUCCESS, skuId);
    }

}
