package com.xq.live.dao;

import com.xq.live.model.Zan;
import com.xq.live.model.ZanKey;

public interface ZanMapper {
    int deleteByPrimaryKey(ZanKey key);

    int insert(Zan record);

    int insertSelective(Zan record);

    Zan selectByPrimaryKey(ZanKey key);

    int updateByPrimaryKeySelective(Zan record);

    int updateByPrimaryKey(Zan record);
}