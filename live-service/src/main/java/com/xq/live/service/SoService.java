package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.So;
import com.xq.live.vo.in.SoInVo;
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
     * 查询一条记录
     * @param id
     * @return
     */
    SoOut get(Long id);

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

}
