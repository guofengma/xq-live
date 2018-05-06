package com.xq.live.dao;

import com.xq.live.model.UserAccount;
import com.xq.live.vo.in.UserAccountInVo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserAccount record);

    int insertSelective(UserAccount record);

    UserAccount selectByPrimaryKey(Long id);

    /**
     * 根据用户id查询账户信息
     * @param userId
     * @return
     */
    UserAccount findAccountByUserId(Long userId);

    int updateByPrimaryKeySelective(UserAccount record);

    int updateByPrimaryKey(UserAccount record);

    /**
     * 账户收入
     * @param inVo
     * @return
     */
    int income(UserAccountInVo inVo);

    /**
     * 账户支出
     * @param inVo
     * @return
     */
    int payout(UserAccountInVo inVo);
}