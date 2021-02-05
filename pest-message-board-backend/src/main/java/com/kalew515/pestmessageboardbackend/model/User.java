package com.kalew515.pestmessageboardbackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @TableField(value = "username")
    private String username;

    @TableField(value = "password")
    private String password;

    @TableField(value = "date_joined")
    private String dateJoined;

    @TableField(value = "is_superuser")
    private Integer isSuperuser;

    @TableField(value = "signature")
    private String signature;

    @TableField(value = "avatar_uuid")
    private String avatarUUID;
}
