package com.kalew515.pestmessageboardbackend.param.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegisterParam {

    @NotNull(message = "用户名不能为空")
    String username;

    @NotNull(message = "密码不能为空")
    String password;
}
