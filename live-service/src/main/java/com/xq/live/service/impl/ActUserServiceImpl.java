package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.ActUserMapper;
import com.xq.live.dao.ShopMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.ActUser;
import com.xq.live.model.Shop;
import com.xq.live.model.User;
import com.xq.live.service.ActUserService;
import com.xq.live.vo.in.ActUserInVo;
import com.xq.live.vo.out.ActUserOut;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by lipeng on 2018/4/27.
 */
@Service
public class ActUserServiceImpl implements ActUserService{
    @Autowired
    private ActUserMapper actUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ShopMapper shopMapper;

    private static Logger logger = Logger.getLogger(ActInfoServiceImpl.class);

    @Override
    public ActUserOut findByInVo(ActUserInVo inVo) {
        ActUserOut byInVo = null;
        try {
             byInVo = actUserMapper.findByInVo(inVo);//主要是为了防止脏数据，其实也可以直接查询单个
            if (byInVo == null) {
                return null;
            }
            return byInVo;
        }catch (Exception e){
            logger.error("查询活动选手异常TooManyResultException ：" + e.getMessage());
            return new ActUserOut();
        }
    }

    @Override
    public Long add(ActUserInVo inVo) {
        int i = actUserMapper.countByActId(inVo.getActId());
        DecimalFormat mFormat = new DecimalFormat("000");//确定格式，把1转换为001
        String s = mFormat.format(i+1);
        inVo.setUserCode(s);
        int insert = actUserMapper.insert(inVo);//插入数据
        if(insert<1){
            return null;
        }
        User user = userMapper.selectByPrimaryKey(inVo.getUserId());
        user.setAge(inVo.getAge());
        user.setHeight(inVo.getHeight());
        int k = userMapper.updateByPrimaryKeySelective(user);//修改user表的信息
        if (k < 1) {
            return null;
        }
        return inVo.getId();
    }

    @Override
    public Pager<ActUserOut> listForNewAct(ActUserInVo inVo) {
        Pager<ActUserOut> result = new Pager<ActUserOut>();
        int listTotal = actUserMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<ActUserOut> list = actUserMapper.listForNewAct(inVo);
            //如果是查询分组的，把分组的关联信息加入进去
            if(inVo.getType()!=null&&inVo.getType()== ActUser.ACT_USER_GROUP){
                for (ActUserOut actUserOut : list) {
                    Shop shop = shopMapper.selectByPrimaryKey(actUserOut.getShopId());
                    actUserOut.setLogoUrl(shop.getLogoUrl());
                    actUserOut.setShopName(shop.getShopName());
                }
            }
            //Collections.sort(list);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }
}
