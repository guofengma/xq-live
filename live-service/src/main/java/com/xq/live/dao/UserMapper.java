package com.xq.live.dao;

import com.xq.live.model.User;
import com.xq.live.vo.in.UserInVo;

import java.util.List;

public interface UserMapper{
    int deleteByPrimaryKey(Long id);

    Long insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User loadUserByUserName(String userName);

    List<User> list(UserInVo inVo);

    int listTotal(UserInVo inVo);
}