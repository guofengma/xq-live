package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.SoWriteOff;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.in.SoWriteOffInVo;
import com.xq.live.vo.out.SoOut;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-21 18:32
 * @copyright:hbxq
 **/
public interface SoWriteOffService {
    /***
     * 分页查询
     * @param inVo
     * @return
     */
    Pager<SoWriteOff> list(SoWriteOffInVo inVo);

    /**
     * 排序查询记录列表
     * @param inVo
     * @return
     */
    List<SoWriteOff> top(SoWriteOffInVo inVo);

    /**
     * 核销
     * @param soWriteOff
     * @return
     */
    Long add(SoWriteOff soWriteOff);

    /**
     * 查询一条记录
     * @param id
     * @return
     */
    SoWriteOff get(Long id);

    /**
     * 输入参数校验
     * @param soWriteOff
     * @return
     */
    int validInput(SoWriteOff soWriteOff);
}
