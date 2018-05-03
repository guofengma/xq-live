package com.xq.live.dao;

import com.xq.live.model.ActShop;
import com.xq.live.vo.in.ActShopInVo;
import com.xq.live.vo.out.ActShopByShopIdOut;
import com.xq.live.vo.out.ActShopOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActShopMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ActShop record);

    int insertSelective(ActShop record);

    ActShop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActShop record);

    int updateByPrimaryKey(ActShop record);

    int countByActId(Long actId);

    List<ActShopOut> list(ActShopInVo inVo);

    List<ActShopOut> listForNewAct(ActShopInVo inVo);

    List<ActShopByShopIdOut> listForActByShopId(ActShopInVo inVo);

    int listTotal(ActShopInVo inVo);

    ActShop findByInVo(ActShopInVo inVo);
}
