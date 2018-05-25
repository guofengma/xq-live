package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.So;
import com.xq.live.model.SoDetail;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.out.SoForOrderOut;
import com.xq.live.vo.out.SoOut;

import java.util.List;

/**
 * 订单service
 *
 * @author zhangpeng32
 * @date 2018-02-09 14:25
 * @copyright:hbxq
 **/
public interface SoService {

    /***
     * 分页查询
     * @param inVo
     * @return
     */
    Pager<SoOut> list(SoInVo inVo);

    List<SoOut> findSoList(SoInVo inVo);

    /**
     * 下单
     * @param inVo
     * @return
     */
    Long create(SoInVo inVo);

    /**
     * 生成商家订单
     * @param inVo
     * @return
     */
    Long createForShop(SoInVo inVo);

    /**
     * 查询一条记录
     * @param id
     * @return
     */
    SoOut get(Long id);

    /**
     * 查询我的订单中的订单详情
     * @param id
     * @return
     */
    SoForOrderOut getForOrder(Long id);

    /**
     * 订单支付
     * @param inVo
     * @return
     */
    Integer paid(SoInVo inVo);

    /**
     * 订单支付
     * @param inVo
     * @return
     */
    Integer finished(SoInVo inVo);

    /**
     * 订单取消
     * @param inVo
     * @return
     */
    Integer cancel(SoInVo inVo);

    /**
     * 新用户首单免费
     * @param inVo
     * @return
     */
    Long freeOrder(SoInVo inVo);

    /**
     * 查询用户订单数量(已支付或者已核销)
     * @param userId
     * @return
     */
    Integer selectByUserIdTotal(Long userId);

    /**
     * 领取活动券
     * @param inVo
     * @return
     */
    Long freeOrderForAct(SoInVo inVo);

    /**
     *  领取折扣券
     * @param inVo
     * @return
     */
    /*Long freeOrderForAgio(SoInVo inVo);*/

    /**
     * 判断当天用户是否领取过活动卷
     * @param inVo
     * @return
     */
    Integer hadBeenGiven(SoInVo inVo);


    /**
     * 商家订单支付
     * @param inVo
     * @return
     */
    Integer paidForShop(SoInVo inVo);

    /**
     * 根据订单ID获取userId
     * @param inVo
     * @return
     */
/*    Long getUserIDBySoId(SoInVo inVo);*///(代码不可用！重写)

}
