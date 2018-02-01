package com.xq.live.web.controller;/**
 * 主题controller
 *
 * @author zhangpeng32
 * @create 2018-01-17 18:56
 */

import com.xq.live.model.Topic;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 主题controller
 * @author zhangpeng32
 * @create 2018-01-17 18:56
 **/
@RestController
@RequestMapping(value = "/topic")
public class TopicController {

    /**
     * 根据ID查询主题信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Topic getTopicById(@PathVariable(value = "id")  Long id){
        return new Topic();
    }

    /**
     * 新增一条商家记录
     * @param topic
     * @return
     */
    @RequestMapping(value = "/add",  method = RequestMethod.POST)
    public Long  put(Topic topic){
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
     * @param topic
     * @return
     */
    @RequestMapping(value = "/update",  method = RequestMethod.PUT)
    public int  update(Topic topic){
        return 0;
    }
}
