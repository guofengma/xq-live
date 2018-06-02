package com.xq.live.service;/**
 * 商家/餐厅service
 *
 * @author zhangpeng32
 * @create 2018-01-17 17:56
 */

import com.xq.live.common.Pager;
import com.xq.live.model.Shop;
import com.xq.live.vo.in.ShopInVo;
import com.xq.live.vo.out.ShopOut;
import org.opencv.phase_unwrapping.Phase_unwrapping;

import java.util.List;
import java.util.Map;

/**
 * 商家/餐厅service
 * @author zhangpeng32
 * @create 2018-01-17 17:56
 **/
public interface ShopService {
    /**
     * 根据id查询商家信息
     * @param id
     * @return
     */
    public ShopOut findShopOutById(Long id);

    /**
     * 根据id查询商家信息
     * @param id
     * @return
     */
    public Shop getShopById(Long id);

    /**
     * 新增商家
     * @param shop
     * @return
     */
    public Long addShop(Shop shop);

    /**
     * 更新一条商家记录
     * @param shop
     * @return
     */
    public  int updateShop(Shop shop);

    /**
     * 根据id删除一条商家记录
     * @param id
     * @return
     */
    public  int deleteShopById(Long id);

    /**
     * 根据查询条件查询商家列表
     * @param inVo
     * @return
     */
    Pager<ShopOut> list(ShopInVo inVo);

    /**
     * 根据查询条件查询商家列表
     * @param inVo
     * @return
     */
    Pager<ShopOut> listForChuangXiang(ShopInVo inVo);

    /**
     * 热门商家
     * @param inVo
     * @return
     */
    List<ShopOut> top(ShopInVo inVo);

    /**
     * 查询首页不同分类热门商家
     * @param inVo
     * @return
     */
    Map<String,List<ShopOut>> listForHomePage(ShopInVo inVo);

    /**
     * 根据id查询商家详细
     * @param inVo
     * @return
     */
    public Shop detail(ShopInVo inVo);

    /**
     * 根据用户信息查询自己的店铺
     * @param userId
     * @return
     */
    public Shop getShopByUserId(Long userId);

    /**
     * 根据商家编码信息查询自己的店铺
     * @param code
     * @return
     */
    public ShopOut getShopByCode(String code);

}
