package com.xq.live.dao;

import com.xq.live.model.ActSku;
import com.xq.live.vo.in.ActSkuInVo;
import com.xq.live.vo.out.ActSkuOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActSkuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActSku record);

    int insertSelective(ActSku record);

    ActSku selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActSku record);

    int updateByPrimaryKey(ActSku record);

    List<ActSkuOut> listForNewAct(ActSkuInVo inVo);

    int listTotal(ActSkuInVo inVo);

    ActSkuOut findByInVo(ActSkuInVo inVo);
}
