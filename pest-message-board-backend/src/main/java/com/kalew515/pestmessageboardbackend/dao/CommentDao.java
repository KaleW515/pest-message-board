package com.kalew515.pestmessageboardbackend.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalew515.pestmessageboardbackend.mapper.CommentMapper;
import com.kalew515.pestmessageboardbackend.model.Comment;
import com.kalew515.pestmessageboardbackend.param.comment.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentDao {
    @Autowired
    private CommentMapper commentMapper;

    // 获取单页评论
    public List<Comment> getComment (CommentParam commentParam, Integer pageSize) {
        Page<Comment> commentPage = new Page<>(commentParam.getPage(), pageSize);
        if (commentParam.getSearchField() != null) {
            LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(Comment::getContent, commentParam.getSearchField());
            return commentMapper.selectPage(commentPage, wrapper).getRecords();
        } else {
            return commentMapper.selectPage(commentPage, null).getRecords();
        }
    }

}
