package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ActShop;
import com.xq.live.service.ActShopService;
import com.xq.live.vo.in.ActShopInVo;
import com.xq.live.vo.out.ActShopOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 商家活动controller
 *
 * @author zhangpeng32
 * @date 2018-03-06 21:09
 * @copyright:hbxq
 **/
@RestController
@RequestMapping("/actshop")
public class ActShopController {

    @Autowired
    private ActShopService actShopService;

    /**
     * 分页查询参加活动的商家列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<ActShopOut>> list(ActShopInVo inVo){
        Pager<ActShopOut> result = actShopService.list(inVo);
        return new BaseResp<Pager<ActShopOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询参加活动的商家列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<ActShopOut>> top(ActShopInVo inVo){
        List<ActShopOut> result = actShopService.top(inVo);
        return new BaseResp<List<ActShopOut>>(ResultStatus.SUCCESS, result);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid ActShop actShop, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        //判断商家是否已经报名了活动
        ActShopInVo inVo = new ActShopInVo();
        inVo.setActId(actShop.getActId());
        inVo.setShopId(actShop.getShopId());
        ActShop as = actShopService.findByInVo(inVo);
        if(as != null){
            return new BaseResp<Long>(ResultStatus.error_act_shop_exist);
        }

        Long id = actShopService.add(actShop);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }
}
