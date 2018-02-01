package com.xq.live.web.controller;/**
 * 点赞控制类
 *
 * @author zhangpeng32
 * @create 2018-01-17 19:10
 */

import com.xq.live.model.Zan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 点赞控制类
 * @author zhangpeng32
 * @create 2018-01-17 19:10
 **/
@RestController
@RequestMapping(value = "/zan")
public class ZanController {

    /**
     * 根据ID查询评论信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Zan getZanById(@PathVariable(value = "id")  Long id){
        return new Zan();
    }

    /**
     * 新增一条记录
     * @param zan
     * @return
     */
    @RequestMapping(value = "/add",  method = RequestMethod.POST)
    public Long  add(Zan zan){
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
     * @param zan
     * @return
     */
    @RequestMapping(value = "/update",  method = RequestMethod.PUT)
    public int  update(Zan zan){
        return 0;
    }
}
