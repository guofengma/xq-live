package com.xq.live.dao;

import com.xq.live.model.ActGroup;
import com.xq.live.vo.in.ActGroupInVo;
import com.xq.live.vo.out.ActGroupOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActGroup record);

    int insertSelective(ActGroup record);

    ActGroup selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActGroup record);

    int updateByPrimaryKey(ActGroup record);

    //根据活动ID查询所有活动列表信息
    List<ActGroupOut> selectOutAll(ActGroupInVo InVo);

    //根据主键ID或者活动ID查询指定信息
    List<ActGroupOut> selectByOutID(ActGroupInVo InVo);

    //批量插入活动数据
    int insertList(List<ActGroup> list);

    //根据主键ID和活动ID删除指定信息
    int updateByID(ActGroup actGroup);

}