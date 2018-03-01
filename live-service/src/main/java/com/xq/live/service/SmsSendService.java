package com.xq.live.service;

import com.xq.live.model.SmsSend;
import com.xq.live.vo.in.SmsSendInVo;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-22 14:20
 * @copyright:hbxq
 **/
public interface SmsSendService {
    /**
     * 新增
     * @param inVo
     * @return
     */
    Long create(SmsSendInVo inVo);
}
