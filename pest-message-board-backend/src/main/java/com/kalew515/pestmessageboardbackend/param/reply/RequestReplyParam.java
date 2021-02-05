package com.kalew515.pestmessageboardbackend.param.reply;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestReplyParam {

    @NotNull(message = "内容不可为空")
    private String content;

    @NotNull(message = "请指定留言")
    private Integer toCommentId;
}
