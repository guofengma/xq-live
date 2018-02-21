package com.xq.live.service;

import com.xq.live.model.Zan;
import com.xq.live.vo.in.ZanInVo;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-08 19:16
 * @copyright:hbxq
 **/
public interface ZanService {
    /**
     * 新增
     * @param zan
     * @return
     */
    public Long add(Zan zan);

    /**
     * 取消赞
     * @param inVo
     * @return
     */
    public int deleteByInVo(ZanInVo inVo);

    /**
     * 取消赞
     * @param id
     * @return
     */
    public int delete(Long id);

}
