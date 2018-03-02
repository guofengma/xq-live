package com.xq.live.dao;

import com.xq.live.model.ActInfo;
import com.xq.live.vo.in.ActInfoInVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActInfoMapper{

    int deleteByPrimaryKey(Long id);

    int insert(ActInfo record);

    int insertSelective(ActInfo record);

    ActInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActInfo record);

    int updateByPrimaryKey(ActInfo record);

    int listTotal(ActInfoInVo inVo);

    List<ActInfo> list(ActInfoInVo inVo);
}