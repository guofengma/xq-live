package com.xq.live.dao;

import com.xq.live.model.Message;
import com.xq.live.vo.in.MessageInVo;
import com.xq.live.vo.out.MessageOut;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    List<MessageOut> list(MessageInVo inVo);

    int listTotal(MessageInVo inVo);

    MessageOut detail(MessageInVo inVo);

    Message selectByRecId(Map<String, Object> paramsMap);
}