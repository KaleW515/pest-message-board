package com.kalew515.pestmessageboardbackend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Attachment {
    @TableField(value = "attachment_uuid")
    String attachmentUUID;

    @TableField(value = "attachment_name")
    String attachmentName;
}
