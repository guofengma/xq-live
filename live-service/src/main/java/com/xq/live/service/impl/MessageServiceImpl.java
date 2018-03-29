package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.MessageMapper;
import com.xq.live.model.Favorites;
import com.xq.live.model.Message;
import com.xq.live.model.MessageText;
import com.xq.live.model.Shop;
import com.xq.live.service.MessageService;
import com.xq.live.vo.in.MessageInVo;
import com.xq.live.vo.out.MessageOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @package: com.xq.live.service.impl
 * @description: 站内信service实现类
 * @author: zhangpeng32
 * @date: 2018/3/28 18:49
 * @version: 1.0
 */
@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public Long add(Message msg) {
        int ret = messageMapper.insert(msg);
        if(ret < 1){
            return null;
        }
        return msg.getId();
    }

    @Override
    public Integer update(Message msg) {
        return messageMapper.updateByPrimaryKeySelective(msg);
    }

    @Override
    public Pager<MessageOut> list(MessageInVo inVo) {
        Pager<MessageOut> result = new Pager<MessageOut>();
        int total = messageMapper.listTotal(inVo);
        if(total > 0){
            List<MessageOut> list = messageMapper.list(inVo);
            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<MessageOut> myMsgList(MessageInVo inVo) {
        return messageMapper.list(inVo);
    }

    @Override
    @Transactional
    public MessageOut detail(MessageInVo inVo) {
        //1、查询消息记录是否存在，存在则查询，不存在则需要向message表新增一条记录
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("receiverId", inVo.getReceiverId());
        paramsMap.put("messageTextId", inVo.getMessageTextId());
        Message message = messageMapper.selectByRecId(paramsMap);
        if(message == null){
            //1、向message表插入记录
            message = new Message();
            message.setReceiverId(inVo.getReceiverId());
            message.setMessageTextId(inVo.getMessageTextId());
            message.setStatus(Message.MESSAGE_STATUS_READ); //标记为已读
            message.setIsDeleted(0);    //设置未删除
            if(inVo.getType() != null){
                if(inVo.getType() == MessageText.MESSAGE_TEXT_TYPE_SYSTEM){
                    message.setSenderId(Message.MESSAGE_SEND_ID_SYSTEM);    //系统消息
                }else if(inVo.getType() == MessageText.MESSAGE_TEXT_TYPE_COMMON){
                    message.setSenderId(Message.MESSAGE_SEND_ID_COMMON);    //公共消息
                }else{
                    message.setSenderId(Message.MESSAGE_SEND_ID_OTHER);    //其他???
                }
            }
            messageMapper.insert(message);
        }

        //2、查询明细
        MessageOut res =  messageMapper.detail(inVo);
        return res;
    }

    @Override
    public Integer delete(MessageInVo inVo) {
        Integer result = 0;

        //1、查询消息记录是否存在，存在则查询，不存在则需要向message表新增一条记录
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("receiverId", inVo.getReceiverId());
        paramsMap.put("messageTextId", inVo.getMessageTextId());
        Message message = messageMapper.selectByRecId(paramsMap);
        if(message == null){
            //1、向message表插入记录
            message = new Message();
            message.setReceiverId(inVo.getReceiverId());
            message.setMessageTextId(inVo.getMessageTextId());
            message.setIsDeleted(1);    //设置已删除
            message.setStatus(Message.MESSAGE_STATUS_UNREAD); //标记为未读
            if(inVo.getType() != null){
                if(inVo.getType() == MessageText.MESSAGE_TEXT_TYPE_SYSTEM){
                    message.setSenderId(Message.MESSAGE_SEND_ID_SYSTEM);    //系统消息
                }else if(inVo.getType() == MessageText.MESSAGE_TEXT_TYPE_COMMON){
                    message.setSenderId(Message.MESSAGE_SEND_ID_COMMON);    //公共消息
                }else{
                    message.setSenderId(Message.MESSAGE_SEND_ID_OTHER);    //其他???
                }
            }
            result = messageMapper.insert(message);
        }else{  //如果记录已经存在，则更新删除状态即可
            message.setIsDeleted(1);    //设置已删除
            message.setStatus(Message.MESSAGE_STATUS_READ);//标记为已读
            result = messageMapper.updateByPrimaryKey(message);
        }
        return result;
    }

}
