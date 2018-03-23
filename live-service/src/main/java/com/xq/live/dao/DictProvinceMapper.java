package com.xq.live.dao;

import com.xq.live.model.DictProvince;

import java.util.List;

public interface DictProvinceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DictProvince record);

    int insertSelective(DictProvince record);

    DictProvince selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictProvince record);

    int updateByPrimaryKey(DictProvince record);

    List<DictProvince> list();
}
