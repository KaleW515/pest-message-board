package com.kalew515.pestmessageboardbackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Reply {

    @TableId(value = "reply_id", type = IdType.AUTO)
    private Integer replyId;

    @TableField(value = "from_user_id")
    private Integer fromUserId;

    @TableField(value = "to_comment_id")
    private Integer toCommentId;

    @TableField(value = "publish_date")
    private String publishDate;

    @TableField(value = "content")
    private String content;
}
