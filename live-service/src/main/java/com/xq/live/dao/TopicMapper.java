package com.xq.live.dao;

import com.xq.live.model.Topic;
import com.xq.live.vo.in.TopicInVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicMapper{

    int deleteByPrimaryKey(Long id);

    int insert(Topic record);

    int insertSelective(Topic record);

    Topic selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Topic record);

    int updateByPrimaryKey(Topic record);

    List<Topic> list(TopicInVo inVo);

    int listTotal(TopicInVo inVo);
}