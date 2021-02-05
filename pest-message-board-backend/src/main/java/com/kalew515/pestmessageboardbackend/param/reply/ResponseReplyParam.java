package com.kalew515.pestmessageboardbackend.param.reply;

import lombok.Data;

@Data
public class ResponseReplyParam {

    private Integer replyId;

    private String fromUsername;

    private String publishDate;

    private String content;

    private String avatarUrl;
}
