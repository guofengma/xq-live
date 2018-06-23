package com.xq.live.web.controller;
/**
 * 投票管理类
 *
 * @author zhangpeng32
 * @create 2018-01-17 19:17
 */

import com.xq.live.common.BaseResp;
import com.xq.live.common.RedisCache;
import com.xq.live.common.ResultStatus;
import com.xq.live.config.ActSkuConfig;
import com.xq.live.model.So;
import com.xq.live.model.SoDetail;
import com.xq.live.model.Vote;
import com.xq.live.service.CountService;
import com.xq.live.service.SoService;
import com.xq.live.service.VoteService;
import com.xq.live.vo.in.SoInVo;
import com.xq.live.vo.in.VoteInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 投票管理类
 * @author zhangpeng32
 * @create 2018-01-17 19:17
 **/
@RestController
@RequestMapping(value = "/vote")
public class VoteController {
    @Autowired
    private VoteService voteService;

    @Autowired
    private SoService soService;

    @Autowired
    private CountService countService;

    @Autowired
    private ActSkuConfig actSkuConfig;

    @Autowired
    private RedisCache redisCache;

    /**
     * 根据ID查询投票信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<Vote> getVoteById(@PathVariable(value = "id")  Long id){
        Vote vote = voteService.get(id);
        return new BaseResp<Vote>(ResultStatus.SUCCESS, vote);
    }

    /**
     * 针对新平台活动，判断是否能够投票(传入shopId的时候，sql语句中shop_id is not null,传入playerUserId的时候，sql语句中player_user_id is not null)
     * actId,beginTime,endTime,userId必填。shopId,playerUserIdId至少填一个
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/canVote", method = RequestMethod.GET)
    public BaseResp<Integer> canVote(VoteInVo inVo){
        if(inVo==null||inVo.getBeginTime()==null||inVo.getEndTime()==null||inVo.getActId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }

        Integer integer = voteService.canVote(inVo);
        if(integer!=null){
            return new BaseResp<Integer>(ResultStatus.error_vote_fail);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS);
    }

    /**
     * 针对新平台活动，判断是否能够领票(只需要传入actId,beginTime,endTime,userId)
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/canGetSku", method = RequestMethod.GET)
    public BaseResp<Integer> canGetSku(VoteInVo inVo){
        if(inVo==null||inVo.getBeginTime()==null||inVo.getEndTime()==null||inVo.getActId()==null||inVo.getUserId()==null){
            return new BaseResp<Integer>(ResultStatus.error_param_empty);
        }

        Integer integer = voteService.canGetSku(inVo);
        if(integer==null){
            return new BaseResp<Integer>(ResultStatus.error_sku_fail);
        }
        SoInVo soInVo=new SoInVo();
        soInVo.setUserId(inVo.getUserId());
        soInVo.setActId(inVo.getActId());
        soInVo.setSkuId(actSkuConfig.getSkuId());
        //判断用户是否领过卷，0没有，1有过
        int i=soService.hadBeenGiven(soInVo);
        if (i!=0){
            return new BaseResp<Integer>(ResultStatus.error_so_had);
        }
        return new BaseResp<Integer>(ResultStatus.SUCCESS, integer);
    }



    /**
     * 新增一条记录
     * @param vote
     * @return
     */
    @RequestMapping(value = "/add",  method = RequestMethod.POST)
    public BaseResp<Long>  add(@Valid VoteInVo vote, BindingResult result){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id  = voteService.add(vote);
        vote.setType(Vote.VOTE_ADD);
        Integer integer = countService.voteNumsNow(vote);
        //如果活动id为37,则每次投票更新投票次数的缓存
        if(vote.getActId().equals(actSkuConfig.getActId())&&vote.getPlayerUserId()!=null){
            String keyUser = "actVoteNumsUser_" + actSkuConfig.getActId() + "_" +vote.getUserId();
            Integer i = redisCache.get(keyUser, Integer.class);
            if(i==null){
                redisCache.set(keyUser,0,1l, TimeUnit.DAYS);
            }else{
                redisCache.set(keyUser,i-1,1l,TimeUnit.DAYS);
            }
        }else if(vote.getActId().equals(actSkuConfig.getActId())&&vote.getSkuId()!=null){
            String keySku  = "actVoteNumsSku_" + actSkuConfig.getActId() + "_" +vote.getUserId();
            Integer i = redisCache.get(keySku, Integer.class);
            if(i==null){
                redisCache.set(keySku,9,1l, TimeUnit.DAYS);
            }else{
                redisCache.set(keySku,i-1,1l,TimeUnit.DAYS);
            }
        }

        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 取消投票
     * @param inVo
     * @param result
     * @return
     */
    @RequestMapping(value = "/delete",  method = RequestMethod.POST)
    public BaseResp<Integer>  delete(@Valid VoteInVo inVo, BindingResult result){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Integer>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        int ret  = voteService.deleteByInVo(inVo);
        inVo.setType(Vote.VOTE_DELETE);
        Integer integer = countService.voteNumsNow(inVo);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, ret);
    }

    /**
     * 删除一条商家记录
     * @param vote
     * @return
     */
    @RequestMapping(value = "/update",  method = RequestMethod.PUT)
    public int  update(Vote vote){
        return 0;
    }
}
