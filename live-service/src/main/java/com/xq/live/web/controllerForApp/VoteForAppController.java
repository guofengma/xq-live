package com.xq.live.web.controllerForApp;
/**
 * 投票管理类
 *
 * @author zhangpeng32
 * @create 2018-01-17 19:17
 */

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Vote;
import com.xq.live.service.VoteService;
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

/**
 * 投票管理类
 * @author lipeng
 * @create 2018-01-17 19:17
 **/
@RestController
@RequestMapping(value = "/voteForApp")
public class VoteForAppController {
    @Autowired
    private VoteService voteService;

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
     * 新增一条记录
     * @param vote
     * @return
     */
    @RequestMapping(value = "/add",  method = RequestMethod.POST)
    public BaseResp<Long>  add(@Valid Vote vote, BindingResult result){
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id  = voteService.add(vote);
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
