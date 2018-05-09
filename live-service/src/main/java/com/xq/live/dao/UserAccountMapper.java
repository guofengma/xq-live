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
     * @param userID
     * @return
     */
    UserAccount findAccountByUserId(Long userID);

    /**
     * 根据参数修改表数据
     * @param inVo
     * @return
     */
    int updateByPrimaryKeySelective(UserAccountInVo inVo);

    /**
     * 根据参数修改表数据除去UserID
     * @param inVo
     * @return
     */
    int updateByUserID(UserAccountInVo inVo);

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