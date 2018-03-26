package com.xq.live.service;

/**
 * @package: com.xq.live.service
 * @description: 各种统计service
 * @author: zhangpeng32
 * @date: 2018/3/19 20:20
 * @version: 1.0
 */
public interface CountService {
    /**
     * 主题访问量统计
     * @param topicId
     * @return
     */
    Integer topicHits(Long topicId);

    /**
     * 主题转发量统计
     * @param topicId
     * @return
     */
    Integer topicTrans(Long topicId);

    /**
     * 餐厅的人气
     * @param shopId
     * @return
     */
    Integer shopPops(Long shopId);
}
