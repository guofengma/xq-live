package com.xq.live.service.impl;

import com.xq.live.dao.ActUserMapper;
import com.xq.live.dao.UserMapper;
import com.xq.live.model.User;
import com.xq.live.service.ActUserService;
import com.xq.live.vo.in.ActUserInVo;
import com.xq.live.vo.out.ActUserOut;
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

    @Override
    public ActUserOut findByInVo(ActUserInVo inVo) {
        List<ActUserOut> byInVo = actUserMapper.findByInVo(inVo);//主要是为了防止脏数据，其实也可以直接查询单个
        if(byInVo==null||byInVo.size()==0){
            return null;
        }
        return byInVo.get(0);
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
}
