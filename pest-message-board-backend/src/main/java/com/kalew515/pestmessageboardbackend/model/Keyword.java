package com.kalew515.pestmessageboardbackend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Keyword {
    @TableField(value = "keyword_id")
    Integer keywordId;

    @TableField(value = "keyword")
    String keyword;
}
