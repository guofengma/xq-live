package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.PaymentConfig;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.FreeSkuConfig;
import com.xq.live.model.So;
import com.xq.live.model.User;
import com.xq.live.model.UserAccount;
import com.xq.live.service.SoService;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.in.WeixinInVo;
import com.xq.live.vo.out.SoForOrderOut;
import com.xq.live.vo.out.SoOut;
import com.xq.live.web.utils.GroupUtil;
import com.xq.live.web.utils.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单controller
 *
 * @author zhangpeng32
 * @date 2018-02-09 14:24
 * @copyright:hbxq
 **/
@RestController
@RequestMapping(value = "/so")
public class SoController {

    @Autowired
    private SoService soService;

    @Autowired
    private UserService userService;


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

        //判断是否是新用户
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
     * 领取活动券
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/freeOrderForAct", method = RequestMethod.POST)
    public BaseResp<Long> freeOrderForAct(@Valid SoInVo inVo, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id = soService.freeOrderForAct(inVo);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     *  领取折扣券
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/freeOrderForAgio", method = RequestMethod.POST)
    public BaseResp<Long> freeOrderForAgio(@Valid SoInVo inVo, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id = soService.freeOrderForAgio(inVo);
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

    //查询用户余额
    @RequestMapping(value = "/doFindAmount", method = RequestMethod.POST)
    public BaseResp<Integer> doFindAmount(UserAccountInVo accountInVo){
        List<UserAccount> list= userService.fingAccountByID(accountInVo.getUserId());
        BigDecimal accountAmount=list.get(0).getAccountAmount();

        return new BaseResp<Integer>(ResultStatus.SUCCESS,accountAmount.intValue());
    }


    //享7平台支付
    @RequestMapping(value = "/doPaymentPlatform", method = RequestMethod.POST)
    public BaseResp<Integer> doPaymentPlatform(@Valid WeixinInVo inVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            return new BaseResp<Integer>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        //根据id获取订单信息
        SoOut soOut = soService.get(inVo.getSoId());
        if (soOut == null) {
            return new BaseResp<Integer>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID) {
            return new BaseResp<Integer>(ResultStatus.error_so_paid);
        }
        //判断用户余额是否可以支付
        List<UserAccount> list= userService.findAccountByUserId(inVo.getSoId());
        BigDecimal accountAmount=list.get(0).getAccountAmount();
        Long userID=list.get(0).getUserId();
        UserAccountInVo account=new UserAccountInVo();
        account.setUserId(userID);
        int i=0;
        if (soOut.getSoAmount().compareTo(accountAmount)<=0){
            account.setAccountAmount(accountAmount.subtract(soOut.getSoAmount()));
            i=userService.updateByUserID(account);
            if (i<1){
                return new BaseResp<Integer>(ResultStatus.error_user_play);
            }else {
                return new BaseResp<Integer>(ResultStatus.SUCCESS,i);
            }
        }else {
            return new BaseResp<Integer>(ResultStatus.error_user_account);
        }
    }

    //支付后平台反红包
    @RequestMapping(value = "/doAddFeedback", method = RequestMethod.POST)
    public BaseResp<Integer> playFeedback(UserAccountInVo accountInVo){
        //获取一个1到10块的红包
        int feedback= GroupUtil.getRandom(1,10);
        //转换成BigDecimal类型
        BigDecimal amount=new BigDecimal(feedback);
        List<UserAccount> list= userService.fingAccountByID(accountInVo.getUserId());
        BigDecimal accountAmount=list.get(0).getAccountAmount();

        int i=0;
        accountInVo.setAccountAmount(amount.add(accountAmount));
        i=userService.updateByUserID(accountInVo);
            if (i<1){
                return new BaseResp<Integer>(ResultStatus.error_user_getaccount);
            }else {
                return new BaseResp<Integer>(ResultStatus.SUCCESS,feedback);
            }
        }

}
