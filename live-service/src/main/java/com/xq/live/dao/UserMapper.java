package com.xq.live.dao;

import com.xq.live.model.User;
import com.xq.live.vo.in.UserInVo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheConfig(cacheNames = "user")
public interface UserMapper{

    int deleteByPrimaryKey(Long id);

    @CachePut(value = "1h")
    int insert(User record);

    int insertSelective(User record);

    @Cacheable(value = "1h")     //缓存时间1小时，key为自动生成
    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User loadUserByUserName(String userName);

    List<User> list(UserInVo inVo);

    int listTotal(UserInVo inVo);

    User findByUserNameAndPwd(UserInVo inVo);

    /**
     * 商家入驻更新企业客户类型，写入商家id
     * @param record
     * @return
     */
    int updateUserType(User record);

    @Cacheable(value = "1h")
    User findByOpenId(String openId);

    Integer updateByOpenId(User user);
}