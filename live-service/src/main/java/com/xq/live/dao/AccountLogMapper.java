package com.xq.live.dao;

import com.xq.live.model.AccountLog;
import com.xq.live.vo.in.AccountLogInVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AccountLog record);

    int insertSelective(AccountLog record);

    AccountLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountLog record);

    int updateByPrimaryKey(AccountLog record);

    int listTotal(AccountLogInVo inVo);

    List<AccountLog> list(AccountLogInVo inVo);
}