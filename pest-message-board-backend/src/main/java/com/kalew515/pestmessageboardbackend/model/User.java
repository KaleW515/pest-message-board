package com.kalew515.pestmessageboardbackend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class User {
    @TableField(value = "user_id")
    Integer userId;

    @TableField(value = "username")
    String username;

    @TableField(value = "password")
    String password;

    @TableField(value = "date_joined")
    String dateJoined;

    @TableField(value = "is_superuser")
    Integer isSuperuser;

    @TableField(value = "signature")
    String signature;

    @TableField(value = "avatar_uuid")
    String avatarUUID;
}
