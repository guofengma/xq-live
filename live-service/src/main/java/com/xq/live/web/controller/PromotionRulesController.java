package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.service.PromotionRulesService;
import com.xq.live.vo.out.PromotionRulesOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 平台卷与商家规则
 * Created by lipeng on 2018/3/30.
 */
@RestController
@RequestMapping("/pnr")
public class PromotionRulesController {

    @Autowired
    private PromotionRulesService promotionRulesService;

    /**
     * 根据shopId查询商家所需的卷规则
     * @param shopId
     * @return
     */
    @RequestMapping(value = "/selectByShopId",method = RequestMethod.GET)
    public BaseResp<List<PromotionRulesOut>> selectByShopId(Integer shopId){
        if(shopId==null){
            return new BaseResp<List<PromotionRulesOut>>(ResultStatus.error_param_empty);
        }
        List<PromotionRulesOut> promotionRulesOuts = promotionRulesService.selectByShopId(shopId);
        return new BaseResp<List<PromotionRulesOut>>(ResultStatus.SUCCESS,promotionRulesOuts);
    }


}
