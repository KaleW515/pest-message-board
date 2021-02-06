package com.kalew515.pestmessageboardbackend.service;

import com.kalew515.pestmessageboardbackend.dao.CommentDao;
import com.kalew515.pestmessageboardbackend.dao.ReplyDao;
import com.kalew515.pestmessageboardbackend.model.Reply;
import com.kalew515.pestmessageboardbackend.param.reply.ReplyParams;
import com.kalew515.pestmessageboardbackend.param.reply.RequestReplyParam;
import com.kalew515.pestmessageboardbackend.param.reply.ResponseReplyParam;
import com.kalew515.pestmessageboardbackend.util.RedisTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReplyService {

    @Autowired
    private ReplyDao replyDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTool redisTool;

    @Autowired
    private CommentDao commentDao;

    @Value("${comment.page-size}")
    private Integer pageSize;

    @Value("${base-url}")
    private String baseUrl;

    // 获取单页评论
    public Map<String, Object> getReply (Integer page, Integer toCommentId) {
        List<ResponseReplyParam> responseReplyParams = new ArrayList<>();
        ReplyParams replyParams = replyDao.getReply(page, toCommentId, pageSize);
        List<Reply> replies = replyParams.getReplyParams();
        for (Reply reply : replies) {
            ResponseReplyParam responseReplyParam = new ResponseReplyParam();
            responseReplyParam.setReplyId(reply.getReplyId());
            responseReplyParam.setContent(reply.getContent());
            responseReplyParam.setPublishDate(reply.getPublishDate());
            responseReplyParam.setAvatarUrl(baseUrl + userService.getUserById(reply.getFromUserId())
                                                                 .getAvatarUUID());
            responseReplyParam.setFromUsername(userService.getUserById(reply.getFromUserId())
                                                          .getUsername());
            responseReplyParams.add(responseReplyParam);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("reply", responseReplyParams);
        result.put("total", replyParams.getTotal());
        return result;
    }

    // 发布评论
    public boolean insertReply (RequestReplyParam requestReplyParam, Integer fromUserId) {
        Reply reply = new Reply();
        reply.setContent(requestReplyParam.getContent());
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        reply.setPublishDate(dateTime.format(formatter));
        reply.setFromUserId(fromUserId);
        reply.setToCommentId(requestReplyParam.getToCommentId());
        redisTool.hincr("info" + commentDao.getCommentByCommentId(requestReplyParam.getToCommentId())
                                           .getUserId(), "nreply", 1);
        return replyDao.insertReply(reply) >= 1;
    }

    // 根据id删除评论
    public Integer deleteReplyByReplyId (Integer replyId) {
        return replyDao.deleteReplyById(replyId);
    }

    // 根据id获取评论
    public Reply getReplyByReplyId (Integer replyId) {
        return replyDao.getReplyById(replyId);
    }
}
