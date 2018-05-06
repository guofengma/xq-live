package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.UserAccount;
import com.xq.live.service.AccountService;
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

    @RequestMapping(value = "/getByUserId", method = RequestMethod.GET)
    public BaseResp<UserAccount> getByUserId(Long userId){
        if(userId ==  null){
            return new BaseResp<UserAccount>(ResultStatus.error_input_user_id);
        }

        UserAccount result =accountService.findAccountByUserId(userId);
        return new BaseResp<UserAccount>(ResultStatus.SUCCESS, result);
    }
}
