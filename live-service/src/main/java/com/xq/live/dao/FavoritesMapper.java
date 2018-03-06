package com.xq.live.dao;

import com.xq.live.model.Favorites;
import com.xq.live.vo.in.FavoritesInVo;

import java.util.List;

public interface FavoritesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Favorites record);

    int insertSelective(Favorites record);

    Favorites selectByPrimaryKey(Long id);

    Favorites selectByUserIdAndShopId(Favorites favorites);

    int updateByPrimaryKeySelective(Favorites record);

    int updateByPrimaryKey(Favorites record);

    List<Favorites> list(FavoritesInVo inVo);

    int listTotal(FavoritesInVo inVo);
}
