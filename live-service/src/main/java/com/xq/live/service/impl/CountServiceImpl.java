package com.xq.live.service.impl;

import com.xq.live.common.RedisCache;
import com.xq.live.dao.*;
import com.xq.live.model.*;
import com.xq.live.service.CountService;
import com.xq.live.vo.in.ActShopInVo;
import com.xq.live.vo.in.ActUserInVo;
import com.xq.live.vo.in.VoteInVo;
import com.xq.live.vo.out.ActUserOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @package: com.xq.live.service.impl
 * @description: 各种统计服务
 * @author: zhangpeng32
 * @date: 2018/3/19 20:23
 * @version: 1.0
 */
@Service
public class CountServiceImpl implements CountService {

    @Autowired
    RedisCache redisCache;

    @Autowired
    TopicMapper topicMapper;

    @Autowired
    ShopMapper shopMapper;

    @Autowired
    ActUserMapper actUserMapper;

    @Autowired
    ActShopMapper actShopMapper;

    private static Long viewArticleTime = System.currentTimeMillis();

    @Override
    public Integer topicHits(Long topicId) {
        String key = "topicHits_" + topicId.toString();
        Integer hits = redisCache.get(key, Integer.class);
        if (hits == null) {
            Topic topic = topicMapper.selectByPrimaryKey(topicId);
            hits = topic.getHitNum() == null ? 0 : topic.getHitNum();
        }
        hits++;
        redisCache.set(key, hits, 1l, TimeUnit.DAYS);
        Long time = System.currentTimeMillis();
        if (time > (viewArticleTime + 300000)) {    //5分钟更新一次数据到数据库,viewArticleTime:写数据库的周期
            viewArticleTime = time;
            Topic topic = new Topic();
            topic.setId(topicId);
            topic.setHitNum(hits);
            topicMapper.updateByPrimaryKeySelective(topic);
            redisCache.del(key);
        }
        return hits;
    }

    @Override
    public Integer topicTrans(Long topicId) {
        String key = "topicTrans_" + topicId.toString();
        Integer trans = redisCache.get(key, Integer.class);
        if (trans == null) {
            Topic topic = topicMapper.selectByPrimaryKey(topicId);
            trans = topic.getTransNum() == null ? 0 : topic.getTransNum();
        }
        trans++;
        redisCache.set(key, trans, 1l, TimeUnit.DAYS);
        Long time = System.currentTimeMillis();
        if (time > (viewArticleTime + 300000)) {    //5分钟更新一次数据到数据库,viewArticleTime:写数据库的周期
            viewArticleTime = time;
            Topic topic = new Topic();
            topic.setId(topicId);
            topic.setTransNum(trans);
            topicMapper.updateByPrimaryKeySelective(topic);
            redisCache.del(key);
        }
        /*Topic topic = new Topic();
        topic.setId(topicId);
        topic.setTransNum(trans);
        topicMapper.updateByPrimaryKeySelective(topic);
        redisCache.del(key);*/
        return trans;
    }

    @Override
    public Integer shopPops(Long shopId) {
        String key = "shopPops_" + shopId.toString();
        Integer pops = redisCache.get(key, Integer.class);
        if (pops == null) {
            Shop shop = shopMapper.selectByPrimaryKey(shopId);
            pops = shop.getPopNum() == null ? 0 : shop.getPopNum();
        }
        pops++;
        redisCache.set(key, pops, 1l, TimeUnit.DAYS);
        Long time = System.currentTimeMillis();
        if (time > (viewArticleTime + 300000)) {    //5分钟更新一次数据到数据库,viewArticleTime:写数据库的周期
            viewArticleTime = time;
            Shop shop = new Shop();
            shop.setId(shopId);
            shop.setPopNum(pops);
            shopMapper.updateByPrimaryKeySelective(shop);
            redisCache.del(key);
        }
        return pops;
    }

    @Override
    public Integer voteNums(VoteInVo invo) {
        String key = null;
        Integer nums = 0;
        ActShop actShop = null;
        ActUserOut actUser = null;
        if(invo.getShopId()!=null&&invo.getPlayerUserId()!=null){
             key = "voteNums_" + invo.getActId() + invo.getShopId() + invo.getPlayerUserId();
        }else if(invo.getShopId()!=null&&invo.getPlayerUserId()==null){
            key = "voteNums_" + invo.getActId() + "_shopId_" +invo.getShopId();
            ActShopInVo actShopInVo = new ActShopInVo();
            actShopInVo.setShopId(invo.getShopId());
            actShopInVo.setActId(invo.getActId());
            //可以后期修改一下
            actShop = actShopMapper.findByInVo(actShopInVo);
            nums = actShop.getVoteNum() == null ? 0 : actShop.getVoteNum();
        }else if(invo.getShopId()==null&&invo.getPlayerUserId()!=null){
            key = "voteNums_" + invo.getActId() + "_player_" +invo.getPlayerUserId();
            ActUserInVo actUserInVo = new ActUserInVo();
            actUserInVo.setUserId(invo.getPlayerUserId());
            actUserInVo.setActId(invo.getActId());
            //可以后期修改一下
            actUser = actUserMapper.findByInVo(actUserInVo);
            nums = actUser.getVoteNum() == null ? 0 : actUser.getVoteNum();
        }
        Integer voteNums = redisCache.get(key, Integer.class);
        if (voteNums == null) {
            voteNums = nums;
        }
        if(invo.getType()== Vote.VOTE_ADD){
            voteNums ++;
        }else{
            voteNums --;
        }
        redisCache.set(key, voteNums, 1l, TimeUnit.DAYS);
        Long time = System.currentTimeMillis();
        if (time > (viewArticleTime + 300000)) {    //5分钟更新一次数据到数据库,viewArticleTime:写数据库的周期
            viewArticleTime = time;
            if(actShop != null){
                actShop.setVoteNum(voteNums);
                actShopMapper.updateByPrimaryKeySelective(actShop);
            }
            if(actUser != null){
                ActUserInVo actUserInVo = new ActUserInVo();
                actUserInVo.setId(actUser.getId());
                actUserInVo.setVoteNum(voteNums);
                actUserMapper.updateByPrimaryKeySelective(actUserInVo);
            }
            redisCache.del(key);
        }
        return voteNums;
    }

    @Override
    public Integer voteNumsNow(VoteInVo invo) {

        Integer nums = 0;
        ActShop actShop = null;
        ActUserOut actUser = null;
        if(invo.getShopId()!=null&&invo.getPlayerUserId()!=null){

        }else if(invo.getShopId()!=null&&invo.getPlayerUserId()==null){
            ActShopInVo actShopInVo = new ActShopInVo();
            actShopInVo.setShopId(invo.getShopId());
            actShopInVo.setActId(invo.getActId());
            //可以后期修改一下
            actShop = actShopMapper.findByInVo(actShopInVo);
            nums = actShop.getVoteNum() == null ? 0 : actShop.getVoteNum();
        }else if(invo.getShopId()==null&&invo.getPlayerUserId()!=null){
            ActUserInVo actUserInVo = new ActUserInVo();
            actUserInVo.setUserId(invo.getPlayerUserId());
            actUserInVo.setActId(invo.getActId());
            //可以后期修改一下
            actUser = actUserMapper.findByInVo(actUserInVo);
            nums = actUser.getVoteNum() == null ? 0 : actUser.getVoteNum();
        }
        if(invo.getType()== Vote.VOTE_ADD){
            nums ++;
        }else{
            nums --;
        }

            if(actShop != null&&actUser == null){
                actShop.setVoteNum(nums);
                actShopMapper.updateByPrimaryKeySelective(actShop);
            }
            if(actShop == null&&actUser != null){
                ActUserInVo actUserInVo = new ActUserInVo();
                actUserInVo.setId(actUser.getId());
                actUserInVo.setVoteNum(nums);
                actUserMapper.updateByPrimaryKeySelective(actUserInVo);
            }

        return nums;
    }
}
