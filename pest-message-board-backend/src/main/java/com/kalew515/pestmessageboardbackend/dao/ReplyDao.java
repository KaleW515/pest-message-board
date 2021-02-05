package com.kalew515.pestmessageboardbackend.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalew515.pestmessageboardbackend.mapper.ReplyMapper;
import com.kalew515.pestmessageboardbackend.model.Reply;
import com.kalew515.pestmessageboardbackend.param.reply.ReplyParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReplyDao {

    @Autowired
    private ReplyMapper replyMapper;

    // 获取留言的单页评论
    public ReplyParams getReply (Integer page, Integer toCommentId, Integer pageSize) {
        Page<Reply> commentPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Reply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reply::getToCommentId, toCommentId);
        ReplyParams replyParams = new ReplyParams();
        replyParams.setReplyParams(replyMapper.selectPage(commentPage, wrapper).getRecords());
        replyParams.setTotal(replyMapper.selectPage(commentPage, wrapper).getTotal());
        return replyParams;
    }

    // 发布评论
    public Integer insertReply (Reply reply) {
        System.out.println(reply);
        return replyMapper.insert(reply);
    }

    // 根据id删除评论
    public Integer deleteReplyById (Integer replyId) {
        return replyMapper.deleteById(replyId);
    }

    // 根据id获取评论
    public Reply getReplyById (Integer replyId) {
        return replyMapper.selectById(replyId);
    }
}
