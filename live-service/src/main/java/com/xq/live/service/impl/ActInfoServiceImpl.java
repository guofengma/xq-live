package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.*;
import com.xq.live.model.AccessLog;
import com.xq.live.model.ActInfo;
import com.xq.live.model.ActShop;
import com.xq.live.model.User;
import com.xq.live.service.ActInfoService;
import com.xq.live.vo.in.ActInfoInVo;
import com.xq.live.vo.in.ActShopInVo;
import com.xq.live.vo.out.ActInfoOut;
import org.apache.log4j.Logger;
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
public class ActInfoServiceImpl implements ActInfoService {
    @Autowired
    private ActInfoMapper actInfoMapper;

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private ActShopMapper actShopMapper;

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Autowired
    private UserMapper userMapper;

    private static Logger logger = Logger.getLogger(ActInfoServiceImpl.class);

    @Override
    public Long add(ActInfo actInfo) {
        actInfo.setCreateTime(new Date());
        int r = actInfoMapper.insert(actInfo);
        if (r < 1) {
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
    public Pager<ActInfoOut> list(final ActInfoInVo inVo) {
        Pager<ActInfoOut> result = new Pager<ActInfoOut>();
        int listTotal = actInfoMapper.listTotal(inVo);
        result.setTotal(listTotal);
        if (listTotal > 0) {
            List<ActInfoOut> list = actInfoMapper.list(inVo);
            result.setList(list);
        }
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<ActInfoOut> top(ActInfoInVo inVo) {
        return actInfoMapper.list(inVo);
    }

    @Override
    public ActInfoOut detail(final ActInfoInVo inVo) {
        ActInfoOut actInfoOut = actInfoMapper.findActInfoById(inVo.getId());
        if (actInfoOut == null) {
            return null;
        }
        User user = userMapper.selectByPrimaryKey(inVo.getUserId());
        //判断商家是否已经报名了活动
        if(user!=null&&user.getShopId()!=null) {
            ActShopInVo actShopInVo = new ActShopInVo();
            actShopInVo.setActId(inVo.getId());
            actShopInVo.setShopId(user.getShopId());
            try {
                ActShop byInVo = actShopMapper.findByInVo(actShopInVo);
                if (byInVo != null) {
                    actInfoOut.setIsSign(ActShop.ACT_SHOP_IS_SIGN);//已报名
                } else {
                    actInfoOut.setIsSign(ActShop.ACT_SHOP_NO_SIGN);//未报名
                }
            } catch (Exception e) {
                logger.error("查询活动商家异常TooManyResultException ：" + e.getMessage());
            }
        }
        try {
            //新开一个线程记录访问日志
            new Thread(new Runnable() {
                @Override
                public void run() {
                    /**
                     * 1、查询用户是否存在访问记录
                     * 2、记录用户访问日志
                     */
                    AccessLog accessLog = new AccessLog();
                    accessLog.setUserId(inVo.getUserId());
                    accessLog.setUserName(inVo.getUserName());
                    accessLog.setUserIp(inVo.getUserIp());
                    accessLog.setSource(inVo.getSourceType());
                    accessLog.setRefId(inVo.getId());
                    accessLog.setBizType(AccessLog.BIZ_TYPE_ACT_VIEW);
                    int cnt = accessLogMapper.checkRecordExist(accessLog);
                    if (cnt == 0) {
                        int logCnt = accessLogMapper.insert(accessLog);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

/*        ActInfoOut out = new ActInfoOut();
        out.setId(actInfo.getId());
        out.setActName(actInfo.getActName());
        out.setActStatus(actInfo.getActStatus());
        out.setActUrl(actInfo.getActUrl());
        out.setIsDeleted(actInfo.getIsDeleted());
        out.setCreateTime(actInfo.getCreateTime());
        out.setStartTime(actInfo.getStartTime());
        out.setMainPic(actInfo.getMainPic());
        out.setEndTime(actInfo.getEndTime());
        out.setUpdateTime(actInfo.getUpdateTime());
        //查询投票数
        int voteNum = voteMapper.countByActId(actInfo.getId());
        out.setVoteNum(voteNum);

        //查询参与商家数
        int shopNum = actShopMapper.countByActId(actInfo.getId());
        out.setShopNum(shopNum);

        Map<String, Object> logMap = new HashMap<String, Object>();
        logMap.put("bizType", AccessLog.BIZ_TYPE_ACT_VIEW);
        logMap.put("refId", inVo.getId());
        int viewNum = accessLogMapper.countViewNum(logMap);
        out.setViewNum(viewNum);*/

        return actInfoOut;

    }
}
