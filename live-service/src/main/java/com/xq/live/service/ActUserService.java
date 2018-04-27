package com.xq.live.service;

import com.xq.live.model.ActUser;
import com.xq.live.vo.in.ActUserInVo;
import com.xq.live.vo.out.ActUserOut;

/**
 * 选手活动Service
 * Created by lipeng on 2018/4/27.
 */
public interface ActUserService {

    /**
     * 查询选手是否报名
     * @param inVo
     * @return
     */
    ActUserOut findByInVo(ActUserInVo inVo);

    Long add(ActUserInVo inVo);
}
