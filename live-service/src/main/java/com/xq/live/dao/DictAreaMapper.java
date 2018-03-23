package com.xq.live.dao;

import com.xq.live.model.DictArea;
import com.xq.live.vo.in.DictInVo;

import java.util.List;

public interface DictAreaMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DictArea record);

    int insertSelective(DictArea record);

    DictArea selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DictArea record);

    int updateByPrimaryKey(DictArea record);

    List<DictArea> list(DictInVo inVo);
}
