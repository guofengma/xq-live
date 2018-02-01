package com.xq.live.web.controller;/**
 * 投票管理类
 *
 * @author zhangpeng32
 * @create 2018-01-17 19:17
 */

import com.xq.live.model.Vote;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 投票管理类
 * @author zhangpeng32
 * @create 2018-01-17 19:17
 **/
@RestController
@RequestMapping(value = "/vote")
public class VoteController {


    /**
     * 根据ID查询评论信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Vote getVoteById(@PathVariable(value = "id")  Long id){
        return new Vote();
    }

    /**
     * 新增一条记录
     * @param vote
     * @return
     */
    @RequestMapping(value = "/add",  method = RequestMethod.POST)
    public Long  add(Vote vote){
        return 1l;
    }

    /**
     * 删除一条商家记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}",  method = RequestMethod.DELETE)
    public int  delete(@PathVariable(value="id") Long id){
        return 0;
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
