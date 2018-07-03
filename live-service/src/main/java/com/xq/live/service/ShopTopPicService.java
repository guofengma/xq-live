package com.xq.live.service;

import com.xq.live.model.ShopTopPic;
import javafx.util.Pair;

import java.util.List;

/**
 * 商家顶部图片接口
 * Created by lipeng on 2018/7/3.
 */
public interface ShopTopPicService {

    /**
     * 通过shopId查询商家的顶部图片
     * @param shopId
     * @return
     */
    List<Pair<String,String>> selectByShopId(Long shopId);

    /**
     * 增加顶部图片
     * @param shopTopPic
     * @return
     */
    Long add(ShopTopPic shopTopPic);

    /**
     * 物理删除图片
     * @param id
     * @return
     */
    Integer delete(Long id);
}
