package com.xq.live.service;

import com.xq.live.model.Vote;
import com.xq.live.model.Zan;
import com.xq.live.vo.in.VoteInVo;
import com.xq.live.vo.in.ZanInVo;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-21 20:57
 * @copyright:hbxq
 **/
public interface VoteService {

    /**
     * 查一条记录
     * @param id
     * @return
     */
    public Vote get(Long id);

    /**
     * 新增
     * @param vote
     * @return
     */
    public Long add(VoteInVo vote);

    /**
     * 取消投票
     * @param id
     * @return
     */
    public int delete(Long id);


    /**
     * 根据活动id、店铺id和用户id取消投票
     * @param inVo
     * @return
     */
    public int deleteByInVo(VoteInVo inVo);
}
