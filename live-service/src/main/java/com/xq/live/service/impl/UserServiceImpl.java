package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.User;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.UserInVo;
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
    public User getUserById(@Param("id") Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long add(User user) {
        return userMapper.insert(user);
    }

    @Override
    public User findByUsername(@org.apache.ibatis.annotations.Param("userName") String userName) {
        return userMapper.loadUserByUserName(userName);
    }

    @Override
    public Pager<User> list(UserInVo inVo) {
        Pager<User> result= new Pager<User>();
        int total = userMapper.listTotal(inVo);
        result.setTotal(total);
        if(total > 0 ){
            List<User> list = userMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public Integer update(User user) {
        return userMapper.updateByPrimaryKey(user);
    }
}
