package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.common.RedisCache;
import com.xq.live.dao.AccessLogMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.User;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.UserInVo;
import com.xq.live.web.utils.SignUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangpeng32 on 2017/12/14.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Override
    public User getUserById(@Param("id") Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long add(User user) {
        user.setIconUrl("https://xq-1256079679.file.myqcloud.com/test_图层 24_0.8.jpg");
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
        return userMapper.findByOpenId(openId);
    }

    @Override
    public User findByMobile(String mobile) {
        User byMobile = userMapper.findByMobile(mobile);
        return byMobile;
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
    public List<User> listForShopId(UserInVo inVo) {
        return userMapper.listForShopId(inVo);
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
        if(user!=null&&user.getMobile()!=null){
            /*Map<String, String> rmp = SignUtil.encryNameAndMobile(user.getMobile());
            user.setUserName(rmp.get("mobile"));*/
            user.setUserName(user.getMobile());
        }
        return userMapper.updateByOpenId(user);
    }

    @Override
    public Integer updateByMobile(User user){
        Date now = new Date();
        //1、更新用户表登录ip，登录次数等
        user.setUpdateTime(now);
        user.setLastLoginTime(now);
        if(user!=null&&user.getMobile()!=null){
            user.setUserName(user.getMobile());
        }
        Integer integer = userMapper.updateByMobile(user);
        return integer;
    }

    @Override
    public Long addAppUser(User user) {
        User byOpenId = userMapper.findByOpenId(user.getOpenId());
        User byMobile = userMapper.findByMobile(user.getMobile());
        if(byOpenId==null) {
            if (byMobile == null) {
                int i = userMapper.insert(user);
                if (i < 1) {
                    return null;
                }
                return user.getId();
            }
            byMobile.setOpenId(user.getOpenId());
            Integer k = userMapper.updateByMobile(byMobile);
            if(k<1){
                return null;
            }
            return byMobile.getId();
        }else {
            if(byOpenId.getMobile()!=null){
                return byOpenId.getId();
            }
            if(byMobile==null){
                byOpenId.setMobile(user.getMobile());
                Integer j = userMapper.updateByOpenId(byOpenId);
                if (j < 1) {
                    return null;
                }
                return byOpenId.getId();
            }
            byMobile.setOpenId(user.getOpenId());
            userMapper.updateByMobile(byMobile);
            userMapper.deleteByPrimaryKey(user.getId());
            return byMobile.getId();
        }
    }
}
