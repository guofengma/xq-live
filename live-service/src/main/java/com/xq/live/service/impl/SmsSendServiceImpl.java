package com.xq.live.service.impl;

import com.xq.live.common.RedisCache;
import com.xq.live.dao.SmsSendMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.SmsSend;
import com.xq.live.model.User;
import com.xq.live.service.SmsSendService;
import com.xq.live.vo.in.SmsSendInVo;
import com.xq.live.vo.out.SmsOut;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-22 14:25
 * @copyright:hbxq
 **/
@Service
public class SmsSendServiceImpl implements SmsSendService {

    @Autowired
    private SmsSendMapper smsSendMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserMapper userMapper;


    @Override
    public Long create(SmsSendInVo inVo) {
        int ret = smsSendMapper.create(inVo);
        if(ret < 0){
            return null;
        }
        return inVo.getId();
    }

    @Override
    public SmsOut redisVerify(SmsSendInVo inVo) {
        String key = "redisVerify_" + inVo.getShopMobile();
        SmsOut smsOut = redisCache.get(key, SmsOut.class);
        SmsSend smsSend = new SmsSend();
        Long time = System.currentTimeMillis();
        if (smsOut == null) {
            smsSend = smsSendMapper.selectByMobile(inVo);
            if(smsSend==null||smsSend.getSmsContent()==null||smsSend.getCreateTime()==null){
                return null;
            }
            if(time > (smsSend.getCreateTime().getTime() + 3600000)){
                smsSendMapper.deleteByPrimaryKey(smsSend.getId());
                redisCache.del(key);
                return null;
            }
            smsOut = new SmsOut();
            smsOut.setVerifyId(smsSend.getSmsContent());
            smsOut.setVeridyTime(smsSend.getCreateTime());
            redisCache.set(key, smsOut, 1l, TimeUnit.HOURS);
            return smsOut;
        }

        if (time > (smsOut.getVeridyTime().getTime() + 600000)) {    //如果10分钟之后，则清空数据
            smsSend = smsSendMapper.selectByMobile(inVo);
            smsSendMapper.deleteByPrimaryKey(smsSend.getId());
            redisCache.del(key);
            return null;
        }

        return smsOut;

    }

    @Override
    public SmsOut redisVerifyForApp(SmsSendInVo inVo) {
        String key = "redisVerifyForApp_" + inVo.getShopMobile();
        SmsOut smsOut = redisCache.get(key, SmsOut.class);
        SmsSend smsSend = new SmsSend();
        Long time = System.currentTimeMillis();
        if (smsOut == null) {
            smsSend = smsSendMapper.selectByMobile(inVo);
            if(smsSend==null||smsSend.getSmsContent()==null||smsSend.getCreateTime()==null){
                return null;
            }
            if(time > (smsSend.getCreateTime().getTime() + 3600000)){
                smsSendMapper.deleteByPrimaryKey(smsSend.getId());
                redisCache.del(key);
                return null;
            }
            smsOut = new SmsOut();
            smsOut.setVerifyId(smsSend.getSmsContent());
            smsOut.setVeridyTime(smsSend.getCreateTime());
            redisCache.set(key, smsOut, 1l, TimeUnit.HOURS);
            return smsOut;
        }

        if (time > (smsOut.getVeridyTime().getTime() + 600000)) {    //如果10分钟之后，则清空数据
            smsSend = smsSendMapper.selectByMobile(inVo);
            smsSendMapper.deleteByPrimaryKey(smsSend.getId());
            redisCache.del(key);
            return null;
        }

        return smsOut;

    }

    @Override
    public SmsOut redisVerifyForShopApp(SmsSendInVo inVo) {
        String key = "redisVerifyForShopApp_" + inVo.getShopMobile();
        SmsOut smsOut = redisCache.get(key, SmsOut.class);
        SmsSend smsSend = new SmsSend();
        Long time = System.currentTimeMillis();
        if (smsOut == null) {
            smsSend = smsSendMapper.selectByMobile(inVo);
            if(smsSend==null||smsSend.getSmsContent()==null||smsSend.getCreateTime()==null){
                return null;
            }
            if(time > (smsSend.getCreateTime().getTime() + 3600000)){
                smsSendMapper.deleteByPrimaryKey(smsSend.getId());
                redisCache.del(key);
                return null;
            }
            smsOut = new SmsOut();
            smsOut.setVerifyId(smsSend.getSmsContent());
            smsOut.setVeridyTime(smsSend.getCreateTime());
            redisCache.set(key, smsOut, 1l, TimeUnit.HOURS);
            return smsOut;
        }

        if (time > (smsOut.getVeridyTime().getTime() + 600000)) {    //如果10分钟之后，则清空数据
            smsSend = smsSendMapper.selectByMobile(inVo);
            smsSendMapper.deleteByPrimaryKey(smsSend.getId());
            redisCache.del(key);
            return null;
        }

        return smsOut;

    }

    @Override
    public Integer isVerify(SmsSendInVo inVo) {
        SmsSend smsSend = smsSendMapper.selectByMobile(inVo);
        if(smsSend==null||smsSend.getSmsContent()==null){
            return null;
        }
        if(!StringUtils.equals(inVo.getSmsContent(),smsSend.getSmsContent())){
            return -1;
        }

        User user = userMapper.selectByPrimaryKey(inVo.getUserId());
        user.setUserName(smsSend.getShopMobile());
        user.setMobile(smsSend.getShopMobile());
        int i = userMapper.updateByPrimaryKeySelective(user);
        return i;


    }

    @Override
    public Integer isVerifyForShopApp(SmsSendInVo inVo) {
        SmsSend smsSend = smsSendMapper.selectByMobile(inVo);
        if(smsSend==null||smsSend.getSmsContent()==null){
            return null;
        }
        if(!StringUtils.equals(inVo.getSmsContent(),smsSend.getSmsContent())){
            return -1;
        }
        User user = new User();
        user.setOpenId(inVo.getOpenId());
        if(inVo.getIconUrl()!=null&&!StringUtils.equals("",inVo.getIconUrl())){

        }

        return null;
    }
}
