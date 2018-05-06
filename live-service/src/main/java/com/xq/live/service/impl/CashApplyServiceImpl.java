package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.CashApplyMapper;
import com.xq.live.model.CashApply;
import com.xq.live.service.AccountService;
import com.xq.live.service.CashApplyService;
import com.xq.live.vo.in.CashApplyInVo;
import com.xq.live.vo.in.UserAccountInVo;
import com.xq.live.vo.out.ActInfoOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * com.xq.live.service.impl
 *
 * @author zhangpeng32
 * Created on 2018/5/6 下午5:10
 * @Description:
 */
@Service
public class CashApplyServiceImpl implements CashApplyService {

    @Autowired
    private CashApplyMapper cashApplyMapper;

    @Autowired
    private AccountService accountService;

    @Override
    public Long create(CashApply cashApply) {
        //新增数据到提现申请表
        int ret = cashApplyMapper.insert(cashApply);
        if(ret > 0){
            //修改账户信息
            this.updateAccount(cashApply);
            return cashApply.getId();
        }
        return null;
    }

    @Override
    public Pager<CashApply> list(CashApplyInVo inVo) {
        Pager<CashApply> result = new Pager<CashApply>();
        int listTotal = cashApplyMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<CashApply> list = cashApplyMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    /**
     * 更新账户信息——提现
     * @param cashApply
     * @return
     */
    private int updateAccount(CashApply cashApply){
        UserAccountInVo accountInVo = new UserAccountInVo();
        accountInVo.setUserId(cashApply.getUserId());
        accountInVo.setOccurAmount(cashApply.getCashAmount());
        return accountService.payout(accountInVo);
    }
}
