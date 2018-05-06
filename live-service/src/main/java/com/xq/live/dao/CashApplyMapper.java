package com.xq.live.dao;

import com.xq.live.model.CashApply;
import com.xq.live.vo.in.CashApplyInVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashApplyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CashApply record);

    int insertSelective(CashApply record);

    CashApply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CashApply record);

    int updateByPrimaryKey(CashApply record);

    int listTotal(CashApplyInVo inVo);

    List<CashApply> list(CashApplyInVo inVo);
}