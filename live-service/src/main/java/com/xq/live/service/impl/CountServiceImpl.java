package com.xq.live.service.impl;

import com.xq.live.common.RedisCache;
import com.xq.live.dao.TopicMapper;
import com.xq.live.model.Topic;
import com.xq.live.service.CountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @package: com.xq.live.service.impl
 * @description: 各种统计服务
 * @author: zhangpeng32
 * @date: 2018/3/19 20:23
 * @version: 1.0
 */
@Service
public class CountServiceImpl implements CountService {

    @Autowired
    RedisCache<String, Integer> redisCache;

    @Autowired
    TopicMapper topicMapper;

    private static Long viewArticleTime = System.currentTimeMillis();

    @Override
    public Integer topicHits(Long topicId) {
        Topic topic = topicMapper.selectByPrimaryKey(topicId);
        String key = "topicHits_" + topicId.toString();
        Integer hits = redisCache.get(key);
        if (hits == null) {
            hits = topic.getHitNum() == null ? 0 : topic.getHitNum();
        }
        hits++;
        redisCache.set(key, hits, 1l, TimeUnit.DAYS);
        Long time = System.currentTimeMillis();
        if (time > (viewArticleTime + 300000)) {
            viewArticleTime = time;
            topic.setHitNum(hits);
            topicMapper.updateByPrimaryKeySelective(topic);
            redisCache.set(key, null);
        }
        return hits;
    }
}
