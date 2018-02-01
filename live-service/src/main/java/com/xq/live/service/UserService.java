package com.xq.live.service;

import com.xq.live.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhangpeng32 on 2017/12/14.
 */
public interface UserService {
    public User getUserById(@Param("id") Long id);

    public Long insert(User user);

    public User findByUsername(@Param("userName") String userName);

    public List<User> findAll();
}
