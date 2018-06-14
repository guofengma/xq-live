package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.vo.in.ActSkuInVo;
import com.xq.live.vo.out.ActSkuOut;

/**
 * 推荐菜活动Service
 * Created by lipeng on 2018/6/13.
 */
public interface ActSkuService {
    /**
     * 分页查询参与推荐菜列表信息(针对的是新活动，带有开始时间和截止时间，可以多次投票)
     * @param inVo
     * @return
     */
    Pager<ActSkuOut> listForNewAct(ActSkuInVo inVo);
}
