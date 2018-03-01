package com.xq.live.dao;

import com.xq.live.model.User;
import com.xq.live.vo.in.UserInVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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

    User findByUserNameAndPwd(UserInVo inVo);
}