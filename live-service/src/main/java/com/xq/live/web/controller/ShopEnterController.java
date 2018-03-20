package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ShopEnter;
import com.xq.live.service.ShopEnterService;
import com.xq.live.service.UserService;
import com.xq.live.vo.out.ShopEnterOut;
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
 * Created by lipeng on 2018/3/8.
 */
@RestController
@RequestMapping(value = "/shopEnter")
public class ShopEnterController {

    @Autowired
    private ShopEnterService shopEnterService;


    @Autowired
    private UserService userService;


    /**
     * 商家入驻---新增一条记录
     * @param shopEnter
     * @param result
     * @return
     */
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid ShopEnter shopEnter, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        ShopEnterOut shopEnterOut = shopEnterService.searchByUserId(shopEnter.getUserId());
        if(shopEnterOut!=null){
            return new BaseResp<Long>(ResultStatus.FAIL,shopEnter.getUserId());
        }
        Long skuId = shopEnterService.add(shopEnter);
        return new BaseResp<Long>(ResultStatus.SUCCESS, skuId);
    }

    /**
     * 通过userId查询一条商家入驻信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/searchByUserId111",method = RequestMethod.GET)
    public BaseResp<ShopEnterOut> searchByUserId(Long userId){
        if(userId==null){
            return new BaseResp<ShopEnterOut>(ResultStatus.error_param_empty);
        }
        ShopEnterOut shopEnterOut = shopEnterService.searchByUserId(userId);
        return new BaseResp<ShopEnterOut>(ResultStatus.SUCCESS,shopEnterOut);
    }

    /**
     * 判断用户商家入驻是否成功
     * @param userId
     * @return
     */
    @RequestMapping(value = "isShopUser",method = RequestMethod.GET)
    public BaseResp<Long> isShopUser(Long userId){
        if(userId==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        ShopEnterOut shopEnterOut = shopEnterService.searchByUserId(userId);
        if(shopEnterOut==null){
            return new BaseResp<Long>(-1,"该用户未申请商家入驻",null);
        }
        if(shopEnterOut.getStatus()==0){
            return new BaseResp<Long>(-1,"该用户申请待审批",null);
        }
        if(shopEnterOut.getStatus()==2){
            return new BaseResp<Long>(-1,"该用户申请不通过",null);
        }


        return new BaseResp<Long>(0,"申请通过",userId);
    }



}
