package com.xq.live.service;

import com.xq.live.common.Pager;
import com.xq.live.model.CashApply;
import com.xq.live.vo.in.CashApplyInVo;

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
}
