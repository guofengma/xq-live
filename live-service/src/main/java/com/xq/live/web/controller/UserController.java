package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.RandomStringUtil;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import com.xq.live.service.AccessLogService;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.UserInVo;
import com.xq.live.web.utils.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by zhangpeng32 on 2017/12/14.
 * 用户信息相关controller
 */

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccessLogService accessLogService;

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<User> getUserbyId(@PathVariable("id") Long id) {
        User user = user = userService.getUserById(id);
        return new BaseResp<User>(ResultStatus.SUCCESS, user);
    }

    /**
     * 新增用户
     * @param in
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> addUser(@Valid User in, HttpServletRequest request, BindingResult result){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        in.setUserIp(IpUtils.getIpAddr(request));
        User user = userService.findByUsername(in.getUserName());
        if(user != null){
            return new BaseResp<Long>(ResultStatus.error_user_exist);
        }

        Long id  = userService.add(in);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 查询用户列表信息
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public BaseResp<Pager<User>> userList(UserInVo inVo){
        Pager<User> result = userService.list(inVo);
        return new BaseResp<Pager<User>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 该方法是注册用户的方法，默认放开访问控制
     * @param in
     */
//    @PostMapping("/signup")
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public BaseResp<Long> signUp(User in) {
        if(in == null || StringUtils.isEmpty(in.getUserName())){
            return new BaseResp<Long>(ResultStatus.error_user_exist);
        }
        User user = userService.findByUsername(in.getUserName());
        if(user != null){
            return new BaseResp<Long>(ResultStatus.error_para_user_empty);
        }
//        String pwd = bCryptPasswordEncoder.encode(in.getPassword());
        in.setPassword(RandomStringUtil.getRandomCode(6,3));
        in.setPassword(DigestUtils.md5DigestAsHex(in.getPassword().getBytes()));
        Long id  = userService.add(in);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    @RequestMapping(value = "/findUserByName/{userName}", method = RequestMethod.GET)
    public BaseResp<User> findUserByName(@PathVariable("userName") String userName){
        User user = userService.findByUsername(userName);
        return new BaseResp<User>(ResultStatus.SUCCESS, user);
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(User user){
        Integer result = userService.update(user);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询用户列表
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    @ResponseBody
    public BaseResp<List<User>> top(UserInVo inVo){
        List<User> result = userService.top(inVo);
        return new BaseResp<List<User>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 登录身份验证
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResp<User> login(UserInVo inVo, HttpServletRequest request){
        if(inVo == null || inVo.getUserName() == null || inVo.getPassword() == null){
            return new BaseResp<User>(ResultStatus.error_para_user_empty);
        }
        inVo.setPassword(DigestUtils.md5DigestAsHex(inVo.getPassword().getBytes()));
        User user = userService.findByUserNameAndPwd(inVo);
        if(user != null){
            //更新登录时间，更新登录次数
            user.setUserIp(IpUtils.getIpAddr(request));
            user.setLoginTimes(user.getLoginTimes() + 1);   //登录次数+1
            userService.updateLoginInfo(user);
        }else{
            return new BaseResp<User>(ResultStatus.error_para_user_login);
        }
        return new BaseResp<User>(ResultStatus.SUCCESS, user);
    }
}
