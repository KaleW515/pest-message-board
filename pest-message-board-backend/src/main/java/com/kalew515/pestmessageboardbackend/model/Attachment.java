package com.kalew515.pestmessageboardbackend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {
    @TableId
    @TableField(value = "attachment_uuid")
    String attachmentUuid;

    @TableField(value = "attachment_name")
    String attachmentName;
}
