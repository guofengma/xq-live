package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.FreeSkuConfig;
import com.xq.live.model.So;
import com.xq.live.service.SoService;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.out.SoForOrderOut;
import com.xq.live.vo.out.SoOut;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单controller
 *
 * @author lipeng
 * @date 2018-02-09 14:24
 * @copyright:hbxq
 **/
@RestController
@RequestMapping(value = "/app/so")
public class SoForAppController {

    @Autowired
    private SoService soService;

    @Autowired
    private FreeSkuConfig freeSkuConfig;

    /**
     * 查一条记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<SoOut> get(@PathVariable("id") Long id) {
        SoOut soOut = soService.get(id);
        return new BaseResp<SoOut>(ResultStatus.SUCCESS, soOut);
    }

    /**
     * 查询我的订单中的订单详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getForOrder/{id}", method = RequestMethod.GET)
    public BaseResp<SoForOrderOut> getForOrder(@PathVariable("id") Long id) {
        SoForOrderOut forOrder = soService.getForOrder(id);
        return new BaseResp<SoForOrderOut>(ResultStatus.SUCCESS, forOrder);
    }

    /**
     * 分页查询列表
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<SoOut>> list(SoInVo inVo) {
        Pager<SoOut> result = soService.list(inVo);
        return new BaseResp<Pager<SoOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查我的订单
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/myorder", method = RequestMethod.GET)
    public BaseResp<List<SoOut>> top(SoInVo inVo) {
        List<SoOut> result = soService.findSoList(inVo);
        return new BaseResp<List<SoOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询商家端中平台代收的营业额
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/totalAmount",method = RequestMethod.GET)
    public BaseResp<BigDecimal> totalAmount(SoInVo inVo){
        BigDecimal bigDecimal = soService.totalAmount(inVo);
        return  new BaseResp<BigDecimal>(ResultStatus.SUCCESS,bigDecimal);
    }

    /**
     * 查我的商家订单
     *查询我的订单中的商家订单，基本参数与平台订单相同  userId ,page,rows,soStatus
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/myorderForShop", method = RequestMethod.GET)
    public BaseResp<List<SoOut>> myorderForShop(SoInVo inVo) {
        inVo.setSoType(So.SO_TYPE_SJ);
        List<SoOut> result = soService.findSoListForShop(inVo);
        return new BaseResp<List<SoOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 生成订单
     *
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseResp<Long> create(@Valid SoInVo inVo, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id = soService.create(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 新用户首单免费
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/freeOrder", method = RequestMethod.POST)
    public BaseResp<Long> freeOrder(@Valid SoInVo inVo, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        Integer soOutNum = soService.selectByUserIdTotal(inVo.getUserId());
        if(soOutNum > 0){
            return new BaseResp<Long>(ResultStatus.error_user_not_new);
        }
        inVo.setSkuId(freeSkuConfig.getSkuId());
        inVo.setSkuNum(freeSkuConfig.getSkuNum());

        Long id = soService.freeOrder(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 订单支付
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/paid", method = RequestMethod.POST)
    public BaseResp<Integer> paid(SoInVo inVo) {
        //入参校验
        if (inVo.getId() == null || inVo.getUserId() == null || StringUtils.isEmpty(inVo.getUserName())) {
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        //订单不存在
        SoOut soOut = soService.get(inVo.getId());
        if (soOut == null) {
            return new BaseResp<Integer>(ResultStatus.error_so_not_exist);
        }

        //订单不是待支付状态，不能修改支付状态
        if(soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID){
            return new BaseResp<Integer>(ResultStatus.error_so_not_wait_pay);
        }
        inVo.setSkuId(soOut.getSkuId());
        int ret = soService.paid(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, ret);
    }

    /**
     * 订单取消
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public BaseResp<Integer> cancel(SoInVo inVo) {
        //入参校验
        if (inVo.getId() == null || inVo.getUserId() == null || StringUtils.isEmpty(inVo.getUserName())) {
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }
        //订单不存在
        SoOut soOut = soService.get(inVo.getId());
        if (soOut == null) {
            return new BaseResp<Integer>(ResultStatus.error_so_not_exist);
        }

        //只有待支付的订单才能取消
        if(soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID){
            return new BaseResp<Integer>(ResultStatus.error_so_cancel_status_error);
        }

        int ret = soService.cancel(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, ret);
    }

}
