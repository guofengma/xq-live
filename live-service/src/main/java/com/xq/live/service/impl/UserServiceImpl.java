package com.xq.live.service.impl;

import com.xq.live.common.Constants;
import com.xq.live.common.Pager;
import com.xq.live.common.RedisCache;
import com.xq.live.dao.AccessLogMapper;
import com.xq.live.dao.SoMapper;
import com.xq.live.dao.UserAccountMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.User;
import com.xq.live.model.UserAccount;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.in.UserInVo;
import com.xq.live.web.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private SoMapper soMapper;

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${jwt.password.salt}")
    private String salt;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public User getUserById(@Param("id") Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long add(User user) {
        user.setIconUrl(Constants.DEFAULT_ICON_URL);
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(user.getUserName()+salt);//密码为userName + salt
        user.setPassword(encoder.encode(user.getPassword()));
        //end
        int ret = userMapper.insert(user);
        if(ret > 0){
            //新增用户成功后，新增一条账户信息
            this.addUserAccount(user);

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
    public User findByUnionId(String unionId) {
        return userMapper.findByUnionId(unionId);
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
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(user.getUserName()+salt);//密码为userName + salt
        user.setPassword(encoder.encode(user.getPassword()));
        //end
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<User> top(UserInVo inVo){
        return userMapper.list(inVo);
    }

    //通过订单ID得到UserID获取用户余额
    @Override
    public List<UserAccount> findAccountByUserId(Long id) {
        List<UserAccount> list = new ArrayList<UserAccount>();
        SoInVo inVo = new SoInVo();
        inVo.setId(id);
        Long userId=soMapper.getUserIDBySoId(inVo);
        UserAccount userAccount=userAccountMapper.findAccountByUserId(userId);
        list.add(0,userAccount);
        return list;
    }

    //通过活动UserID获取用户余额
    @Override
    public List<UserAccount> fingAccountByID(Long userID) {
        List<UserAccount> list = new ArrayList<UserAccount>();
        UserAccount userAccount=userAccountMapper.findAccountByUserId(userID);
        list.add(0,userAccount);
        return list;
    }
    //通过UserId获取用户余额

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
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(user.getUserName() + salt);//密码为userName + salt
        user.setPassword(encoder.encode(user.getPassword()));
        //end
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
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(user.getUserName() + salt);//密码为userName + salt
        user.setPassword(encoder.encode(user.getPassword()));
        //end
        return userMapper.updateByOpenId(user);
    }

    //在用户短信验证通过后修改account_name名称为用户电话号码
    @Override
    public Integer updateByUserID(UserAccountInVo accountInVo) {
        int i=userAccountMapper.updateByUserID(accountInVo);
        //如果成功返回1，失败返回0
        if (i>0){
            return 1;
        }
        return 0;
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
        //修改密码----begain
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(user.getUserName()+salt);//密码为userName + salt
        user.setPassword(encoder.encode(user.getPassword()));
        //end
        String aa = user.getPassword();
        String bb = user.getUserName()+salt;
        boolean kk = encoder.matches(bb, aa);
        Integer integer = userMapper.updateByMobile(user);
        return integer;
    }


    @Override
    public String login(String username, String password){
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, username+salt);
        //BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //boolean kk = encoder.matches("1999900099abc", "$2a$10$I3hlDXQqWI9S9XA8kKcj6uLNUc2NO1uAuSjTO6aqWfvaZXD0cmfha");
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenUtil.generateToken(userDetails);
    }

    @Override
    public Long register(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = user.getPassword();
        user.setPassword(encoder.encode(rawPassword));
//        rawPassword = DigestUtils.md5DigestAsHex((rawPassword).getBytes());
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        if (!jwtTokenUtil.isTokenExpired(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }


    @Override
    public Long addAppUser(User user) {
        User byOpenId = userMapper.findByOpenId(user.getOpenId());
        User byUnionId = userMapper.findByUnionId(user.getUnionId());
        User byMobile = userMapper.findByMobile(user.getMobile());
        /*
        商家端中用微信登录必有openId，且openId应该都是不同的
        byUnionId如果为空,byMobile也为空,则证明要么没用户，要么没有有效用户(该用户要被合并)，则直接插入一条数据,返回userId
        byUnionId如果为空,byMobile不为空，则到byMobile
         */
        if(byUnionId==null){
            if(byMobile==null){
                //修改密码----begain
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                user.setPassword(user.getUserName() + salt);//密码为userName + salt
                user.setPassword(encoder.encode(user.getPassword()));
                //end
                    int i = userMapper.insert(user);
                    if (i < 1) {
                        return null;
                    }
                    //新增用户成功后，新增一条账户信息
                    this.addUserAccount(user);
                    return user.getId();
            }
            byMobile.setOpenId(user.getOpenId());
            byMobile.setUnionId(user.getUnionId());
            //修改密码----begain
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            byMobile.setPassword(byMobile.getUserName() + salt);//密码为userName + salt
            byMobile.setPassword(encoder.encode(byMobile.getPassword()));
            //end
            Integer k = userMapper.updateByMobile(byMobile);
            if(k<1){
                return null;
            }
            return byMobile.getId();
        }else {
            if(byUnionId.getMobile()!=null&&!StringUtils.equals("",byUnionId.getMobile())){
                if(!StringUtils.equals(byUnionId.getOpenId(), user.getOpenId())){
                    byUnionId.setOpenId(user.getOpenId());
                    //修改密码----begain
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    byUnionId.setPassword(byUnionId.getUserName() + salt);//密码为userName + salt
                    byUnionId.setPassword(encoder.encode(byUnionId.getPassword()));
                    //end
                    userMapper.updateByPrimaryKeySelective(byUnionId);
                }
                return byUnionId.getId();
            }
            if(byMobile==null){
                byUnionId.setMobile(user.getMobile());
                //删除不要的缓存----begin
                String[] k = new String[2];
                k[0] = "login_username_" + byUnionId.getUserName();
                k[1] = byUnionId.getUserName();
                redisCache.del(k);
                //end
                byUnionId.setUserName(user.getMobile());
                //修改密码----begain
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                byUnionId.setPassword(byUnionId.getUserName() + salt);//密码为userName + salt
                byUnionId.setPassword(encoder.encode(byUnionId.getPassword()));
                //end
                Integer j = userMapper.updateByPrimaryKeySelective(byUnionId);
                if (j < 1) {
                    return null;
                }
                return byUnionId.getId();
            }
            byMobile.setOpenId(user.getOpenId());
            byMobile.setUnionId(user.getUnionId());
            //修改密码----begain
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            byMobile.setPassword(byMobile.getUserName() + salt);//密码为userName + salt
            byMobile.setPassword(encoder.encode(byMobile.getPassword()));
            //end
            userMapper.updateByMobile(byMobile);
            //删除不要的缓存----begin
            String[] k = new String[3];
            k[0] = "login_username_" + byUnionId.getUserName();
            k[1] = byUnionId.getUserName();
            k[2] = byUnionId.getId().toString();
            redisCache.del(k);
            //end
            userMapper.deleteByPrimaryKey(byUnionId.getId());
            return byMobile.getId();
        }


        /*if(byOpenId==null) {
            if (byMobile == null) {
                int i = userMapper.insert(user);
                if (i < 1) {
                    return null;
                }
                //新增用户成功后，新增一条账户信息
                this.addUserAccount(user);
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
            userMapper.deleteByPrimaryKey(byOpenId.getId());
            return byMobile.getId();
        }*/
    }

    /**
     * 新增用户账户记录
     * @param user
     * @return
     */
    private Integer addUserAccount(User user){
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(user.getId());
        userAccount.setUserName(user.getUserName());
        userAccount.setAccountName(user.getMobile());   //暂时把手机号作为用户账号
        userAccount.setAccountType(UserAccount.ACCOUNT_TYPE_XQ);
        userAccount.setAccountAmount(BigDecimal.ZERO);
        userAccount.setAccountStatus(UserAccount.ACCOUNT_STATUS_ACTIVE);
        return userAccountMapper.insert(userAccount);
    }
}
