package com.xq.live.dao;

import com.xq.live.model.Sku;
import com.xq.live.vo.in.SkuInVo;
import com.xq.live.vo.out.SkuForTscOut;
import com.xq.live.vo.out.SkuOut;
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

    List<SkuOut> list(SkuInVo inVo);

    int listTotal(SkuInVo inVo);

    SkuOut selectById(Long id);

    List<SkuForTscOut> queryTscList(SkuInVo inVo);

    SkuForTscOut getTscForZan(SkuInVo inVo);

    int tscListTotal(SkuInVo inVo);
}
