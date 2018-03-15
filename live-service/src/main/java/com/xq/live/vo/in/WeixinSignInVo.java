package com.xq.live.vo.in;

import javax.validation.constraints.NotNull;

/**
 * 微信token验证入参
 * Created by lipeng on 2018/3/15.
 */
public class WeixinSignInVo {
    @NotNull(message = "signature必填")
    private String signature;//微信加密签名
    @NotNull(message = "timestamp必填")
    private String timestamp;//时间戳
    @NotNull(message = "nonce必填")
    private String nonce;//随机数
    @NotNull(message = "echostr必填")
    private String echostr;//随机字符串

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getEchostr() {
        return echostr;
    }

    public void setEchostr(String echostr) {
        this.echostr = echostr;
    }
}
