package com.xq.live.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.xq.live.common.*;
import com.xq.live.model.User;
import com.xq.live.service.AccessLogService;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.UserInVo;
import com.xq.live.web.utils.IpUtils;
import com.xq.live.web.utils.PayUtils;
import com.xq.live.web.utils.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public BaseResp<Long> addUser(String code,HttpServletRequest request){
        //获取openId
        if (StringUtils.isEmpty(code)) {
            return new BaseResp<Long>(ResultStatus.error_weixin_user_code_empty);
        }

        /*if(StringUtils.isEmpty(unionId)){
            return new BaseResp<Long>(ResultStatus.error_weixin_user_unionid_empty);
        }*/
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
            String unionId = jsonObject.getString("unionid");
            /*
            通过unionid查询唯一用户，如果没有查到再查询openId，如果也没有查到证明是新用户或者是仅仅通过手机号在商家端里面注册的用户，直接插入数据即可,
            如果通过unionid没查到，通过openId查到了，则证明是之前的老用户，没有unionId，则更新用户，返回userId给前端,
            如果通过unionid查到了用户，则里面必有openId,再比较其中的openId，如果openId相等的话，则不用更新用户，不相等的话更新用户(更新openId)
            */
            User byUnionId = userService.findByUnionId(unionId);
            User user = userService.findByOpenId(openId);
            if(byUnionId==null){
                if(user!=null){
                   user.setUnionId(unionId);
                    Integer update = userService.update(user);
                   return new BaseResp<Long>(ResultStatus.error_user_exist,user.getId());
                }
            }else if(byUnionId!=null){
                if(!StringUtils.equals(byUnionId.getOpenId(),openId)){
                    byUnionId.setOpenId(openId);
                    Integer update = userService.update(byUnionId);
                    return new BaseResp<Long>(ResultStatus.error_user_exist,byUnionId.getId());
                }
                return new BaseResp<Long>(ResultStatus.error_user_exist,byUnionId.getId());
            }

            /*User user = userService.findByOpenId(openId);
            if(user != null){
                return new BaseResp<Long>(ResultStatus.error_user_exist,user.getId());
            }*/
            User userNew = new User();
            userNew.setOpenId(openId);
            userNew.setUnionId(unionId);
            userNew.setUserIp(IpUtils.getIpAddr(request));
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssmm");
            userNew.setUserName("xq_" + sdf.format(date));
            userNew.setPassword(RandomStringUtil.getRandomCode(6,3));
            userNew.setPassword(DigestUtils.md5DigestAsHex(userNew.getPassword().getBytes()));
            userNew.setSourceType(1);  //来源小程序
            Long id  = userService.add(userNew);
            return new BaseResp<Long>(ResultStatus.SUCCESS, id);
        }
        return new BaseResp<Long>(ResultStatus.FAIL);
    }

    /**
     * 通过openId和unionId新增用户
     * @param openId
     * @param unionId
     * @param request
     * @return
     */
    @RequestMapping(value = "/addUserUnionId", method = RequestMethod.POST)
    public BaseResp<Long> addUserUnionId(String openId,String unionId,HttpServletRequest request){
        //获取openId
        if (StringUtils.isEmpty(openId)||StringUtils.isEmpty(unionId)) {
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
            /*
            通过unionid查询唯一用户，如果没有查到再查询openId，如果也没有查到证明是新用户或者是仅仅通过手机号在商家端里面注册的用户，直接插入数据即可,
            如果通过unionid没查到，通过openId查到了，则证明是之前的老用户，没有unionId，则更新用户，返回userId给前端,
            如果通过unionid查到了用户，则里面必有openId,再比较其中的openId，如果openId相等的话，则不用更新用户，不相等的话更新用户(更新openId)
            */
            User byUnionId = userService.findByUnionId(unionId);
            User user = userService.findByOpenId(openId);
            if(byUnionId==null){
                if(user!=null){
                    user.setUnionId(unionId);
                    Integer update = userService.update(user);
                    return new BaseResp<Long>(ResultStatus.error_user_exist,user.getId());
                }
            }else if(byUnionId!=null){
                if(!StringUtils.equals(byUnionId.getOpenId(),openId)){
                    byUnionId.setOpenId(openId);
                    Integer update = userService.update(byUnionId);
                    return new BaseResp<Long>(ResultStatus.error_user_exist,byUnionId.getId());
                }
                return new BaseResp<Long>(ResultStatus.error_user_exist,byUnionId.getId());
            }

            /*User user = userService.findByOpenId(openId);
            if(user != null){
                return new BaseResp<Long>(ResultStatus.error_user_exist,user.getId());
            }*/
            User userNew = new User();
            userNew.setOpenId(openId);
            userNew.setUnionId(unionId);
            userNew.setUserIp(IpUtils.getIpAddr(request));
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssmm");
            userNew.setUserName("xq_" + sdf.format(date));
            userNew.setPassword(RandomStringUtil.getRandomCode(6,3));
            userNew.setPassword(DigestUtils.md5DigestAsHex(userNew.getPassword().getBytes()));
            userNew.setSourceType(1);  //来源小程序
            Long id  = userService.add(userNew);
            return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }


    /**
     * 最新版本的新增用户,此接口没有用处，不启用
     * @param openId
     * @param mobile
     * @param request
     * @return
     */
    /*@RequestMapping(value = "/addUserForVersion", method = RequestMethod.POST)
    public BaseResp<Long> addUserForVersion(String openId,String mobile, HttpServletRequest request){
        //获取openId
        if (StringUtils.isEmpty(openId)) {
            return new BaseResp<Long>(ResultStatus.error_weixin_user_code_empty);
        }

        if(StringUtils.isEmpty(mobile)){
            return new BaseResp<Long>(ResultStatus.error_weixin_user_code_empty);
        }


            User user = userService.findByOpenId(openId);
            User byMobileUser = userService.findByMobile(mobile);
            if(user==null){
                if(byMobileUser!=null){
                    byMobileUser.setOpenId(openId);
                    //将openId更新到user表中
                    Integer integer = userService.updateByMobile(byMobileUser);
                    return new BaseResp<Long>(ResultStatus.error_user_exist,byMobileUser.getId());
                }
            }else{
                if(byMobileUser==null){
                    user.setMobile(mobile);
                    //将mobile更新到user表中
                    Integer integer = userService.updateByOpenId(user);
                    return new BaseResp<Long>(ResultStatus.error_user_exist,user.getId());
                }
                return new BaseResp<Long>(ResultStatus.error_user_exist,user.getId());
            }
            user = new User();
            user.setOpenId(openId);
            user.setUserIp(IpUtils.getIpAddr(request));
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHssmm");

            //如果有手机号，则把手机号对应的userName隐藏
            //Map<String, String> rmp = SignUtil.encryNameAndMobile(mobile);
            //user.setUserName(rmp.get("mobile"));
            user.setUserName(mobile);
            user.setMobile(mobile);
            user.setPassword(RandomStringUtil.getRandomCode(6,3));
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            user.setSourceType(1);  //来源小程序
            Long id  = userService.add(user);
            return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }*/

    /**
     * 通过code获取用户信息
     * @param code
     * @return
     */
    @RequestMapping(value = "/findByCode",method = RequestMethod.GET)
    public BaseResp<User> findByCode(String code){
        //获取openId
        if (StringUtils.isEmpty(code)) {
            return new BaseResp<User>(ResultStatus.error_weixin_user_code_empty);
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
                return new BaseResp<User>(errcode, jsonObject.getString("errmsg"), null);
            }
            String openId = jsonObject.getString("openid");
            User user = userService.findByOpenId(openId);
            if(user != null){
                return new BaseResp<User>(ResultStatus.SUCCESS,user);
            }
            user = new User();
            user.setOpenId(openId);
            return new BaseResp<User>(ResultStatus.SUCCESS,user);

        }
        return new BaseResp<User>(ResultStatus.FAIL);
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
     * 根据openId查询用户信息,加入unionId之后，通过openId查出来的数据有可能不准，小程序不启用
     * @param openId
     * @return
     */
    /*@RequestMapping(value = "/findByOpenId/{openId}", method = RequestMethod.GET)
    public BaseResp<User> findByOpenId(@PathVariable("openId") String openId){
        User user = userService.findByOpenId(openId);
        return new BaseResp<User>(ResultStatus.SUCCESS, user);
    }*/

    /**
     * 更新用户信息,要通过userId来更新用户信息
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

        User u = userService.getUserById(user.getId());
        if(u == null){
            return new BaseResp<Integer>(ResultStatus.error_param_open_id);
        }
        Date now = new Date();
        //1、更新用户表登录ip，登录次数等
        user.setUpdateTime(now);
        user.setLastLoginTime(now);
        if(user!=null&&user.getMobile()!=null){
            /*Map<String, String> rmp = SignUtil.encryNameAndMobile(user.getMobile());
            user.setUserName(rmp.get("mobile"));*/
            user.setUserName(user.getMobile());
        }
        Integer result = userService.update(user);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, result);
    }

    /**
     * 通过手机号来修改用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/updateByMobile",method = RequestMethod.POST)
    public BaseResp<Integer> updateByMobile(User user){
        if(user==null||user.getMobile()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_mobile_empty);
        }

        User byMobile = userService.findByMobile(user.getMobile());
        if(byMobile==null){
            return new BaseResp<>(ResultStatus.error_para_user_empty);
        }
        Integer integer = userService.updateByMobile(user);
        return new BaseResp<Integer>(ResultStatus.SUCCESS,integer);
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

    @RequestMapping(value = "/batch", method = RequestMethod.GET)
    public BaseResp<?> batchAddUser(String date,Integer num){
        Long count = 0L;
        if(num == null){
            num = 1000;
        }
        for(int i = 0; i< num; i++){
            User in = new User();
            in.setUserName("xq_"+System.currentTimeMillis());
            in.setPassword(RandomStringUtil.getRandomCode(6,3));
            in.setPassword(DigestUtils.md5DigestAsHex(in.getPassword().getBytes()));
            in.setOpenId("ogDNV4"+RandomStringUtil.getRandomCode(22, 6));
            in.setSourceType(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                if(StringUtils.isNotEmpty(date)){
                    Date d = sdf.parse(date);
                    in.setCreateTime(d);
                    in.setUpdateTime(d);
                }else{
                    Date d = new Date();
                    in.setCreateTime(d);
                    in.setUpdateTime(d);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Long id  = userService.add(in);
            count++;
        }

        return new BaseResp<Long>(ResultStatus.SUCCESS, count);
    }

}
