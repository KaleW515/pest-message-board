package com.kalew515.pestmessageboardbackend.controller;

import com.kalew515.pestmessageboardbackend.model.User;
import com.kalew515.pestmessageboardbackend.param.user.LoginParam;
import com.kalew515.pestmessageboardbackend.param.user.RegisterParam;
import com.kalew515.pestmessageboardbackend.service.UserService;
import com.kalew515.pestmessageboardbackend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Response<String> login (@Valid @RequestBody LoginParam loginParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Response.invalid(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        List<User> users = userService.getUserByUsername(loginParam.getUsername());
        if (users.size() == 0) {
            return Response.invalid("没有此帐号");
        }
        if (userService.validatePassword(loginParam)) {
            return Response.success("登陆成功");
        } else {
            return Response.invalid("密码错误");
        }
    }

    @PostMapping("/register")
    public Response<String> register (@Valid @RequestBody RegisterParam registerParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Response.invalid(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        List<User> users = userService.getUserByUsername(registerParam.getUsername());
        if (users.size() >= 1) {
            return Response.invalid("你已经注册过了");
        }
        userService.insertUser(registerParam);
        return Response.success("注册成功");
    }
}
