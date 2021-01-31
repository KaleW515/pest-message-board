package com.kalew515.pestmessageboardbackend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Reply {
    @TableField(value = "reply_id")
    Integer replyId;

    @TableField(value = "from_user_id")
    Integer fromUserId;

    @TableField(value = "to_comment_id")
    Integer toCommentId;

    @TableField(value = "pub_date")
    String publishTime;

    @TableField(value = "content")
    String content;
}
