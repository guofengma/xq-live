package com.xq.live.service.impl;

import com.xq.live.common.RedisCache;
import com.xq.live.config.ActSkuConfig;
import com.xq.live.dao.ActGroupMapper;
import com.xq.live.dao.ActShopMapper;
import com.xq.live.dao.SoWriteOffMapper;
import com.xq.live.model.ActGroup;
import com.xq.live.model.ActShop;
import com.xq.live.service.CouponForActShopService;
import com.xq.live.vo.out.ActGroupOut;
import com.xq.live.vo.out.ActShopOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 门给新活动建立的，用来参与活动的商家核销完券码后投票数目加5票
 * 注:现在准备专门做定时任务的serviceImpl
 * Created by lipeng on 2018/5/4.
 */
@Service
public class CouponForActShopServiceImpl implements CouponForActShopService{
    @Autowired
    private ActShopMapper actShopMapper;

    @Autowired
    private SoWriteOffMapper soWriteOffMapper;

    @Autowired
    private ActGroupMapper actGroupMapper;

    @Autowired
    private ActSkuConfig actSkuConfig;

    @Autowired
    private RedisCache redisCache;


    @Override
    /*@Scheduled(cron = "0 0 2 * * ?")*/
    public void addVoteForActShop() {
        List<ActShopOut> list = actShopMapper.listForSoWrite();
        for (ActShopOut actShopOut : list) {
            Integer count = soWriteOffMapper.selectActShopCount(actShopOut.getShopId());
            Integer i = count * 5;
            if(i!=0) {
                ActShop actShop = new ActShop();
                actShop.setId(actShopOut.getId());
                actShop.setVoteNum(actShopOut.getVoteNum() + i);//核销之后加5票
                actShopMapper.updateByPrimaryKeySelective(actShop);
            }
        }

        List<ActGroupOut> actGroupOuts = actGroupMapper.listForSoWrite();
        if(list!=null||list.size()>0){
            for (ActGroupOut actGroupOut : actGroupOuts) {
                Integer count = soWriteOffMapper.selectActShopCount(actGroupOut.getShopId());
                Integer i = count * 5;
                if(i!=0) {
                    ActGroup actGroup = new ActGroup();
                    actGroup.setId(actGroupOut.getId());
                    actGroup.setGroupVoteNum(actGroupOut.getGroupVoteNum()+i);//核销之后加5票
                    actGroupMapper.updateByPrimaryKeySelective(actGroup);
                }
            }
        }

        System.out.println("now time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    @Override
    /*@Scheduled(fixedRate = 5000)*/
    /*@Scheduled(cron = "0 0 2 * * ?")*/
    public void deleteActVoteNums() {
        //String key = "actVoteNums_" + "*";
        String keyUser = "actVoteNumsUser_" + actSkuConfig.getActId() + "_" +"*";
        String keySku  = "actVoteNumsSku_" + actSkuConfig.getActId() + "_" +"*";
        redisCache.delAll(keyUser);
        redisCache.delAll(keySku);
        System.out.println("now time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
