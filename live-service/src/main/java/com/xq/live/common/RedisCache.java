package com.xq.live.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @package: com.xq.live.common
 * @description: redis缓存工具类封装
 * @author: zhangpeng32
 * @date: 2018/3/17 16:04
 * @version: 1.0
 */
@Service
public class RedisCache<K, V>{

    @Autowired
    private RedisTemplate redisTemplate;

    public void set(K key, V value){
        ValueOperations<K, V> operations = redisTemplate.opsForValue();
        operations.set(key, value);
    }

    public V get(K key){
        ValueOperations<K, V> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    public void set(K key, V value, final long timeout, final TimeUnit unit){
        ValueOperations<K, V> operations = redisTemplate.opsForValue();
        operations.set(key, value, timeout, unit);
    }

    public boolean hasKey(K key){
        boolean result = redisTemplate.hasKey(key);
        return result;
    }
}
