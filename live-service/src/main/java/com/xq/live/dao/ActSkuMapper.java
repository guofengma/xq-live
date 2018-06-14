package com.xq.live.dao;

import com.xq.live.model.ActSku;
import org.springframework.stereotype.Repository;

@Repository
public interface ActSkuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActSku record);

    int insertSelective(ActSku record);

    ActSku selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActSku record);

    int updateByPrimaryKey(ActSku record);
}
