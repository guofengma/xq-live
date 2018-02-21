package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.ActInfoMapper;
import com.xq.live.model.ActInfo;
import com.xq.live.service.ActInfoService;
import com.xq.live.vo.in.ActInfoInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 活动实现类
 *
 * @author zhangpeng32
 * @create 2018-02-07 17:21
 **/
@Service
public class ActInfoServiceImpl implements ActInfoService{
    @Autowired
    private ActInfoMapper actInfoMapper;
    @Override
    public Long add(ActInfo actInfo) {
        actInfo.setCreateTime(new Date());
        int r = actInfoMapper.insert(actInfo);
        if(r < 1){
            return null;
        }
        return actInfo.getId();
    }

    @Override
    public int update(ActInfo actInfo) {
        return actInfoMapper.updateByPrimaryKeySelective(actInfo);
    }

    @Override
    public int delete(Long id) {
        ActInfo actInfo = new ActInfo();
        actInfo.setId(id);
        actInfo.setIsDeleted(1);
//        return actInfoMapper.deleteByPrimaryKey(id);
        return actInfoMapper.updateByPrimaryKeySelective(actInfo);
    }

    @Override
    public ActInfo selectOne(Long id) {
        return actInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public Pager<ActInfo> list(ActInfoInVo inVo) {
        Pager<ActInfo> result = new Pager<ActInfo>();
        int listTotal = actInfoMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if(listTotal > 0){
            List<ActInfo> list = actInfoMapper.list(inVo);
            result.setList(list);
         }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<ActInfo> top(ActInfoInVo inVo){
        return actInfoMapper.list(inVo);
    }
}
