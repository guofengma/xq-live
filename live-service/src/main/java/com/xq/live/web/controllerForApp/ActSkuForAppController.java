package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.ActProSku;
import com.xq.live.model.PromotionRules;
import com.xq.live.model.Sku;
import com.xq.live.service.ActSkuService;
import com.xq.live.service.PromotionRulesService;
import com.xq.live.service.SkuService;
import com.xq.live.vo.in.ActSkuInVo;
import com.xq.live.vo.out.ActSkuOut;
import com.xq.live.vo.out.PromotionRulesOut;
import com.xq.live.vo.out.SkuOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 推荐菜活动接口
 * Created by lipeng on 2018/6/13.
 */
@RestController
@RequestMapping(value = "/app/actSku")
public class ActSkuForAppController {
    @Autowired
    private ActSkuService actSkuService;

    @Autowired
    private PromotionRulesService promotionRulesService;

    @Autowired
    private SkuService skuService;
    /**
     * 选手报名接口
     * @param inVo
     * @param result
     * @return
     */
    /*@RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid ActUserInVo inVo, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        //判断选手是否已经报名了活动
        ActUserOut byInVo = actUserService.findByInVo(inVo);
        if(byInVo != null){
            return new BaseResp<Long>(ResultStatus.error_act_user_exist,byInVo.getId());
        }

        Long id = actUserService.add(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }*/

    /**
     * 分页查询参与推荐菜列表信息(针对的是新活动，带有开始时间和截止时间，可以多次投票)
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listNewAct", method = RequestMethod.GET)
    public BaseResp<Pager<ActSkuOut>> listNewAct(ActSkuInVo inVo){
        //如果要查最新的话，要传个sortType为1
        //为了分享这个地方注释掉，但是这里是要必传的，判断用户的已投票状态
        /*if(inVo.getVoteUserId()==null){
            return new BaseResp<Pager<ActSkuOut>>(-1,"voteUserId必填", null);
        }*/
        if(inVo.getActId()==null){
            return new BaseResp<Pager<ActSkuOut>>(-1,"actId必填", null);
        }
        if(inVo.getBeginTime()==null||inVo.getEndTime()==null){
            return new BaseResp<Pager<ActSkuOut>>(-1,"时间必填",null);
        }
        Pager<ActSkuOut> result = actSkuService.listForNewAct(inVo);
        return new BaseResp<Pager<ActSkuOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询单个详情页的活动推荐菜信息
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/findByInVo",method = RequestMethod.GET)
    public BaseResp<ActSkuOut> findByInVo(ActSkuInVo inVo){
        //此地方是必传，但是这里注释掉是为了分享能正常进行
       /* if(inVo.getVoteUserId()==null){
            return new BaseResp<ActSkuOut>(-1,"voteUserId必填", null);
        }*/
        if(inVo.getActId()==null){
            return new BaseResp<ActSkuOut>(-1,"actId必填", null);
        }
        if(inVo.getBeginTime()==null||inVo.getEndTime()==null){
            return new BaseResp<ActSkuOut>(-1,"时间必填",null);
        }
        if(inVo.getSkuId()==null){
            return new BaseResp<ActSkuOut>(-1,"skuId必填",null);
        }
        ActSkuOut byInVo = actSkuService.findByInVo(inVo);
        return new BaseResp<ActSkuOut>(ResultStatus.SUCCESS,byInVo);
    }

    /**
     * 新增活动推荐菜信息
     * @param proSku
     * @return
     */
    @RequestMapping(value = "/insertByRules",method = RequestMethod.POST)
    public BaseResp<Long> inActInfo(ActProSku proSku){
        if (proSku==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        ActSkuInVo actSkuInVo=new ActSkuInVo();
        actSkuInVo.setActId(proSku.getActId());
        actSkuInVo.setSkuId(proSku.getSkuId());

        ActSkuOut byInVo = actSkuService.findByInVo(actSkuInVo);
        if (byInVo!=null){
            return new BaseResp<Long>(-1,"菜品已报名", null);
        }
        SkuOut skuOut = skuService.selectById(proSku.getSkuId());
        //添加规则
        PromotionRules promotionRules=new PromotionRules();
        promotionRules.setSkuId(proSku.getSkugId());
        promotionRules.setSkuCode(proSku.getSkugCode());
        promotionRules.setJianAmount(proSku.getJianAmount());
        promotionRules.setManAmount(proSku.getManAmount());
        promotionRules.setRuleType(proSku.getRuleType());
        promotionRules.setRuleDesc(proSku.getRuleDesc());
        promotionRules.setShopId(proSku.getShopId());
        promotionRules.setSkuName(proSku.getSkuName());
        Long pid = promotionRulesService.add(promotionRules);
        //添加actsku
        //PromotionRules rules=promotionRulesService.selectByPrimaryKey(pid);
        ActSkuInVo inVo= new ActSkuInVo();
        inVo.setActId(proSku.getActId());
        //inVo.setSkuCode(skuOut.getSkuCode());
        inVo.setSkuId(proSku.getSkuId());
        inVo.setPrId(pid);
        Long actSkuid=actSkuService.insert(inVo);
        if (actSkuid==null){
            return new BaseResp<Long>(-1,"添加菜品失败", null);
        }
        return new BaseResp<Long>(ResultStatus.SUCCESS,actSkuid);
    }

}
