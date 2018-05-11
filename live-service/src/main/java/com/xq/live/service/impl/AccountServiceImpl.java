package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.AccountLogMapper;
import com.xq.live.dao.UserAccountMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.AccountLog;
import com.xq.live.model.CashApply;
import com.xq.live.model.User;
import com.xq.live.model.UserAccount;
import com.xq.live.service.AccountService;
import com.xq.live.vo.in.AccountLogInVo;
import com.xq.live.vo.in.UserAccountInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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

    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer update(UserAccount userAccount) {
        return userAccountMapper.updateByPrimaryKey(userAccount);
    }

    @Override
    @Transactional
    public Integer income(UserAccountInVo inVo, String remark) {
        //查询用户账户信息
        UserAccount userAccount =  userAccountMapper.findAccountByUserId(inVo.getUserId());
        //如果账户不存在，则新增一个(主要是历史数据没创建账户信息)
        if(userAccount == null){
            User user = userMapper.selectByPrimaryKey(inVo.getUserId());
            if(user != null){
                userAccount = this.createAccountByUser(user);
            }else{
                return 0;
            }
        }

        inVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
        //更新账户余额
        Integer ret = userAccountMapper.income(inVo);
        if(ret > 0){
            //写入账户变动日志
            this.addAccountLog(userAccount, inVo.getOccurAmount(), AccountLog.OPERATE_TYPE_INCOME, remark);
        }
        return ret;
    }

    @Override
    @Transactional
    public Integer payout(UserAccountInVo inVo, String remark) {
        //查询用户账户信息
        UserAccount userAccount =  userAccountMapper.findAccountByUserId(inVo.getUserId());
        inVo.setVersionNo(userAccount.getVersionNo());  //版本号，作为更新的乐观锁条件
        //更新账户余额
        Integer ret = userAccountMapper.payout(inVo);
        if(ret > 0){
            //写入账户变动日志
            this.addAccountLog(userAccount, inVo.getOccurAmount(), AccountLog.OPERATE_TYPE_PAYOUT, remark);
        }
        return ret;
    }

    @Override
    public UserAccount findAccountByUserId(Long userId) {
        return userAccountMapper.findAccountByUserId(userId);
    }

    @Override
    public Pager<AccountLog> findAccountLogs(AccountLogInVo inVo) {
        Pager<AccountLog> result = new Pager<AccountLog>();
        int listTotal = accountLogMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<AccountLog> list = accountLogMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public BigDecimal balance(Long userId) {
        BigDecimal res = BigDecimal.ZERO;
        //查询用户账户信息
        UserAccount userAccount =  userAccountMapper.findAccountByUserId(userId);
        if(userAccount != null && userAccount.getAccountAmount() != null){
            res = userAccount.getAccountAmount();
        }

        return res.setScale(2, RoundingMode.HALF_UP);
    }


    public UserAccount create(UserAccount account){
        int ret = userAccountMapper.insert(account);
        if(ret > 0){
            return account;
        }
        return null;
    }

    /**
     * 账户操作日志
     * @param userAccount
     * @param occurAmount
     * @param operateType
     * @return
     */
    private Integer addAccountLog(UserAccount userAccount, BigDecimal occurAmount, int operateType, String remark){
        AccountLog accountLog = new AccountLog();
        accountLog.setUserId(userAccount.getUserId());
        accountLog.setUserName(userAccount.getUserName());
        accountLog.setAccountId(userAccount.getId());
        accountLog.setAccountName(userAccount.getAccountName());
        accountLog.setPreAmount(userAccount.getAccountAmount());
        accountLog.setOperateType(operateType);
        accountLog.setRemark(remark);
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

    /**
     * 根据user信息创建用户账户
     * @param user
     * @return
     */
    private UserAccount createAccountByUser(User user){
        UserAccount account = new UserAccount();
        account.setUserId(user.getId());
        account.setUserName(user.getUserName());
        account.setAccountName(user.getUserName());
        account.setAccountType(UserAccount.ACCOUNT_TYPE_XQ);
        account.setAccountAmount(BigDecimal.ZERO);
        account.setVersionNo(0);
        return this.create(account);
    }
}
