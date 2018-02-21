package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.TopicMapper;
import com.xq.live.model.Topic;
import com.xq.live.service.TopicService;
import com.xq.live.vo.in.TopicInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-08 14:01
 * @copyright:hbxq
 **/
@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicMapper topicMapper;


    @Override
    public Topic selectOne(Long id) {
        return topicMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long add(Topic topic) {
        int r = topicMapper.insert(topic);
        if(r == 0){
            return null;
        }
        return topic.getId();
    }

    @Override
    public int update(Topic topic) {
        topic.setUpdateTime(new Date());
        return topicMapper.updateByPrimaryKeySelective(topic);
    }

    @Override
    public int delete(Long id) {
        return topicMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Pager<Topic> list(TopicInVo inVo) {
        Pager<Topic> pager = new Pager<Topic>();
        int total = topicMapper.listTotal(inVo);

        if(total > 0){
            List<Topic> list = topicMapper.list(inVo);
            pager.setList(list);
        }
        pager.setTotal(total);  //总记录数
        pager.setPage(inVo.getPage());     //当前页
        return pager;
    }

    @Override
    public List<Topic> top(TopicInVo inVo) {
        return topicMapper.list(inVo);
    }
}
