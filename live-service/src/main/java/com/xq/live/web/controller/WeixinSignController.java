package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.vo.in.WeixinSignInVo;
import com.xq.live.web.utils.SignUtil;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by lipeng on 2018/3/15.
 */

@RestController
@RequestMapping(value = "/wxsign")
public class WeixinSignController {

    @RequestMapping(value = "/isSign",method = RequestMethod.GET)
    public BaseResp<String> isSign(@Valid WeixinSignInVo inVo, HttpServletRequest request,BindingResult result){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<String>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        String signature = inVo.getSignature();
        String timestamp = inVo.getTimestamp();
        String nonce = inVo.getNonce();
        String echostr = inVo.getEchostr();
        String ua = ((HttpServletRequest) request).getHeader("user-agent").toLowerCase();
        if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
            return new BaseResp<String>(ResultStatus.SUCCESS,echostr);
        }
        boolean b = SignUtil.checkSignature(signature, timestamp, nonce);
        if(b==true){
            return new BaseResp<String>(ResultStatus.SUCCESS,"token验证正确");
        }
        return new BaseResp<String>(ResultStatus.FAIL,"token验证错误");

    }
}
