package com.xq.live.service.impl;

import com.xq.live.dao.SmsSendMapper;
import com.xq.live.model.SmsSend;
import com.xq.live.service.SmsSendService;
import com.xq.live.vo.in.SmsSendInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Long create(SmsSendInVo inVo) {
        int ret = smsSendMapper.create(inVo);
        if(ret < 0){
            return null;
        }
        return inVo.getId();
    }
}
