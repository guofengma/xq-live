package com.xq.live.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @package: com.xq.live.common
 * @description: redis缓存工具类封装
 * @author: zhangpeng32
 * @date: 2018/3/17 16:04
 * @version: 1.0
 */
@Service
public class RedisCache{

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 将value对象写入缓存
     * @param key
     * @param value
     */
    public void set(String key,Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取缓存<br>
     * 注：该方法暂不支持Character数据类型
     * @param key   key
     * @param clazz 类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        return (T)redisTemplate.boundValueOps(key).get();
    }



    /**
     * 模糊删除满足条件的redis数据
     * @param key
     * @return
     */
    public  void  delAll(String key){
        Set keys = redisTemplate.keys(key);
        redisTemplate.delete(keys);
    }




    /**
     * 将value对象写入缓存
     * @param key
     * @param value
     * @param timeout 失效时间(秒)
     */
    public void set(String key,Object value,final long timeout, final TimeUnit unit){
        if(value.getClass().equals(String.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Integer.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Double.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Float.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Short.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Long.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else if(value.getClass().equals(Boolean.class)){
            stringRedisTemplate.opsForValue().set(key, value.toString());
        }else{
            redisTemplate.opsForValue().set(key, value);
        }
        if(timeout > 0){
            redisTemplate.expire(key, timeout, unit);
        }
    }

    /**
     * @Description: 是否缓存
     * @Author: zhangpeng32
     * @Date: 2018/3/17 17:32
     * @Version: 1.0.0
     */
    public boolean hasKey(String key) {
        boolean result = redisTemplate.hasKey(key);
        return result;
    }

    /**
     * 删除缓存<br>
     * 根据key精确匹配删除
     * @param key
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * @Description: 指定缓存的失效时间
     * @param: key
     * @Return:
     * @Author: zhangpeng32
     * @Date: 2018/3/20 19:50
     * @Version: 1.0.0
     */
    public void expire(String key, final long timeout, final TimeUnit unit) {
        if(timeout > 0){
            redisTemplate.expire(key, timeout, unit);
        }
    }
}
