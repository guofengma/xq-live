package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.User;
import com.xq.live.vo.in.UserInVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zhangpeng32 on 2017/12/14.
 */
public interface UserService {
    public User getUserById(@Param("id") Long id);

    public Long add(User user);

    public User findByUsername(@Param("userName") String userName);

    public User findByOpenId(String openId);

    public User findByMobile(String mobile);

    public Pager<User> list(UserInVo inVo);

    public List<User> listForShopId(UserInVo inVo);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public Integer update(User user);

    public List<User> top(UserInVo inVo);

    /**
     * 根据入参查询用户信息
     * @param inVo
     * @return
     */
    public User findByUserNameAndPwd(UserInVo inVo);

    /**
     * 更新用户登录信息
     * @param user
     * @return
     */
    public Integer updateLoginInfo(User user);

    /**
     * 使用openid更新用户信息
     * @param user
     * @return
     */
    public Integer updateByOpenId(User user);

    /**
     * 使用mobile更新用户信息
     * @param user
     * @return
     */
    public Integer updateByMobile(User user);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 操作结果
     */
    String login(String username, String password);

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 操作结果
     */
    Long register(User user);

    /**
     * 刷新密钥
     *
     * @param oldToken 原密钥
     * @return 新密钥
     */
    String refreshToken(String oldToken);

}
