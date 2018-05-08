package com.xq.live.web.controllerForApp;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.CashApply;
import com.xq.live.model.UserAccount;
import com.xq.live.service.AccountService;
import com.xq.live.service.CashApplyService;
import com.xq.live.vo.in.CashApplyInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * com.xq.live.web.controller
 *  提现业务
 * @author zhangpeng32
 * Created on 2018/5/6 下午4:51
 * @Description:
 */
@RequestMapping(value = "app/tx")
@RestController
public class CashApplyForAppController {

    @Autowired
    private CashApplyService cashApplyService;

    @Autowired
    private AccountService accountService;
    /**
     * 申请提现
     * @param cashApply
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseResp<?> create(@Valid CashApply cashApply, BindingResult result){
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        //查询账户信息
        UserAccount account = accountService.findAccountByUserId(cashApply.getUserId());
        if(account == null){
            return new BaseResp<Long>(ResultStatus.error_user_account_info);
        }

        //提取金额大于账户余额
        if(cashApply.getCashAmount().compareTo(account.getAccountAmount()) > 0){
            return new BaseResp<Long>(ResultStatus.error_cash_apply_amount);
        }

        cashApply.setAccountId(account.getId());
        cashApply.setAccountName(account.getAccountName());
        Long res = cashApplyService.create(cashApply);
        return new BaseResp<Long>(ResultStatus.SUCCESS, res);
    }

    /**
     * 提现列表查询
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/list")
    public BaseResp<?> list(CashApplyInVo inVo){
        if(inVo == null || inVo.getUserId() == null){
            return new BaseResp<Pager<CashApply>>(ResultStatus.error_para_user_empty);
        }
        Pager<CashApply> res = cashApplyService.list(inVo);
        return new BaseResp<Pager<CashApply>>(ResultStatus.SUCCESS, res);
    }
}
