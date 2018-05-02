package com.xq.live.dao;

import com.xq.live.model.ActGroup;

public interface ActGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActGroup record);

    int insertSelective(ActGroup record);

    ActGroup selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActGroup record);

    int updateByPrimaryKey(ActGroup record);
}