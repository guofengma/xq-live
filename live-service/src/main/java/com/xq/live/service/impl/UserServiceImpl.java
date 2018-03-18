package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.common.RedisCache;
import com.xq.live.dao.AccessLogMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.User;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.UserInVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangpeng32 on 2017/12/14.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Autowired
    private RedisCache<String, User> redisCache;

    @Override
    public User getUserById(@Param("id") Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long add(User user) {
        int ret = userMapper.insert(user);
        if(ret > 0){
            return user.getId();
        }
        return null;
    }

    @Override
    public User findByUsername(@org.apache.ibatis.annotations.Param("userName") String userName) {
        return userMapper.loadUserByUserName(userName);
    }

    @Override
    public User findByOpenId(String openId) {
        User user = null;
        // 从缓存中获取用户信息
        String key = "USER_OPEN_ID_" + openId;
        // 缓存存在
        if(redisCache.hasKey(key)){
            user = redisCache.get(key);
            return user;
        }
        user = userMapper.findByOpenId(openId);
        // 插入缓存
        redisCache.set(key, user, 10, TimeUnit.MINUTES);
        return user;
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
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<User> top(UserInVo inVo){
        return userMapper.list(inVo);
    }

    @Override
    public User findByUserNameAndPwd(UserInVo inVo){
        return userMapper.findByUserNameAndPwd(inVo);
    }

    @Override
    public Integer updateLoginInfo(User user){
        Date now = new Date();
        //1、更新用户表登录ip，登录次数等
        user.setUpdateTime(now);
        user.setLastLoginTime(now);
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public Integer updateByOpenId(User user){
        Date now = new Date();
        //1、更新用户表登录ip，登录次数等
        user.setUpdateTime(now);
        user.setLastLoginTime(now);
        return userMapper.updateByOpenId(user);
    }
}
