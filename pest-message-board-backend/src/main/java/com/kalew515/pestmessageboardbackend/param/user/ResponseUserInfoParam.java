package com.kalew515.pestmessageboardbackend.param.user;

import lombok.Data;

@Data
public class ResponseUserInfoParam {

    private Integer userId;

    private String username;

    private String signature;

    private String avatarUuid;

    private Integer isSuperuser;
}
