package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.CashApply;
import com.xq.live.vo.in.CashApplyInVo;

import java.util.List;

/**
 * com.xq.live.service
 *  提现业务service
 * @author zhangpeng32
 * Created on 2018/5/6 下午4:54
 * @Description:
 */
public interface CashApplyService {
    /**
     * 创建提现申请
     * @param cashApply
     * @return
     */
    public Long create(CashApply cashApply);

    /**
     * 提现记录列表查询
     * @param inVo
     * @return
     */
    public Pager<CashApply> list(CashApplyInVo inVo);

    /**
     * 通过shopId查询商家的提现
     * @param shopId
     * @return
     */
    List<CashApply> selectByUserId(Long shopId);

    /**
     * 通过id查询单个提现详情
     * @param id
     * @return
     */
    CashApply get(Long id);
}
