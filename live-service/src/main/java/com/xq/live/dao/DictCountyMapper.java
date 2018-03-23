package com.xq.live.dao;

import com.xq.live.model.DictCounty;
import com.xq.live.vo.in.DictInVo;

import java.util.List;

public interface DictCountyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DictCounty record);

    int insertSelective(DictCounty record);

    DictCounty selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictCounty record);

    int updateByPrimaryKey(DictCounty record);

    List<DictCounty> list(DictInVo inVo);
}
