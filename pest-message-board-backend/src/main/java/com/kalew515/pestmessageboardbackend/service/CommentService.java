package com.kalew515.pestmessageboardbackend.service;

import com.kalew515.pestmessageboardbackend.dao.CommentDao;
import com.kalew515.pestmessageboardbackend.model.Comment;
import com.kalew515.pestmessageboardbackend.param.comment.CommentParams;
import com.kalew515.pestmessageboardbackend.param.comment.RequestCommentParam;
import com.kalew515.pestmessageboardbackend.param.comment.ResponseCommentParam;
import com.kalew515.pestmessageboardbackend.util.RedisTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTool redisTool;

    @Autowired
    private CurrUserService currUserService;

    @Value("${comment.page-size}")
    private Integer pageSize;

    @Value("${base-url}")
    private String baseUrl;

    // 获取留言
    public Map<String, Object> getComments (RequestCommentParam requestCommentParam) {
        if (requestCommentParam.getPage() == null) {
            requestCommentParam.setPage(1);
        }
        List<Comment> comments;
        Long total;
        CommentParams commentParams;
        if (requestCommentParam.getUserId() == null) {
            commentParams = commentDao.getComment(requestCommentParam, pageSize);
        } else {
            if (Objects.equals(currUserService.getCurrUser()
                                              .getUserId(), requestCommentParam.getUserId())) {
                this.resetNotice(requestCommentParam.getUserId());
            }
            commentParams = commentDao.getCommentByUserId(requestCommentParam, pageSize);
        }
        comments = commentParams.getCommentParams();
        total = commentParams.getTotal();
        List<ResponseCommentParam> responseCommentParams = new ArrayList<>();
        for (Comment comment : comments) {
            ResponseCommentParam responseCommentParam = new ResponseCommentParam();
            int commentUserId = comment.getUserId();
            responseCommentParam.setFromUsername(userService.getUserById(commentUserId)
                                                            .getUsername());
            responseCommentParam.setCommentId(comment.getCommentId());
            responseCommentParam.setContent(comment.getContent());
            responseCommentParam.setAvatarUrl(baseUrl + userService.getUserById(commentUserId)
                                                                   .getAvatarUUID());
            responseCommentParam.setLikeNum(comment.getLikeNum());
            responseCommentParam.setDislikeNum(comment.getDislikeNum());
            responseCommentParam.setPubDate(comment.getPublishTime());
            responseCommentParam.setReplyIsShow(false);
            responseCommentParam.setInputIsShow(false);
            responseCommentParams.add(responseCommentParam);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("comment", responseCommentParams);
        result.put("total", total);
        return result;
    }

    // 插入留言
    public Integer insertComment (String content, Integer userId) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setDislikeNum(0);
        comment.setLikeNum(0);
        comment.setUserId(userId);
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        comment.setPublishTime(dateTime.format(formatter));
        return commentDao.insertComment(comment);
    }

    // 点赞
    public boolean addLikeNum (Integer commentId, Integer userId) {
        if (redisTool.sHasKey("like" + commentId, userId.toString())) {
            return false;
        }
        redisTool.sSet("like" + commentId, userId.toString());
        commentDao.addLikeNumCount(commentId);
        redisTool.hincr("info" + commentDao.getCommentByCommentId(commentId)
                                           .getUserId(), "nlike", 1);
        return true;
    }

    // 点踩
    public boolean addDislikeNum (Integer commentId, Integer userId) {
        if (redisTool.sHasKey("dislike" + commentId, userId.toString())) {
            return false;
        }
        redisTool.sSet("dislike" + commentId, userId.toString());
        commentDao.addDislikeNumCount(commentId);
        redisTool.hincr("info" + commentDao.getCommentByCommentId(commentId)
                                           .getUserId(), "ndislike", 1);
        return true;
    }

    // 根据留言id获取留言
    public Comment getCommentByCommentId (Integer commentId) {
        return commentDao.getCommentByCommentId(commentId);
    }

    // 根据留言id删除留言
    public Integer deleteCommentByCommentId (Integer commentId) {
        return commentDao.deleteCommentByCommentId(commentId);
    }

    // 将新通知归零
    public void resetNotice (Integer id) {
        redisTool.hset("info" + id, "nreply", 0);
        redisTool.hset("info" + id, "nlike", 0);
        redisTool.hset("info" + id, "ndislike", 0);
    }
}
