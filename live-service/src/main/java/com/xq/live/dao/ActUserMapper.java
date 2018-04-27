package com.xq.live.dao;

import com.xq.live.model.ActUser;
import com.xq.live.vo.in.ActUserInVo;
import com.xq.live.vo.out.ActUserOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActUserInVo record);

    int insertSelective(ActUserInVo record);

    ActUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActUserInVo record);

    int updateByPrimaryKey(ActUserInVo record);

    int countByActId(Long actId);

    List<ActUserOut> findByInVo(ActUserInVo inVo);
}
