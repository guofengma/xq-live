package com.xq.live.web.controller;/**
 * 主题回复controller
 *
 * @author zhangpeng32
 * @create 2018-01-17 19:08
 */

import com.xq.live.model.Comment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 主题回复controller
 * @author zhangpeng32
 * @create 2018-01-17 19:08
 **/
@RestController
@RequestMapping(value = "/comment")
public class CommentController {

    /**
     * 根据ID查询评论信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Comment getCommentById(@PathVariable(value = "id")  Long id){
        return new Comment();
    }

    /**
     * 新增一条记录
     * @param comment
     * @return
     */
    @RequestMapping(value = "/add",  method = RequestMethod.POST)
    public Long  put(Comment comment){
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
     * @param comment
     * @return
     */
    @RequestMapping(value = "/update",  method = RequestMethod.PUT)
    public int  update(Comment comment){
        return 0;
    }
}
