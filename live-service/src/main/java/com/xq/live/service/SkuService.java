package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.Sku;
import com.xq.live.vo.in.SkuInVo;
import com.xq.live.vo.out.SkuForTscOut;
import com.xq.live.vo.out.SkuOut;

import java.util.List;

/**
 * SKU服务service
 *
 * @author zhangpeng32
 * @date 2018-02-09 10:32
 * @copyright:hbxq
 **/
public interface SkuService {

    /**
     * 查一条
     * @param id
     * @return
     */
    Sku get(Long id);

    /**
     * 分页查询
     * @param inVo
     * @return
     */
    Pager<SkuOut> list(SkuInVo inVo);

    /**
     * 查询列表
     * @param inVo
     * @return
     */
    List<SkuOut> top(SkuInVo inVo);

    /**
     * 新增sku
     * @param sku
     * @return
     */
    Long add(Sku sku);

    /**
     * 根据id查询，返回结果包含促销信息
     * @param id
     * @return
     */
    SkuOut selectById(Long id);

    /**
     * 分页查询特色菜
     * @param inVo
     * @return
     */
    Pager<SkuForTscOut> queryTscList(SkuInVo inVo);

    /**
     * 判断是否是新用户首次下单
     * @param userId
     * @return
     */
    Integer isNewUser(Long userId);
}
