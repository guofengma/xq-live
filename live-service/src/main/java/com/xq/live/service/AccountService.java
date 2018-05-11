package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.AccountLog;
import com.xq.live.model.UserAccount;
import com.xq.live.vo.in.AccountLogInVo;
import com.xq.live.vo.in.UserAccountInVo;

import java.math.BigDecimal;

/**
 * com.xq.live.service
 *  用户账户service
 * @author zhangpeng32
 * Created on 2018/5/5 下午4:27
 * @Description:
 */
public interface AccountService {
    /**
     * 更新用户账户
     * @param userAccount
     * @return
     */
    Integer update(UserAccount userAccount);

    /**
     * 账户收入
     * @param inVo
     * @param remark  备注
     * @return
     */
    Integer income(UserAccountInVo inVo, String remark);


    /**
     * 账户支出
     * @param inVo
     * @return
     */
    Integer payout(UserAccountInVo inVo, String remark);

    /**
     * 根据用户id查询账户信息
     * @param userId
     * @return
     */
    UserAccount findAccountByUserId(Long userId);

    /**
     * 分页查询用户交易流水列表
     * @param inVo
     * @return
     */
    public Pager<AccountLog> findAccountLogs(AccountLogInVo inVo);

    /**
     * 根据用户id查询账户余额
     * @param userId
     * @return
     */
    public BigDecimal balance(Long userId);
}
