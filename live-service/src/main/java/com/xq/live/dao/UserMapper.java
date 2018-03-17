package com.xq.live.dao;

import com.xq.live.model.User;
import com.xq.live.vo.in.UserInVo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper{

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

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

    User findByOpenId(String openId);
}