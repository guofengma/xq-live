package com.xq.live.dao;

import com.xq.live.model.Sku;
import com.xq.live.vo.in.SkuInVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Sku record);

    int insertSelective(Sku record);

    Sku selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Sku record);

    int updateByPrimaryKey(Sku record);

    List<Sku> list(SkuInVo inVo);

    int listTotal(SkuInVo inVo);
}