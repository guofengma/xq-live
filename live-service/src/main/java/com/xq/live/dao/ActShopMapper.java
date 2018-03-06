package com.xq.live.dao;

import com.xq.live.model.ActShop;
import org.springframework.stereotype.Repository;

@Repository
public interface ActShopMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActShop record);

    int insertSelective(ActShop record);

    ActShop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActShop record);

    int updateByPrimaryKey(ActShop record);

    int countByActId(Long actId);
}