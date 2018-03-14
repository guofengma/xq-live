package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.*;
import com.xq.live.model.Attachment;
import com.xq.live.model.Topic;
import com.xq.live.model.User;
import com.xq.live.service.TopicService;
import com.xq.live.vo.in.CommentInVo;
import com.xq.live.vo.in.TopicInVo;
import com.xq.live.vo.in.ZanInVo;
import com.xq.live.vo.out.TopicForZanOut;
import com.xq.live.vo.out.TopicOut;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ZanMapper zanMapper;


    @Override
    public Topic selectOne(Long id) {
        Topic topic = topicMapper.selectByPrimaryKey(id);
        if(topic != null){
            topic = getPicUrls(topic);
        }
        return topic;
    }

    @Override
    public TopicForZanOut selectByZan(TopicInVo inVo) {
        TopicForZanOut result = topicMapper.selectByZan(inVo);
        return result;
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
    public Pager<TopicOut> list(TopicInVo inVo) {
        Pager<TopicOut> pager = new Pager<TopicOut>();
        int total = topicMapper.listTotal(inVo);

        if(total > 0){
            List<Topic> list = topicMapper.list(inVo);
            List<TopicOut> listForOut = new ArrayList<TopicOut>();
            for(Topic t : list){
                t = getPicUrls(t);
                User user = userMapper.selectByPrimaryKey(t.getUserId());
                CommentInVo commentInVo = new CommentInVo();
                commentInVo.setRefId(t.getId());
                commentInVo.setCmtType(2);
                int listTotal = commentMapper.listTotal(commentInVo);
                ZanInVo zanInVo = new ZanInVo();
                zanInVo.setRefId(t.getId());
                zanInVo.setType(2);
                int zan = zanMapper.total(zanInVo);
                TopicOut topicOut = new TopicOut();
                BeanUtils.copyProperties(t,topicOut);
                topicOut.setIconUrl(user.getIconUrl());
                topicOut.setCommentNum(listTotal);
                topicOut.setZan(zan);
                listForOut.add(topicOut);
            }
            pager.setList(listForOut);
        }
        pager.setTotal(total);  //总记录数
        pager.setPage(inVo.getPage());     //当前页
        return pager;
    }

    @Override
    public Pager<TopicOut> myList(TopicInVo inVo) {
        Pager<TopicOut> pager = new Pager<TopicOut>();
        int total = topicMapper.myListTotal(inVo);

        if(total > 0){
            List<Topic> list = topicMapper.myList(inVo);
            List<TopicOut> listForOut = new ArrayList<TopicOut>();
            for(Topic t : list){
                t = getPicUrls(t);
                User user = userMapper.selectByPrimaryKey(t.getUserId());
                CommentInVo commentInVo = new CommentInVo();
                commentInVo.setRefId(t.getId());
                commentInVo.setCmtType(2);
                int listTotal = commentMapper.listTotal(commentInVo);
                ZanInVo zanInVo = new ZanInVo();
                zanInVo.setRefId(t.getId());
                zanInVo.setType(2);
                int zan = zanMapper.total(zanInVo);
                TopicOut topicOut = new TopicOut();
                BeanUtils.copyProperties(t,topicOut);
                topicOut.setIconUrl(user.getIconUrl());
                topicOut.setCommentNum(listTotal);
                topicOut.setZan(zan);
                listForOut.add(topicOut);
            }
            pager.setList(listForOut);
        }
        pager.setTotal(total);  //总记录数
        pager.setPage(inVo.getPage());     //当前页
        return pager;
    }

    @Override
    public List<TopicOut> top(TopicInVo inVo) {
        List<Topic> list = topicMapper.list(inVo);
        List<TopicOut> listForOut = new ArrayList<TopicOut>();
        for(Topic t : list){
            t = getPicUrls(t);
            User user = userMapper.selectByPrimaryKey(t.getUserId());
            CommentInVo commentInVo = new CommentInVo();
            commentInVo.setRefId(t.getId());
            commentInVo.setCmtType(2);
            int listTotal = commentMapper.listTotal(commentInVo);
            TopicOut topicOut = new TopicOut();
            BeanUtils.copyProperties(t,topicOut);
            topicOut.setIconUrl(user.getIconUrl());
            topicOut.setCommentNum(listTotal);
            listForOut.add(topicOut);
        }
        return listForOut;
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
