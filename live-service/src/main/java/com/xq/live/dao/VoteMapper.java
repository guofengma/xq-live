package com.xq.live.dao;

import com.xq.live.model.Vote;

public interface VoteMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Vote record);

    int insertSelective(Vote record);

    Vote selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Vote record);

    int updateByPrimaryKey(Vote record);
}