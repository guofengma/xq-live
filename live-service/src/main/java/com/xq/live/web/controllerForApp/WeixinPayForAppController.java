package com.xq.live.web.controllerForApp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.xq.live.common.BaseResp;
import com.xq.live.common.PaymentConfig;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Shop;
import com.xq.live.model.So;
import com.xq.live.service.ShopService;
import com.xq.live.service.ServiceAmountService;
import com.xq.live.service.SoService;
import com.xq.live.service.SoWriteOffService;
import com.xq.live.vo.in.ServiceAmountInVo;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.in.SoWriteOffInVo;
import com.xq.live.vo.in.WeixinInVo;
import com.xq.live.vo.out.SoOut;
import com.xq.live.web.utils.IpUtils;
import com.xq.live.web.utils.PayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信支付服务端controller
 * @author lipeng
 * @date 2018-03-09 10:54
 * @copyright:hbxq
 */
@RestController
@RequestMapping("/app/wxpay")
public class WeixinPayForAppController {
    private Logger logger = Logger.getLogger(WeixinPayForAppController.class);

    @Autowired
    private SoService soService;

    @Autowired
    private ShopService ShopService;

    @Autowired
    private ServiceAmountService serviceAmountService;

    @Autowired
    private SoWriteOffService soWriteOffService;

    private WXPay wxpay;
    private PaymentConfig config;

    public WeixinPayForAppController() throws Exception {
        config = PaymentConfig.getInstance();
        wxpay = new WXPay(config, WXPayConstants.SignType.MD5);
    }

    /**
     * @Description: 获取openId
     * @param: code
     * @Author: zhangpeng32
     * @Date: 2018/3/11 17:39
     * @Version: 1.0.0
     */
    @RequestMapping(value = "/getOpenId", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> getOpenId(String code, HttpServletRequest request) throws Exception{
        if (StringUtils.isEmpty(code)) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_weixin_user_code_empty);
        }
        //获取openId
        String param = "?grant_type=" + PaymentConfig.GRANT_TYPE + "&appid=" + PaymentConfig.APPID + "&secret=" + PaymentConfig.APP_SECRET + "&js_code=" + code;
        System.out.println(PaymentConfig.GET_OPEN_ID_URL + param);
        //创建请求对象
        String httpRet = PayUtils.httpRequest(PaymentConfig.GET_OPEN_ID_URL, "GET", param);
        Map<String, String> result = new HashMap<String, String>();
        JSONObject jsonObject = JSONObject.parseObject(httpRet);
        if (jsonObject != null) {
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode != null) {
                //返回异常信息
                return new BaseResp<Map<String, String>>(errcode, jsonObject.getString("errmsg"), null);
            }
            result.put("openId", jsonObject.getString("openid"));
            result.put("sessionKey", jsonObject.getString("session_key"));
        }
        return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 微信支付调用后台
     *
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> pay(@Valid WeixinInVo inVo, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        //根据id获取订单信息
        SoOut soOut = soService.get(inVo.getSoId());
        if (soOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }

        try {
            //生成的随机字符串
            String nonce_str = WXPayUtil.generateNonceStr();
            ;
            //获取客户端的ip地址
            String spbill_create_ip = IpUtils.getIpAddr(request);

            int price100 = soOut.getSoAmount().multiply(new BigDecimal(100)).intValue();
            //组装参数，用户生成统一下单接口的签名
            Map<String, String> packageParams = new HashMap<String, String>();
            packageParams.put("appid", PaymentConfig.APPID);
            packageParams.put("mch_id", PaymentConfig.MCH_ID);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", soOut.getSkuName());    //商品描述
            //            packageParams.put("detail", "testdetail");    //商品描述
            packageParams.put("out_trade_no", soOut.getId().toString());//商户订单号
            packageParams.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("spbill_create_ip", spbill_create_ip);
            packageParams.put("notify_url", PaymentConfig.WX_NOTIFY_URL);//支付成功后的回调地址
            packageParams.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
            packageParams.put("openid", inVo.getOpenId());

            String reqStrXMl = WXPayUtil.mapToXml(packageParams);
            System.out.println("reqStrXMl : " + reqStrXMl);
            String sign = WXPayUtil.generateSignature(packageParams, PaymentConfig.API_KEY);
            System.out.println("sign : " + sign);
            String xml = WXPayUtil.generateSignedXml(packageParams, PaymentConfig.API_KEY);

            System.out.println("xml : " + xml);
            //调用统一下单接口，并接受返回的结果
            String result = PayUtils.httpRequest(PaymentConfig.UNIFIED_ORDER_URL, "POST", xml);


            System.out.println("调试模式_统一下单接口 返回XML数据：" + result);

            // 将解析结果存储在HashMap中
            Map map = WXPayUtil.xmlToMap(result);

            String return_code = (String) map.get("return_code");//返回状态码

            Map<String, String> response = new HashMap<String, String>();//返回给小程序端需要的参数
            response.put("appid", PaymentConfig.APPID);
            if (return_code == "SUCCESS" || return_code.equals(return_code)) {
                String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                response.put("prepayid", prepay_id);
                response.put("noncestr", nonce_str);
                Long timeStamp = System.currentTimeMillis() / 1000;
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                response.put("package", "Sign=WXPay");
                //                //拼接签名需要的参数
                //                String stringSignTemp = "appId=" + PaymentConfig.APPID + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id+ "&signType=MD5&timeStamp=" + timeStamp;
                //                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                //                String paySign = PayUtils.sign(stringSignTemp, PaymentConfig.API_KEY, "utf-8").toUpperCase();
                String paySign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", paySign);
            }

            return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BaseResp<Map<String, String>>(ResultStatus.FAIL);
    }

    /**
     * 小程序上的支付(支付到享7平台)
     * @param inVo
     * @param request
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/doUnifiedOrder", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> doUnifiedOrder(@Valid WeixinInVo inVo, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        //根据id获取订单信息
        SoOut soOut = soService.get(inVo.getSoId());
        if (soOut == null) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if (soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID) {
            return new BaseResp<Map<String, String>>(ResultStatus.error_so_paid);
        }

        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        int price100 = soOut.getSoAmount().multiply(new BigDecimal(100)).intValue();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", soOut.getSkuName());    //商品描述
        data.put("out_trade_no", soOut.getId().toString());//商户订单号
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentConfig.WX_NOTIFY_URL);//支付成功后的回调地址
        data.put("trade_type", PaymentConfig.TRADE_TYPE);//支付方式
        data.put("openid", inVo.getOpenId());

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appId", config.getAppID());
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
//                response.put("prepayid", rMap.get("prepay_id"));
                response.put("package", "prepay_id="+prepayid);
                response.put("signType", "MD5");
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("paySign", sign);
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }

    /**
     * 商家端app上面的支付(支付到享7平台)
     * @param inVo
     * @param request
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/payForShopApp", method = RequestMethod.POST)
    public BaseResp<Map<String, String>> payForShopApp(@Valid WeixinInVo inVo, HttpServletRequest request, BindingResult bindingResult) {
        if(inVo.getShopId()==null||inVo.getServicePrice()==null||inVo.getBeginTime()==null||inVo.getEndTime()==null||inVo.getUserId()==null){
            return new BaseResp<Map<String, String>>(ResultStatus.error_param_empty);
        }


        //判断商家是否存在
        Shop shopById = ShopService.getShopById(inVo.getShopId());
        if(shopById==null||shopById.getIsDeleted()==1){
            return new BaseResp<Map<String, String>>(ResultStatus.error_shop_info_empty);
        }


        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        String spbill_create_ip = IpUtils.getIpAddr(request);

        int price100 = inVo.getServicePrice().multiply(new BigDecimal(100)).intValue();

        Long date = new Date().getTime();
        //统一下单接口
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getShopAppID());//用商家端的APPID
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("body", shopById.getShopName()+"-缴纳服务费");    //商品描述
        data.put("out_trade_no", data.toString());//商户订单号------用的是当前系统时间
        data.put("total_fee", String.valueOf(price100));//支付金额，这边需要转成字符串类型，否则后面的签名会失败
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("notify_url", PaymentConfig.WX_NOTIFY_SHOP_APP_URL);//支付成功后的回调地址
        data.put("trade_type", PaymentConfig.TRADE_TYPE_APP);//支付方式
        data.put("attach", JSON.toJSONString(inVo));
        //data.put("openid", inVo.getOpenId());//app微信支付不需要openId

        //返回给小程序端需要的参数
        Map<String, String> response = new HashMap<String, String>();
        response.put("appid", config.getShopAppID());
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = rMap.get("return_code");//返回状态码
            String result_code = rMap.get("result_code");//
            String nonceStr = WXPayUtil.generateNonceStr();
            response.put("noncestr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
                response.put("prepayid", rMap.get("prepay_id"));//app微信支付需要prepayid
                response.put("package", "Sign=WXPay");
//                response.put("signType", "MD5");//app微信支付不需要signType
                response.put("partnerid", config.getMchID());
                response.put("timestamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                System.out.println("二次签名参数response ： "+response);

                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(response, PaymentConfig.API_KEY);
                response.put("sign", sign);//小程序里面是paySign,app支付里面是sign
                System.out.println("生成的签名paySign : "+ sign);
                return new BaseResp<Map<String, String>>(ResultStatus.SUCCESS, response);
            }else{
                return new BaseResp<Map<String, String>>(ResultStatus.error_unified_order_fail.getErrorCode(), rMap.get("err_code_des"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Map<String, String>>(ResultStatus.FAIL, response);
        }
    }


    /**
     * 微信支付结果通知(小程序)
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotify")
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String openid = (String) notifyMap.get("openid");  //用户标识
                String out_trade_no = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号
                //查询订单 根据订单号查询订单  SoOut -订单实体类
                Long soId = Long.valueOf(out_trade_no);
                SoOut soOut = soService.get(soId);
                if (!PaymentConfig.MCH_ID.equals(mch_id) || soOut == null || new BigDecimal(total_fee).compareTo(soOut.getSoAmount().multiply(new BigDecimal(100))) != 0) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                    if (So.SO_STATUS_WAIT_PAID == soOut.getSoStatus()) {//支付的状态判断
                        //订单状态的修改。根据实际业务逻辑执行
                        SoInVo inVo = new SoInVo();
                        inVo.setId(soOut.getId());
                        inVo.setUserId(soOut.getUserId());
                        inVo.setUserName(soOut.getUserName());
                        inVo.setSkuId(soOut.getSkuId());
                        inVo.setSkuNum(soOut.getSkuNum());
                        int ret = soService.paid(inVo);
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        logger.info("订单已处理");
                    }
                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 微信支付结果通知(商家端app)
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/wxNotifyForShopApp")
    public void wxNotifyForShopApp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //读取参数
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        //sb为微信返回的xml
        String notifyData = sb.toString();  //支付结果通知的xml格式数据
        System.out.println("支付结果通知的xml格式数据：" + notifyData);

        Map notifyMap = WXPayUtil.xmlToMap(notifyData);       // 转换成map
        String resXml = "";
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            // 签名正确
            if ("SUCCESS".equals(notifyMap.get("result_code"))) {
                //这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String) notifyMap.get("mch_id"); //商户号
                String out_trade_no = (String) notifyMap.get("out_trade_no"); //商户订单号
                String total_fee = (String) notifyMap.get("total_fee");
                String transaction_id = (String) notifyMap.get("transaction_id"); //微信支付订单号
                String attach = (String) notifyMap.get("attach");
                WeixinInVo weixinInVo = JSON.parseObject(attach, WeixinInVo.class);//将附带参数读取出来

                if (!PaymentConfig.MCH_ID.equals(mch_id)) {
                    logger.info("支付失败,错误信息：" + "参数错误");
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
                } else {
                    //添加缴费记录begin
                    ServiceAmountInVo serviceAmountInVo = new ServiceAmountInVo();
                    serviceAmountInVo.setPaidUserId(weixinInVo.getUserId());
                    serviceAmountInVo.setShopId(weixinInVo.getShopId());
                    serviceAmountInVo.setServicePrice(weixinInVo.getServicePrice());
                    serviceAmountInVo.setBeginTime(weixinInVo.getBeginTime());
                    serviceAmountInVo.setEndTime(weixinInVo.getEndTime());
                    Long id = serviceAmountService.add(serviceAmountInVo);
                    //添加缴费记录end
                    //将缴费时间段内的核销的票券给结算掉
                    SoWriteOffInVo soWriteOffInVo = new SoWriteOffInVo();
                    soWriteOffInVo.setShopId(weixinInVo.getShopId());
                    soWriteOffInVo.setBegainTime(weixinInVo.getBeginTime());
                    soWriteOffInVo.setEndTime(weixinInVo.getEndTime());
                    int kk = soWriteOffService.updateByShopId(soWriteOffInVo);
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

                }
            } else {
                logger.info("支付失败,错误信息：" + notifyMap.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        } else {
            // 签名错误，如果数据里没有sign字段，也认为是签名错误
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
            logger.info("通知签名验证失败");
        }
        //------------------------------
        //处理业务完毕
        //------------------------------
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }
}
