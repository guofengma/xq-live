package com.xq.live.web.controller;

import com.xq.live.exception.UsernameIsExitedException;
import com.xq.live.model.User;
import com.xq.live.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangpeng32 on 2017/12/14.
 */

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public User getUserbyId(@PathVariable("id") Long id) {
        User user = user = userService.getUserById(id);
        return user;
    }

    @RequestMapping(value = "/add/{username}", method = RequestMethod.GET)
    public Long addUser(@PathVariable("username") String username){
        User user = new User();
        user.setUserName(username);
        user.setPassword("123456");
        Long id  = userService.insert(user);
        return id;
    }

/*        public UserController(UserRepository myUserRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = myUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }*/

    @RequestMapping("/userList")
    @ResponseBody
    public Map<String, Object> userList(){
        List<User> myUsers = userService.findAll();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("users",myUsers);
        return map;
    }

    /**
     * 该方法是注册用户的方法，默认放开访问控制
     * @param user
     */
    @PostMapping("/signup")
    public void signUp(@RequestBody User user) {
        User user1 = userService.findByUsername(user.getUserName());
        if(null != user1){
            throw new UsernameIsExitedException("用户已经存在~");
        }
        user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword()).getBytes()));
        user.setCreateTime(new Date());
        userService.insert(user);
    }

//    @RequestMapping(value = "/get/{userName}", method = RequestMethod.GET)
//    public User getUserByName(@PathVariable("userName") String userName){
//        User user = userService.findByUsername(userName);
//        return user;
//    }
}
