package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.RandomNumberUtil;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.AccountLog;
import com.xq.live.model.Coupon;
import com.xq.live.model.UserAccount;
import com.xq.live.service.AccountService;
import com.xq.live.service.CouponService;
import com.xq.live.vo.in.AccountLogInVo;
import com.xq.live.vo.in.CouponInVo;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.in.UserAccountInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * com.xq.live.web.controller
 *
 * @author zhangpeng32
 * Created on 2018/5/5 下午3:35
 * @Description:
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CouponService couponService;

    /**
     * 根据用户id查询账户信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getByUserId", method = RequestMethod.GET)
    public BaseResp<UserAccount> getByUserId(Long userId){
        if(userId ==  null){
            return new BaseResp<UserAccount>(ResultStatus.error_input_user_id);
        }

        UserAccount result = accountService.findAccountByUserId(userId);
        return new BaseResp<UserAccount>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询用户交易流水
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/listTrading", method = RequestMethod.GET)
    public BaseResp<?> listTrading(AccountLogInVo inVo){
        if(inVo ==  null || inVo.getUserId() == null){
            return new BaseResp<Pager<AccountLog>>(ResultStatus.error_para_user_empty);
        }

        Pager<AccountLog> res = accountService.findAccountLogs(inVo);
        return new BaseResp<Pager<AccountLog>>(ResultStatus.SUCCESS, res);
    }

    /**
     * 账户余额查询
     * @param userId
     * @return
     */
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public BaseResp<BigDecimal> balance(Long userId){
        if(userId ==  null){
            return new BaseResp<BigDecimal>(ResultStatus.error_input_user_id);
        }

        BigDecimal result = accountService.balance(userId);
        return new BaseResp<BigDecimal>(ResultStatus.SUCCESS, result);
    }

    /**
     * 核销券领取随机红包
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/redPacket", method = RequestMethod.POST)
    public BaseResp<BigDecimal> randomMoney(CouponInVo inVo){
        if(inVo == null){
            return new BaseResp<BigDecimal>(ResultStatus.error_param_empty);
        }
        if(inVo.getUserId() ==  null){
            return new BaseResp<BigDecimal>(ResultStatus.error_parm_user_id_empty);
        }
        if(inVo.getId() == null){
            return new BaseResp<BigDecimal>(ResultStatus.error_parm_coupon_id_empty);
        }
        Coupon coupon = couponService.get(inVo.getId());
        if(coupon == null){ //券不存在
            return new BaseResp<BigDecimal>(ResultStatus.error_coupon_null);
        }

        if(inVo.getUserId().compareTo(coupon.getUserId()) != 0){    //券不是该用户的，无法领取红包
            return new BaseResp<BigDecimal>(ResultStatus.error_coupon_user_id);
        }
        //券未核销，不能领取红包
        if(coupon.getIsUsed() != Coupon.COUPON_IS_USED_YES){
            return new BaseResp<BigDecimal>(ResultStatus.error_coupon_is_not_used);
        }

        int max = 0;
        int couponAmount = coupon.getCouponAmount();
        /**
         * 如果购买享七券支付的金额小于1元，则可领取红包金额按小于1元处理
         * couponAmount).multiply(BigDecimal.valueOf(0.1) = 面值 * 10% = 买券时候的付款金额
         * 如果是0元券，也可以领取1元以下的红包
         */
        if(BigDecimal.ONE.compareTo(BigDecimal.valueOf(couponAmount).multiply(BigDecimal.valueOf(0.1))) == 1){
            max = 1;
        }else{
            max = couponAmount;
        }
        BigDecimal redPacketAmount = RandomNumberUtil.randomNumber(max, 10);
        UserAccountInVo accountInVo = new UserAccountInVo();
        accountInVo.setUserId(inVo.getUserId());
        accountInVo.setOccurAmount(redPacketAmount);
        int ret = accountService.income(accountInVo, "核销享七券，领取红包奖励");
        if(ret > 0){
            return new BaseResp<BigDecimal>(ResultStatus.SUCCESS, redPacketAmount);
        }
        return new BaseResp<BigDecimal>(ResultStatus.error_receive_red_packet_fail);
    }


}