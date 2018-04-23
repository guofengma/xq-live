package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.RandomStringUtil;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @package: com.xq.live.web.controller
 * @description: 授权controller
 * @author: zhangpeng32
 * @date: 2018/4/20 15:55
 * @version: 1.0
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    /**
     * 该方法是注册用户的方法，默认放开访问控制
     * @param in
     */
    @PostMapping("/signup")
    public BaseResp<Long> signup(User in) throws AuthenticationException{
        if(in == null || StringUtils.isEmpty(in.getUserName())){
            return new BaseResp<Long>(ResultStatus.error_user_exist);
        }
        User user = userService.findByUsername(in.getUserName());
        if(user != null){
            return new BaseResp<Long>(ResultStatus.error_user_exist);
        }
        Long id  = userService.register(in);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 登录认证
     * @param in
     * @return
     * @throws AuthenticationException
     */
    @PostMapping("/login")
    public BaseResp<?> login(User in) throws AuthenticationException{
        if(in == null || StringUtils.isEmpty(in.getUserName())){
            return new BaseResp<String>(ResultStatus.error_para_user_empty);
        }
        String token = userService.login(in.getUserName(), in.getPassword());
        return new BaseResp<String>(ResultStatus.SUCCESS, token);
    }

    /**
     * 刷新密钥
     *
     * @param authorization 原密钥
     * @return 新密钥
     * @throws AuthenticationException 错误信息
     */
    @GetMapping(value = "/refresh")
    public BaseResp<?> refreshToken(@RequestHeader String authorization) throws AuthenticationException {
        String result =  userService.refreshToken(authorization);
        return new BaseResp<String>(ResultStatus.SUCCESS, result);
    }
}
