package com.xq.live.service.impl;

import com.xq.live.dao.UserMapper;
import com.xq.live.model.User;
import com.xq.live.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangpeng32 on 2017/12/14.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(@Param("id") Long id){
        return  userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long insert(User user){
        return userMapper.insert(user);
    }

    @Override
    public User findByUsername(@org.apache.ibatis.annotations.Param("userName") String userName){
        return  userMapper.loadUserByUserName(userName);
    }

    @Override
    public List<User> findAll(){
        return  userMapper.list();
    }
}
