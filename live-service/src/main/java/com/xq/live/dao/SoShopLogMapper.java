package com.xq.live.dao;

import com.xq.live.model.SoShopLog;
import org.springframework.stereotype.Repository;

@Repository
public interface SoShopLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SoShopLog record);

    int insertSelective(SoShopLog record);

    SoShopLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SoShopLog record);

    int updateByPrimaryKey(SoShopLog record);
}
