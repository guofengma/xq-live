package com.xq.live.service;

/**
 * 专门给新活动建立的，用来参与活动的商家核销完券码后投票数目加5票
 * Created by lipeng on 2018/5/4.
 */
public interface CouponForActShopService {

    /**
     * 给参与活动的商家每次核销之后加5票
     */
    void addVoteForActShop();
}
