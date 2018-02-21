package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.Sku;
import com.xq.live.vo.in.SkuInVo;

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
    Pager<Sku> list(SkuInVo inVo);

    /**
     * 查询列表
     * @param inVo
     * @return
     */
    List<Sku> top(SkuInVo inVo);

    /**
     * 新增sku
     * @param sku
     * @return
     */
    Long add(Sku sku);
}
