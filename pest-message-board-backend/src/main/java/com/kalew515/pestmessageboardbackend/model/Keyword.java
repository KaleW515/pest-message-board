package com.kalew515.pestmessageboardbackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Keyword {
    @TableId(value = "keyword_id", type = IdType.AUTO)
    private Integer keywordId;

    @TableField(value = "keyword")
    private String keyword;
}
