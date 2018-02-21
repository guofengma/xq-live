package com.xq.live.service.impl;

import com.xq.live.common.Pager;
import com.xq.live.dao.CommentMapper;
import com.xq.live.model.Comment;
import com.xq.live.service.CommentService;
import com.xq.live.vo.in.CommentInVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-08 19:56
 * @copyright:hbxq
 **/
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Long add(Comment comment) {
        int res = commentMapper.insert(comment);
        if(res < 1){
            return null;
        }
        return comment.getId();
    }

    @Override
    public Integer update(Comment comment) {
        return null;
    }

    @Override
    public Integer deleteByInVo(CommentInVo inVo) {
        return commentMapper.deleteComment(inVo);
    }

    @Override
    public Integer delete(Long id) {
        return commentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer get(Long id) {
        return null;
    }

    @Override
    public Pager<Comment> list(CommentInVo inVo) {
        Pager<Comment> result = new Pager<Comment>();
        int total = commentMapper.listTotal(inVo);
        if(total > 0){
            List<Comment> list = commentMapper.list(inVo);
            result.setList(list);
        }
        result.setTotal(total);
        result.setPage(inVo.getPage());
        return result;
    }

    @Override
    public List<Comment> top(CommentInVo inVo) {
        return commentMapper.list(inVo);
    }
}
