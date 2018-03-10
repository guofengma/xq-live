package com.xq.live.common;

/**
 * 微信支付常量配置
 *
 * @author zhangpeng32
 * @date 2018-03-09 11:06
 **/
public class WeixinPayConstants {

    //小程序appid
    public static final String appid = "wxf91e2a026658e78e";
    //微信支付的商户id
    public static final String mch_id = "1499658152";
    //微信支付的商户密钥
    public static final String key = "f8fa91ff595a529f2b208b5cd859e693";
    //支付成功后的服务器回调url
    public static final String notify_url = "https://hbxq001.cn/wxpay/notify";

    public static final String grant_type = "authorization_code";

    //签名方式，固定值
    public static final String SIGNTYPE = "MD5";
    //交易类型，小程序支付的固定值为JSAPI
    public static final String TRADETYPE = "JSAPI";
    //微信统一下单接口地址
    public static final String pay_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
}
