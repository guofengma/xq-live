package com.xq.live.dao;

import com.xq.live.model.DictCity;
import com.xq.live.vo.in.DictInVo;

import java.util.List;

public interface DictCityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DictCity record);

    int insertSelective(DictCity record);

    DictCity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictCity record);

    int updateByPrimaryKey(DictCity record);

    List<DictCity> list(DictInVo inVo);
}
