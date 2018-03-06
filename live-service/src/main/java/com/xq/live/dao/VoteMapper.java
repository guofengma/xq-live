package com.xq.live.dao;

import com.xq.live.model.Vote;
import com.xq.live.vo.in.VoteInVo;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Vote record);

    int insertSelective(Vote record);

    Vote selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Vote record);

    int updateByPrimaryKey(Vote record);

    int deleteByInVo(VoteInVo inVo);

    /**
     * 根据活动查询投票数
     * @param actId
     * @return
     */
    int countByActId(Long actId);
}