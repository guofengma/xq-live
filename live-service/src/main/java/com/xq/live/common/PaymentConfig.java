package com.xq.live.common;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 微信支付常量配置
 *
 * @author zhangpeng32
 * @date 2018-03-09 11:06
 **/
public class PaymentConfig extends WXPayConfig {

    //小程序appid
    public static final String APPID = "wxf91e2a026658e78e";
    //微信支付的商户id
    public static final String MCH_ID = "1499658152";
    //微信支付的商户密钥
    public static final String API_KEY = "26fe9bdfa1f9f9607f8ba337a97a6111";
    //支付成功后的服务器回调url
    public static final String WX_NOTIFY_URL = "https://hbxq001.cn/wxpay/wxNotify";

    public static final String GRANT_TYPE = "authorization_code";

    //签名方式，固定值
    public static final String SIGN_TYPE = "MD5";
    //交易类型，小程序支付的固定值为JSAPI
    public static final String TRADE_TYPE = "JSAPI";
    //微信统一下单接口地址
    public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static final String GET_OPEN_ID_URL = "https://api.weixin.qq.com/sns/jscode2session";

    private byte[] certData;

    private static PaymentConfig INSTANCE;

    private PaymentConfig() throws Exception {
        //        String certPath = "D://CERT/common/apiclient_cert.p12";
        //        File file = new File(certPath);
        //        InputStream certStream = new FileInputStream(file);
        //        this.certData = new byte[(int) file.length()];
        //        certStream.read(this.certData);
        //        certStream.close();
    }

    public static PaymentConfig getInstance() throws Exception {
        if (INSTANCE == null) {
            synchronized (PaymentConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PaymentConfig();
                }
            }
        }
        return INSTANCE;
    }


    public String getAppID() {
        return APPID;
    }

    public String getMchID() {
        return MCH_ID;
    }

    public String getKey() {
        return API_KEY;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }


    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    public IWXPayDomain getWXPayDomain() {
        return WXPayDomainSimpleImpl.instance();
    }

    public String getPrimaryDomain() {
        return "api.mch.weixin.qq.com";
    }

    public String getAlternateDomain() {
        return "api2.mch.weixin.qq.com";
    }

    @Override
    public int getReportWorkerNum() {
        return 1;
    }

    @Override
    public int getReportBatchSize() {
        return 2;
    }
}
