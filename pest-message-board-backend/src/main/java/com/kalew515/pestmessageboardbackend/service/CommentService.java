package com.kalew515.pestmessageboardbackend.service;

import com.kalew515.pestmessageboardbackend.dao.CommentDao;
import com.kalew515.pestmessageboardbackend.model.Comment;
import com.kalew515.pestmessageboardbackend.param.comment.CommentParam;
import com.kalew515.pestmessageboardbackend.param.comment.ResponseCommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserService userService;

    @Value("${comment.page-size}")
    private Integer pageSize;

    // 获取评论
    public List<ResponseCommentParam> getComments (CommentParam commentParam) {
        if (commentParam.getPage() == null) {
            commentParam.setPage(1);
        }
        List<Comment> comments = commentDao.getComment(commentParam, pageSize);
        List<ResponseCommentParam> responseCommentParams = new ArrayList<>();
        for (Comment comment : comments) {
            ResponseCommentParam responseCommentParam = new ResponseCommentParam();
            int commentUserId = comment.getUserId();
            responseCommentParam.setFromUsername(userService.getUserById(commentUserId)
                                                            .getUsername());
            responseCommentParam.setCommentId(comment.getCommentId());
            responseCommentParam.setContent(comment.getContent());
            responseCommentParam.setAvatarUrl(userService.getUserById(commentUserId)
                                                         .getAvatarUUID());
            responseCommentParam.setLikeNum(comment.getLikeNum());
            responseCommentParam.setDislikeNum(comment.getDislikeNum());
            responseCommentParam.setPubDate(comment.getPublishTime());
            responseCommentParams.add(responseCommentParam);
        }
        return responseCommentParams;
    }
}
