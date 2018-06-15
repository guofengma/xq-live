package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.ActSkuMapper;
import com.xq.live.service.ActSkuService;
import com.xq.live.vo.in.ActSkuInVo;
import com.xq.live.vo.out.ActSkuOut;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 推荐菜活动Service实现类
 * Created by lipeng on 2018/6/13.
 */
@Service
public class ActSkuServiceImpl implements ActSkuService{
    @Autowired
    private ActSkuMapper actSkuMapper;

    private static Logger logger = Logger.getLogger(ActInfoServiceImpl.class);

    @Override
    public Pager<ActSkuOut> listForNewAct(ActSkuInVo inVo) {
        Pager<ActSkuOut> result = new Pager<ActSkuOut>();
        int listTotal = actSkuMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if(listTotal>0){
            List<ActSkuOut> list = actSkuMapper.listForNewAct(inVo);
            /*for (ActSkuOut actSkuOut : list) {

            }*/
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public ActSkuOut findByInVo(ActSkuInVo inVo) {

        ActSkuOut byInVo = null;
        try {
            byInVo = actSkuMapper.findByInVo(inVo);//主要是为了防止脏数据，其实也可以直接查询单个
            if (byInVo == null) {
                return null;
            }
            return byInVo;
        }catch (Exception e){
            logger.error("查询活动推荐菜异常TooManyResultException ：" + e.getMessage());
            return new ActSkuOut();
        }
    }

    @Override
    public Integer update(ActSkuInVo inVo) {
        int i = actSkuMapper.updateByPrimaryKeySelective(inVo);
        return i;
    }
}
