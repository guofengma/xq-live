package com.xq.live.dao;

import com.xq.live.model.Shop;
import com.xq.live.vo.in.ShopInVo;
import com.xq.live.vo.out.ActShopByShopIdOut;
import com.xq.live.vo.out.ShopOut;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Shop record);

    int insertSelective(Shop record);

    Shop selectByPrimaryKey(Long id);

    Shop selectByShopName(String shopName);

    int updateByPrimaryKeySelective(Shop record);

    int updateByPrimaryKey(Shop record);

    /**
     * 分页查询列表
     * @param inVo
     * @return
     */
    List<ShopOut> list(ShopInVo inVo);

    /**
     * 分页查询收藏商家列表
     * @param inVo
     * @return
     */
    List<ShopOut> getSCForList(List<ShopInVo> inVo);

    /**
     * 查询首页的川湘菜
     * @param inVo
     * @return
     */
    List<ShopOut> listForChuanXiang(ShopInVo inVo);

    /**
     * 查询记录总数
     * @param inVo
     * @return
     */
    int listTotal(ShopInVo inVo);

    /**
     * 更新人气数值
     * @param id
     * @return
     */
    int updatePopNum(Long id);

    /**
     * 根据用户id查询自己的店铺
     * @param userId
     * @return
     */
    Shop getShopByUserId(Long userId);

    ShopOut findShopOutById(Long id);

    /**
     * 根据用户code查询自己的店铺
     * @param code
     * @return
     */
    ShopOut findShopOutByCode(String code);

    /**
     * 查询商家参与的活动列表
     * @param inVo
     * @return
     */
    List<ActShopByShopIdOut> listForActByShopId(ShopInVo inVo);
}
