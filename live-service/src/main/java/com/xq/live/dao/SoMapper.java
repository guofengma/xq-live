package com.xq.live.dao;

import com.xq.live.model.So;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.out.SoForOrderOut;
import com.xq.live.vo.out.SoOut;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SoInVo record);

    So selectByPrimaryKey(Long id);

    int selectByUserIdTotal(Long userId);

    int updateByPrimaryKeySelective(So record);

    int updateByPrimaryKey(So record);

    int listTotal(SoInVo inVo);

    int listForShopTotal(SoInVo inVo);

    List<SoOut> list(SoInVo inVo);

    List<SoOut> listForShop(SoInVo inVo);

    List<SoOut> listNoPage(SoInVo inVo);

    List<SoOut> listForShopNoPage(SoInVo inVo);

    SoOut selectByPk(Long id);

    SoOut selectByPkForShop(Long id);

    SoForOrderOut selectByPkForOrder(Long id);

    int paid(SoInVo inVo);

    int hexiao(SoInVo inVo);

    int cancel(SoInVo inVo);

    /**
     * 判断用户是否已经领取过活动卷
     * @param inVo
     * @return
     */
    Integer hadBeenGiven(SoInVo inVo);

    /**
     * 根据订单ID获取userId
     * @param inVo
     * @return
     */
    Long getUserIDBySoId(SoInVo inVo);

    BigDecimal totalAmount(SoInVo inVo);
}
