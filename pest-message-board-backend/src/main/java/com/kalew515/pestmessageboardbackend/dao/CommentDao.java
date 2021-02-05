package com.kalew515.pestmessageboardbackend.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalew515.pestmessageboardbackend.mapper.CommentMapper;
import com.kalew515.pestmessageboardbackend.model.Comment;
import com.kalew515.pestmessageboardbackend.param.comment.CommentParams;
import com.kalew515.pestmessageboardbackend.param.comment.RequestCommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class CommentDao {

    @Autowired
    private CommentMapper commentMapper;

    // 获取单页留言
    public CommentParams getComment (RequestCommentParam requestCommentParam, Integer pageSize) {
        Page<Comment> commentPage = new Page<>(requestCommentParam.getPage(), pageSize);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        CommentParams commentParams = new CommentParams();
        if (!Objects.equals(requestCommentParam.getSearchField(), "")) {
            if (requestCommentParam.getSeqValue()) {
                wrapper.like(Comment::getContent, requestCommentParam.getSearchField())
                       .orderByDesc(Comment::getLikeNum);
            } else {
                wrapper.like(Comment::getContent, requestCommentParam.getSearchField());
            }
        } else {
            if (requestCommentParam.getSeqValue()) {
                wrapper.orderByDesc(Comment::getLikeNum);
            } else {
                wrapper.orderByDesc(Comment::getPublishTime);
            }
        }
        commentParams.setCommentParams(commentMapper.selectPage(commentPage, wrapper)
                                                    .getRecords());
        commentParams.setTotal(commentMapper.selectPage(commentPage, wrapper)
                                            .getTotal());
        return commentParams;
    }

    // 获取单用户留言
    public CommentParams getCommentByUserId (RequestCommentParam requestCommentParam, Integer pageSize) {
        Page<Comment> commentPage = new Page<>(requestCommentParam.getPage(), pageSize);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getUserId, requestCommentParam.getUserId());
        CommentParams commentParams = new CommentParams();
        commentParams.setCommentParams(commentMapper.selectPage(commentPage, wrapper)
                                                    .getRecords());
        commentParams.setTotal(commentMapper.selectPage(commentPage, wrapper)
                                            .getTotal());
        return commentParams;
    }

    // 添加评论
    public Integer insertComment (Comment comment) {
        return commentMapper.insert(comment);
    }

    // 增加点赞数
    public void addLikeNumCount (Integer commentId) {
        int oldLikeNumCount = commentMapper.selectById(commentId)
                                           .getLikeNum();
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setLikeNum(oldLikeNumCount + 1);
        commentMapper.updateById(comment);
    }

    // 增加点踩数
    public void addDislikeNumCount (Integer commentId) {
        int oldDislikeNumCount = commentMapper.selectById(commentId)
                                              .getDislikeNum();
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setDislikeNum(oldDislikeNumCount + 1);
        commentMapper.updateById(comment);
    }

    // 根据id获取留言
    public Comment getCommentByCommentId (Integer commentId) {
        return commentMapper.selectById(commentId);
    }

    // 根据id删除留言
    public Integer deleteCommentByCommentId (Integer commentId) {
        return commentMapper.deleteById(commentId);
    }
}
