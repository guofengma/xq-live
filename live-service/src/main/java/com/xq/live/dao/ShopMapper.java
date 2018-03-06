package com.xq.live.dao;

import com.xq.live.model.Shop;
import com.xq.live.vo.in.ShopInVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Shop record);

    int insertSelective(Shop record);

    Shop selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Shop record);

    int updateByPrimaryKey(Shop record);

    /**
     * 分页查询列表
     * @param inVo
     * @return
     */
    List<Shop> list(ShopInVo inVo);

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

}