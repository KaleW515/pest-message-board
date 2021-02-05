package com.kalew515.pestmessageboardbackend.param.comment;

import lombok.Data;

@Data
public class ResponseCommentParam {

    private String pubDate;

    private String fromUsername;

    private String avatarUrl;

    private Integer commentId;

    private Integer likeNum;

    private Integer dislikeNum;

    private String content;

    private Boolean replyIsShow;

    private Boolean inputIsShow;
}
