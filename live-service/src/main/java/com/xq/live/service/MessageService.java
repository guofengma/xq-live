package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.Message;
import com.xq.live.vo.in.MessageInVo;
import com.xq.live.vo.out.MessageOut;

import java.util.List;

/**
 * @package: com.xq.live.service
 * @description: 站内信service接口
 * @author: zhangpeng32
 * @date: 2018/3/28 18:47
 * @version: 1.0
 */
public interface MessageService {
    /**
     * 新增
     * @param msg
     * @return
     */
    Long add(Message msg);

    /**
     * 根据id更新记录
     * @param msg
     * @return
     */
    Integer update(Message msg);

    /**
     * 分页查询消息列表
     * @param inVo
     * @return
     */
    Pager<MessageOut> list(MessageInVo inVo);

    /**
     * 查询列表
     * @param inVo
     * @return
     */
    List<MessageOut> myMsgList(MessageInVo inVo);

    /**
     * 查询消息详情
     * @param inVo
     * @return
     */
    MessageOut detail(MessageInVo inVo);

    /**
     * 删除一条站内信
     * @param inVo
     * @return
     */
    Integer delete(MessageInVo inVo);
}
