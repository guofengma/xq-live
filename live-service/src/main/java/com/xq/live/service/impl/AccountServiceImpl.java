package com.xq.live.service.impl;

import com.xq.live.dao.AccountLogMapper;
import com.xq.live.dao.UserAccountMapper;
import com.xq.live.model.AccountLog;
import com.xq.live.model.UserAccount;
import com.xq.live.service.AccountService;
import com.xq.live.vo.in.UserAccountInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * com.xq.live.service.impl
 * 账户操作service
 * @author zhangpeng32
 * Created on 2018/5/5 下午4:29
 * @Description:
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private AccountLogMapper accountLogMapper;

    @Override
    public Integer update(UserAccount userAccount) {
        return userAccountMapper.updateByPrimaryKey(userAccount);
    }

    @Override
    @Transactional
    public Integer income(UserAccountInVo inVo) {
        //查询用户账户信息
        UserAccount userAccount =  userAccountMapper.findAccountByUserId(inVo.getUserId());
        inVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
        //更新账户余额
        Integer ret = userAccountMapper.income(inVo);
        if(ret > 0){
            //写入账户变动日志
            this.addAccountLog(userAccount, inVo.getOccurAmount(), AccountLog.OPERATE_TYPE_INCOME);
        }
        return ret;
    }

    @Override
    @Transactional
    public Integer payout(UserAccountInVo inVo) {
        //查询用户账户信息
        UserAccount userAccount =  userAccountMapper.findAccountByUserId(inVo.getUserId());
        inVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
        //更新账户余额
        Integer ret = userAccountMapper.payout(inVo);
        if(ret > 0){
            //写入账户变动日志
            this.addAccountLog(userAccount, inVo.getOccurAmount(), AccountLog.OPERATE_TYPE_PAYOUT);
        }
        return ret;
    }

    @Override
    public UserAccount findAccountByUserId(Long userId) {
        return userAccountMapper.findAccountByUserId(userId);
    }

    /**
     * 账户操作日志
     * @param userAccount
     * @param occurAmount
     * @param operateType
     * @return
     */
    private Integer addAccountLog(UserAccount userAccount, BigDecimal occurAmount, int operateType){
        AccountLog accountLog = new AccountLog();
        accountLog.setUserId(userAccount.getUserId());
        accountLog.setUserName(userAccount.getUserName());
        accountLog.setAccountId(userAccount.getId());
        accountLog.setAccountName(userAccount.getAccountName());
        accountLog.setPreAmount(userAccount.getAccountAmount());
        accountLog.setOperateType(operateType);
        BigDecimal aferAmount = BigDecimal.ZERO;
        if(operateType == AccountLog.OPERATE_TYPE_INCOME){  //收入
            aferAmount = userAccount.getAccountAmount().add(occurAmount);
        }else {
            aferAmount = userAccount.getAccountAmount().subtract(occurAmount);  //支出
        }
        accountLog.setAfterAmount(aferAmount);
        accountLog.setOperateAmount(occurAmount);

        Integer ret = accountLogMapper.insert(accountLog);
        return ret;
    }
}
