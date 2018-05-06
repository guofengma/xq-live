package com.xq.live.service;

import com.xq.live.model.UserAccount;
import com.xq.live.vo.in.UserAccountInVo;

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
     * @return
     */
    Integer income(UserAccountInVo inVo);


    /**
     * 账户支出
     * @param inVo
     * @return
     */
    Integer payout(UserAccountInVo inVo);

    /**
     * 根据用户id查询账户信息
     * @param userId
     * @return
     */
    UserAccount findAccountByUserId(Long userId);
}
