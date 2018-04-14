package com.xq.live.web.controllerForApp;

import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.*;
import com.xq.live.model.User;
import com.xq.live.service.AccessLogService;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.UserInVo;
import com.xq.live.web.utils.IpUtils;
import com.xq.live.web.utils.PayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lipeng on 2017/12/14.
 * 用户信息相关controller
 */

@RestController
@RequestMapping(value = "/app/user")
public class UserForAppController {

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
    public BaseResp<User> getUserbyId(@PathVariable Long id) {
        User user = user = userService.getUserById(id);
        return new BaseResp<User>(ResultStatus.SUCCESS, user);
    }

    /**
     * 新增用户
     * @param code
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> addUser(String code, HttpServletRequest request){
        //获取openId
        if (StringUtils.isEmpty(code)) {
            return new BaseResp<Long>(ResultStatus.error_weixin_user_code_empty);
        }
        //获取openId
        String param = "?grant_type=" + PaymentConfig.GRANT_TYPE + "&appid=" + PaymentConfig.APPID + "&secret=" + PaymentConfig.API_KEY + "&js_code=" + code;
        //创建请求对象
        String httpRet = PayUtils.httpRequest(PaymentConfig.GET_OPEN_ID_URL, "GET", param);
        Map<String, String> result = new HashMap<String, String>();
        JSONObject jsonObject = JSONObject.parseObject(httpRet);
        if (jsonObject != null) {
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode != null) {
                //返回异常信息
                return new BaseResp<Long>(errcode, jsonObject.getString("errmsg"), null);
            }
            String openId = jsonObject.getString("openid");
            User user = userService.findByOpenId(openId);
            if(user != null){
                return new BaseResp<Long>(ResultStatus.error_user_exist,user.getId());
            }
            user = new User();
            user.setOpenId(openId);
            user.setUserIp(IpUtils.getIpAddr(request));
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssmm");
            user.setUserName("xq_"+sdf.format(date));
            user.setPassword(RandomStringUtil.getRandomCode(6,3));
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            user.setSourceType(1);  //来源小程序
            Long id  = userService.add(user);
            return new BaseResp<Long>(ResultStatus.SUCCESS, id);
        }
        return new BaseResp<Long>(ResultStatus.FAIL);
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
     * 根据openId查询用户信息
     * @param openId
     * @return
     */
    @RequestMapping(value = "/findByOpenId/{openId}", method = RequestMethod.GET)
    public BaseResp<User> findByOpenId(@PathVariable("openId") String openId){
        User user = userService.findByOpenId(openId);
        return new BaseResp<User>(ResultStatus.SUCCESS, user);
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(User user){
        if(user == null || user.getOpenId() == null){
            return new BaseResp<Integer>(ResultStatus.error_param_open_id_empty);
        }
        if(user.getId() == null){
            return new BaseResp<Integer>(ResultStatus.error_input_user_id);
        }

        User u = userService.findByOpenId(user.getOpenId());
        if(u == null){
            return new BaseResp<Integer>(ResultStatus.error_param_open_id);
        }
        Integer result = userService.updateByOpenId(user);
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
