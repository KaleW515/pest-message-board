package com.kalew515.pestmessageboardbackend.param.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReviseParam {
    @NotNull(message = "原始密码不能为空") String oldPassword;

    @NotNull(message = "新密码不能为空") String newPassword;
}
