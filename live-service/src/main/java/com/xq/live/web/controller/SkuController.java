package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Sku;
import com.xq.live.service.SkuService;
import com.xq.live.vo.in.SkuInVo;
import com.xq.live.vo.out.SkuForTscOut;
import com.xq.live.vo.out.SkuOut;
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
 * SKU controller
 *
 * @author zhangpeng32
 * @date 2018-02-09 10:31
 * @copyright:hbxq
 **/
@RestController
@RequestMapping(value = "/sku")
public class SkuController {

    @Autowired
    private SkuService skuService;

    /**
     * 查一条记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<SkuOut> get(@PathVariable("id") Long id){
        SkuOut skuOut = skuService.selectById(id);
        return new BaseResp<SkuOut>(ResultStatus.SUCCESS, skuOut);
    }

    /**
     * 分页查询列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<SkuOut>> list(SkuInVo inVo){
        inVo.setSkuType(Sku.SKU_TYPE_XQQ);
        Pager<SkuOut> result = skuService.list(inVo);
        return new BaseResp<Pager<SkuOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 分页查询列表(针对活动券)
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listForAct", method = RequestMethod.GET)
    public BaseResp<Pager<SkuOut>> listForAct(SkuInVo inVo){
        inVo.setSkuType(Sku.SKU_TYPE_HDQ);
        Pager<SkuOut> result = skuService.list(inVo);
        return new BaseResp<Pager<SkuOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     *  分页查询列表(针对折扣券)
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listForAgio", method = RequestMethod.GET)
    public BaseResp<Pager<SkuOut>> listForAgio(SkuInVo inVo){
        inVo.setSkuType(Sku.SKU_TYPE_AGIO);
        Pager<SkuOut> result = skuService.list(inVo);
        return new BaseResp<Pager<SkuOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查热门
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<SkuOut>> top(SkuInVo inVo){
        List<SkuOut> result = skuService.top(inVo);
        return new BaseResp<List<SkuOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 新增sku
     * @param sku
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid Sku sku, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long skuId = skuService.add(sku);
        return new BaseResp<Long>(ResultStatus.SUCCESS, skuId);
    }

    /**
     * 查询单个推荐菜
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/getTscForZan",method = RequestMethod.GET)
    public BaseResp<SkuForTscOut> getTscForZan(SkuInVo inVo){
        if(inVo==null||inVo.getId()==null||inVo.getZanUserId()==null){
            return new BaseResp<SkuForTscOut>(ResultStatus.error_param_empty);
        }
        if(inVo.getShopId() == null){
            return new BaseResp<SkuForTscOut>(ResultStatus.error_param_shop_id_empty);
        }
        inVo.setSkuType(Sku.SKU_TYPE_TSC);
        SkuForTscOut tscForZan = skuService.getTscForZan(inVo);
        return  new BaseResp<SkuForTscOut>(ResultStatus.SUCCESS,tscForZan);
    }

    /**
     * 分页特色菜列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/tsc", method = RequestMethod.GET)
    public BaseResp<Pager<SkuForTscOut>> tscList(SkuInVo inVo){
        if(inVo == null){
            return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.error_param_empty);
        }

        if(inVo.getShopId() == null){
            return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.error_param_shop_id_empty);
        }
        inVo.setSkuType(Sku.SKU_TYPE_TSC);
        Pager<SkuForTscOut> result = skuService.queryTscList(inVo);
        return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询单个套餐
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/getAgio",method = RequestMethod.GET)
    public BaseResp<SkuForTscOut> getAgio(SkuInVo inVo){
        if(inVo==null||inVo.getId()==null||inVo.getZanUserId()==null){
            return new BaseResp<SkuForTscOut>(ResultStatus.error_param_empty);
        }
        if(inVo.getShopId() == null){
            return new BaseResp<SkuForTscOut>(ResultStatus.error_param_shop_id_empty);
        }
        inVo.setSkuType(Sku.SKU_TYPE_SJTC);
        SkuForTscOut tscForZan = skuService.getTscForZan(inVo);
        return  new BaseResp<SkuForTscOut>(ResultStatus.SUCCESS,tscForZan);
    }

    /**
     * 分页套餐列表
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/agioList", method = RequestMethod.GET)
    public BaseResp<Pager<SkuForTscOut>> agioList(SkuInVo inVo){
        if(inVo == null){
            return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.error_param_empty);
        }

        if(inVo.getShopId() == null){
            return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.error_param_shop_id_empty);
        }
        inVo.setSkuType(Sku.SKU_TYPE_SJTC);
        Pager<SkuForTscOut> result = skuService.queryTscList(inVo);
        return new BaseResp<Pager<SkuForTscOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 判断是否是首次下单用户 0新用户 其他 老用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "/isNewUser",method = RequestMethod.GET)
    public BaseResp<Integer> isNewUser(Long userId){
        if(userId==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        Integer newUser = skuService.isNewUser(userId);
        if(newUser==0){
            return new BaseResp<Integer>(ResultStatus.SUCCESS,0);
        }
        return new BaseResp<Integer>(ResultStatus.FAIL,newUser);
    }
}
