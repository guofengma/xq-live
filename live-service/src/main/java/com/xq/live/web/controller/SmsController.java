package com.xq.live.web.controller;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.xq.live.common.BaseResp;
import com.xq.live.common.Constants;
import com.xq.live.common.RandomStringUtil;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.SmsSend;
import com.xq.live.service.SmsSendService;
import com.xq.live.service.UserService;
import com.xq.live.vo.in.SmsSendInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.out.SmsOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-22 13:09
 * @copyright:hbxq
 **/
@RestController
@RequestMapping(value = "/sms")
public class SmsController {

    @Autowired
    private SmsSendService smsSendService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public BaseResp<Long> send(@Valid SmsSendInVo inVo, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        try {
            SmsSingleSender sender = new SmsSingleSender(Constants.SMS_APP_ID, Constants.SMS_APP_KEY);
            ArrayList<String> params = new ArrayList<String>();
            params.add(inVo.getShopName());
            params.add(inVo.getUserName());
            inVo.setCreateTime(new Date());
            params.add(inVo.getCreateTime().toLocaleString());
            params.add(inVo.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            SmsSingleSenderResult ret = sender.sendWithParam(Constants.SMS_NATION_CODE, inVo.getShopMobile(), Constants.TEMP_ID_PAID_SUCCESS, params, "", "", "");
            if (ret.result == 0) {//短信发送成功
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_SUCCESS);
            } else {
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_FAIL);
            }
            inVo.setRemark(ret.errMsg);
            inVo.setSmsContent(this.getPaidSmsMsg(inVo, params));
            Long id = smsSendService.create(inVo);
            return new BaseResp<Long>(ResultStatus.SUCCESS, id);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<Long>(ResultStatus.FAIL);
        }
    }

    /**
     * 用户注册发送验证码
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/sendForRegister", method = RequestMethod.POST)
    public BaseResp<SmsOut> sendForRegister(SmsSendInVo inVo) {
        if(inVo==null||inVo.getShopMobile()==null||inVo.getUserId()==null||inVo.getUserName()==null){
            return new BaseResp<SmsOut>(ResultStatus.error_param_empty);
        }

        inVo.setSmsType(SmsSend.SMS_TYPE_VERTIFY);
        SmsOut smsOut = smsSendService.redisVerify(inVo);
        //判断缓存是否存在，并且在10分钟以内
        if(smsOut!=null){
           return new BaseResp<SmsOut>(ResultStatus.SUCCESS,smsOut);
        }

        try {
            SmsSingleSender sender = new SmsSingleSender(Constants.SMS_APP_ID, Constants.SMS_APP_KEY);
            ArrayList<String> params = new ArrayList<String>();
            String randomCode = RandomStringUtil.getRandomCode(4, 0);
            inVo.setCreateTime(new Date());
            params.add(randomCode);
            SmsSingleSenderResult ret = sender.sendWithParam(Constants.SMS_NATION_CODE, inVo.getShopMobile(), Constants.TEMP_ID_VERIFY_SUCCESS, params, "", "", "");
            if (ret.result == 0) {//短信发送成功
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_SUCCESS);
            } else {
                inVo.setSendStatus(SmsSend.SMS_SEND_STATUS_FAIL);
            }
            inVo.setRemark(ret.errMsg);
            inVo.setSmsContent(randomCode);
            Long id = smsSendService.create(inVo);
            SmsOut smsOutForNew = smsSendService.redisVerify(inVo);//再把数据缓存
            return new BaseResp<SmsOut>(ResultStatus.SUCCESS, smsOutForNew);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResp<SmsOut>(ResultStatus.FAIL);
        }
    }

    /**
     *判断验证码是否正确,并且更新user表,返回一个通行的userId
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/isVerify",method = RequestMethod.GET)
    public BaseResp<Long> isVerify(SmsSendInVo inVo){
        if(inVo==null||inVo.getShopMobile()==null||inVo.getSmsContent()==null||inVo.getUserId()==null||inVo.getUserName()==null){
            return new BaseResp<Long>(ResultStatus.error_param_empty);
        }
        inVo.setSmsType(SmsSend.SMS_TYPE_VERTIFY);
        Long verify = smsSendService.isVerify(inVo);
        if(verify==-1||verify==null){
            return new BaseResp<Long>(ResultStatus.FAIL,verify);
        }
        UserAccountInVo accountInVo = new UserAccountInVo();
        accountInVo.setUserId(inVo.getUserId());
        accountInVo.setUserName(inVo.getUserName());
        accountInVo.setAccountName(inVo.getShopMobile());
        //判断更新account_name是否成功
        int i= userService.updateByUserID(accountInVo);
        if (i==0){
            return new BaseResp<Long>(ResultStatus.error_act_update);
        }
        return new BaseResp<Long>(ResultStatus.SUCCESS,verify);


    }

    /**
     * 组装支付成功短信通知内容
     * @param inVo
     * @param params
     * @return
     */
    private String getPaidSmsMsg(SmsSendInVo inVo, List<String> params) {
        /*尊敬的 {1}，顾客{2}在{3}成功支付￥{4}元，您可以通过享7平台进行提现操作，谢谢。*/
        StringBuffer msgSb = new StringBuffer("");
        msgSb.append("尊敬的 ");
        msgSb.append(params.get(0)).append(",");
        msgSb.append("顾客").append(params.get(1));
        msgSb.append(params.get(2)).append("成功支付");
        msgSb.append("￥").append(params.get(3)).append("元，您可以通过享7平台进行提现操作，谢谢。");
        return msgSb.toString();
    }

}
