package com.xq.live.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.xq.live.common.BaseResp;
import com.xq.live.common.RandomStringUtil;
import com.xq.live.common.ResultStatus;
import com.xq.live.common.WeixinPayConstants;
import com.xq.live.model.So;
import com.xq.live.service.SoService;
import com.xq.live.vo.in.WeixinInVo;
import com.xq.live.vo.out.SoOut;
import com.xq.live.web.utils.IpUtils;
import com.xq.live.web.utils.PayUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-03-09 10:54
 * @copyright:hbxq
 **/
@RestController
@RequestMapping("/wxpay")
public class WeixinPayController {

    @Autowired
    private SoService soService;

    @RequestMapping(value = "/getOpenId", method = RequestMethod.POST)
    public BaseResp<Map<String, Object>> login(String code, HttpServletRequest request) {
        if (StringUtils.isEmpty(code)) {
            return new BaseResp<Map<String, Object>>(ResultStatus.error_weixin_user_code_empty);
        }

        //拼接参数
        String param = "?grant_type=" + WeixinPayConstants.grant_type + "&appid=" + WeixinPayConstants.appid + "&secret=" + WeixinPayConstants.key + "&js_code=" + code;
        System.out.println("https://api.weixin.qq.com/sns/jscode2session" + param);

        //创建请求对象
        String httpRet = PayUtils.httpRequest("https://api.weixin.qq.com/sns/jscode2session", "GET", param);
        System.out.println(httpRet);
        Map<String, Object> result = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.parseObject(httpRet);
        if (jsonObject != null) {
            Integer errcode = jsonObject.getInteger("errcode");
            if (errcode != null) {
                //返回异常信息
                return new BaseResp<Map<String, Object>>(errcode, jsonObject.getString("errmsg"), null);
            }
            result.put("openId", jsonObject.getString("openid"));
            result.put("sessionKey", jsonObject.getString("session_key"));
        }
        return new BaseResp<Map<String, Object>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 微信支付调用后台
     * @param inVo
     * @param request
     * @return
     */
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public BaseResp<Map<String, Object>> pay(@Valid WeixinInVo inVo, HttpServletRequest request, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            List<ObjectError> list = bindingResult.getAllErrors();
            return new BaseResp<Map<String, Object>>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }

        //根据id获取订单信息
        SoOut soOut = soService.get(inVo.getSoId());
        if(soOut == null){
            return new BaseResp<Map<String, Object>>(ResultStatus.error_so_not_exist);
        }
        //订单已支付
        if(soOut.getSoStatus() != So.SO_STATUS_WAIT_PAID){
            return new BaseResp<Map<String, Object>>(ResultStatus.error_so_paid);
        }

        try{
            //生成的随机字符串
            String nonce_str = RandomStringUtil.getRandomCode(32, 3);
            //获取客户端的ip地址
            String spbill_create_ip = IpUtils.getIpAddr(request);
            //组装参数，用户生成统一下单接口的签名
            Map<String, String> packageParams = new HashMap<String, String>();
            packageParams.put("appid", WeixinPayConstants.appid);
            packageParams.put("mch_id", WeixinPayConstants.mch_id);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", soOut.getSkuName());    //商品描述
//            packageParams.put("detail", "testdetail");    //商品描述
            packageParams.put("out_trade_no", soOut.getId().toString());//商户订单号
            packageParams.put("total_fee", Integer.valueOf(soOut.getSoAmount().multiply(new BigDecimal(100)).intValue()).toString());//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("spbill_create_ip", spbill_create_ip);
            packageParams.put("notify_url", WeixinPayConstants.notify_url);//支付成功后的回调地址
            packageParams.put("trade_type", WeixinPayConstants.TRADETYPE);//支付方式
            packageParams.put("openid", inVo.getOpenId());

            String reqStrXMl = WXPayUtil.mapToXml(packageParams);
            System.out.println("reqStrXMl : "+reqStrXMl);
            String sign = WXPayUtil.generateSignature(packageParams, WeixinPayConstants.key);
            System.out.println("sign : "+sign);
            String xml = WXPayUtil.generateSignedXml(packageParams, WeixinPayConstants.key);

            System.out.println("xml : "+xml);
            //调用统一下单接口，并接受返回的结果
            String result = PayUtils.httpRequest(WeixinPayConstants.pay_url, "POST", xml);


            System.out.println("调试模式_统一下单接口 返回XML数据：" + result);

            // 将解析结果存储在HashMap中
            Map map = PayUtils.doXMLParse(result);

            String return_code = (String) map.get("return_code");//返回状态码

            Map<String, Object> response = new HashMap<String, Object>();//返回给小程序端需要的参数
            if(return_code=="SUCCESS"||return_code.equals(return_code)){
                String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                response.put("nonceStr", nonce_str);
                response.put("package", "prepay_id=" + prepay_id);
                Long timeStamp = System.currentTimeMillis() / 1000;
                response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                //拼接签名需要的参数
                String stringSignTemp = "appId=" + WeixinPayConstants.appid + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id+ "&signType=MD5&timeStamp=" + timeStamp;
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String paySign = PayUtils.sign(stringSignTemp, WeixinPayConstants.key, "utf-8").toUpperCase();

                response.put("paySign", paySign);
            }

            response.put("appid", WeixinPayConstants.appid);

            return new BaseResp<Map<String, Object>>(ResultStatus.SUCCESS, response);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new BaseResp<Map<String, Object>>(ResultStatus.FAIL);
    }

    /**
     * @Description:微信支付
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/notify")
    @ResponseBody
    public void wxNotify(HttpServletRequest request,HttpServletResponse response)throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        System.out.println("接收到的报文：" + notityXml);

        Map map = PayUtils.doXMLParse(notityXml);

        String returnCode = (String) map.get("return_code");
        if("SUCCESS".equals(returnCode)){
            //验证签名是否正确
            if(PayUtils.verify(PayUtils.createLinkString(map), (String)map.get("sign"), WeixinPayConstants.key, "utf-8")){
                /**此处添加自己的业务逻辑代码start**/


                /**此处添加自己的业务逻辑代码end**/
                //通知微信服务器已经支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        System.out.println(resXml);
        System.out.println("微信支付回调数据结束");
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }
}
