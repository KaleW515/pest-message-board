package com.kalew515.pestmessageboardbackend.param.reply;

import com.kalew515.pestmessageboardbackend.model.Reply;
import lombok.Data;

import java.util.List;

@Data
public class ReplyParams {

    private List<Reply> replyParams;

    private Long total;
}
