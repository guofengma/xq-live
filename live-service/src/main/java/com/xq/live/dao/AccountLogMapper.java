package com.xq.live.dao;

import com.xq.live.model.AccountLog;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AccountLog record);

    int insertSelective(AccountLog record);

    AccountLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccountLog record);

    int updateByPrimaryKey(AccountLog record);
}