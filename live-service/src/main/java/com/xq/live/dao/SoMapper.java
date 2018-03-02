package com.xq.live.dao;

import com.xq.live.model.So;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.out.SoOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SoInVo record);

    So selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(So record);

    int updateByPrimaryKey(So record);

    int listTotal(SoInVo inVo);

    List<SoOut> list(SoInVo inVo);

    SoOut selectByPk(Long id);

    int paid(SoInVo inVo);

    int hexiao(SoInVo inVo);

    int cancel(SoInVo inVo);

}