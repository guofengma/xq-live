package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.AccountLog;
import com.xq.live.model.UserAccount;
import com.xq.live.service.AccountService;
import com.xq.live.vo.in.AccountLogInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}