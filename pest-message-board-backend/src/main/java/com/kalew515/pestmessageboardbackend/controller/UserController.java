package com.kalew515.pestmessageboardbackend.controller;

import com.baomidou.kaptcha.Kaptcha;
import com.kalew515.pestmessageboardbackend.checker.LoginChecker;
import com.kalew515.pestmessageboardbackend.interceptor.InterceptCheck;
import com.kalew515.pestmessageboardbackend.model.User;
import com.kalew515.pestmessageboardbackend.param.user.LoginParam;
import com.kalew515.pestmessageboardbackend.param.user.RegisterParam;
import com.kalew515.pestmessageboardbackend.param.user.ReviseParam;
import com.kalew515.pestmessageboardbackend.service.CurrUserService;
import com.kalew515.pestmessageboardbackend.service.JwtService;
import com.kalew515.pestmessageboardbackend.service.UserService;
import com.kalew515.pestmessageboardbackend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CurrUserService currUserService;

    @Autowired
    private Kaptcha kaptcha;

    @PostMapping("/login")
    public Response<String> login (@Valid @RequestBody LoginParam loginParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Response.invalid(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        List<User> users = userService.getUserByUsername(loginParam.getUsername());
        if (users.size() == 0) {
            return Response.invalid("没有此帐号");
        }
        if (userService.isFreeze(loginParam.getUsername())) {
            return Response.invalid("该帐号已经被冻结");
        }
        if (userService.validatePassword(loginParam)) {
            return Response.success("登陆成功", jwtService.signToken(users.get(0).getUserId()));
        } else {
            userService.addPwdFalseCount(loginParam.getUsername());
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

    @InterceptCheck(checkers = {LoginChecker.class})
    @DeleteMapping("/unfreeze/{id}")
    public Response<String> unFreeze (@PathVariable Integer id) {
        if (userService.unFreezeUser(id)) {
            return Response.success("解冻成功");
        } else {
            return Response.invalid("解冻失败");
        }
    }

    @InterceptCheck(checkers = {LoginChecker.class})
    @PostMapping("/revise")
    public Response<String> revise (@Valid @RequestBody ReviseParam reviseParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Response.invalid(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        User user = currUserService.getCurrUser();
        if (userService.validatePassword(user, reviseParam.getOldPassword())) {
            userService.updatePassword(user, reviseParam.getNewPassword());
            return Response.success("更新成功");
        } else {
            return Response.invalid("原始密码有误");
        }
    }

    @GetMapping("/captcha")
    public void render() {
        kaptcha.render();
    }

    @PostMapping("/valid")
    public void validDefaultTime(@RequestParam String code) {
        //default timeout 900 seconds
        kaptcha.validate(code);
    }

    @PostMapping("/validTime")
    public void validCustomTime(@RequestParam String code) {
        kaptcha.validate(code, 60);
    }
}
