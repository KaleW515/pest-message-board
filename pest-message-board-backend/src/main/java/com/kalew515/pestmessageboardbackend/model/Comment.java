package com.kalew515.pestmessageboardbackend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Comment {
    @TableField(value = "comment_id")
    Integer commentId;

    @TableField(value = "like_num")
    Integer likeNum;

    @TableField(value = "dislike_num")
    Integer dislikeNum;

    @TableField(value = "content")
    String content;

    @TableField(value = "pub_date")
    String publishTime;

    @TableField(value = "user_id")
    Integer userId;
}
