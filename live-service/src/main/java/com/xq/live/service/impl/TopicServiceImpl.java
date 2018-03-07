package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.AttachmentMapper;
import com.xq.live.dao.TopicMapper;
import com.xq.live.model.Attachment;
import com.xq.live.model.Topic;
import com.xq.live.service.TopicService;
import com.xq.live.vo.in.TopicInVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private AttachmentMapper attachmentMapper;


    @Override
    public Topic selectOne(Long id) {
        Topic topic = topicMapper.selectByPrimaryKey(id);
        if(topic != null){
            topic = getPicUrls(topic);
        }
        return topic;
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
            for(Topic t : list){
                t = getPicUrls(t);
            }
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

    /**
     *
     * @return
     */
    public Topic getPicUrls(Topic topic){
        if(StringUtils.isNotEmpty(topic.getPicIds())){
            String picIds = topic.getPicIds();
            String[] temPicIds = picIds.split(",");
            if(temPicIds != null && temPicIds.length > 0){
                List<Long> ids = new ArrayList<Long>();
                for(String picId : temPicIds){
                    ids.add(Long.valueOf(picId));
                }
                if(ids.size() > 0){
                    Map<String, Object> paramsMap = new HashMap<String, Object>();
                    paramsMap.put("ids", ids);
                    List<Attachment> picUrls = attachmentMapper.selectByIds(paramsMap);
                    topic.setPicUrls(picUrls);
                }
            }
        }
        return topic;
    }
}
