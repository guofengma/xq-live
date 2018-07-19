package com.xq.live.web.exception;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @package: com.xq.live.web.exception
 * @description: jwt 未授权(身份验证异常处理)
 * @author: zhangpeng32
 * @date: 2018/4/20 17:26
 * @version: 1.0
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        JSONObject result = new JSONObject();
        JSONObject header = new JSONObject();
        if (authException instanceof BadCredentialsException) { /**身份认证未通过*/
            header.put("errorcode", "8002");
            header.put("errorinfo", "用户名或密码错误，请重新输入！");
            result.put("header", header);
        } else {
            header.put("errorcode", "8001");
            header.put("errorinfo", "无效的token");
            result.put("header", header);
        }
        response.getWriter().write(JSONObject.toJSONString(result));
    }
}
