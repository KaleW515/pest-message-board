package com.kalew515.pestmessageboardbackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Comment {
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    @TableField(value = "like_num")
    private Integer likeNum;

    @TableField(value = "dislike_num")
    private Integer dislikeNum;

    @TableField(value = "content")
    private String content;

    @TableField(value = "pub_date")
    private String publishTime;

    @TableField(value = "user_id")
    private Integer userId;
}
