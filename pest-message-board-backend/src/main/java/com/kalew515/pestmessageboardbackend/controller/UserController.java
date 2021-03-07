package com.kalew515.pestmessageboardbackend.controller;

import com.baomidou.kaptcha.Kaptcha;
import com.kalew515.pestmessageboardbackend.checker.AdminChecker;
import com.kalew515.pestmessageboardbackend.checker.LoginChecker;
import com.kalew515.pestmessageboardbackend.interceptor.InterceptCheck;
import com.kalew515.pestmessageboardbackend.model.User;
import com.kalew515.pestmessageboardbackend.param.user.LoginParam;
import com.kalew515.pestmessageboardbackend.param.user.RegisterParam;
import com.kalew515.pestmessageboardbackend.param.user.ResponseUserInfoParam;
import com.kalew515.pestmessageboardbackend.param.user.ReviseParam;
import com.kalew515.pestmessageboardbackend.service.CurrUserService;
import com.kalew515.pestmessageboardbackend.service.JwtService;
import com.kalew515.pestmessageboardbackend.service.UserService;
import com.kalew515.pestmessageboardbackend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@InterceptCheck(checkers = {LoginChecker.class})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CurrUserService currUserService;

    @Autowired
    private Kaptcha kaptcha;

    @Value("${base-url}")
    private String baseUrl;

    @PostMapping("/login")
    public Response<String> login (@Valid @RequestBody LoginParam loginParam,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Response.invalid(Objects.requireNonNull(bindingResult.getFieldError())
                                           .getDefaultMessage());
        }
        kaptcha.validate(loginParam.getCode());
        List<User> users = userService.getUserByUsername(loginParam.getUsername());
        if (users.size() == 0) {
            return Response.invalid("没有此帐号");
        }
        if (userService.isFreeze(loginParam.getUsername())) {
            return Response.invalid("该帐号已经被冻结");
        }
        if (userService.validatePassword(loginParam)) {
            return Response.success("登录成功", jwtService.signToken(users.get(0)
                                                                      .getUserId()));
        } else {
            userService.addPwdFalseCount(loginParam.getUsername());
            return Response.invalid("密码错误");
        }
    }

    @PostMapping("/register")
    public Response<String> register (@Valid @RequestBody RegisterParam registerParam,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Response.invalid(Objects.requireNonNull(bindingResult.getFieldError())
                                           .getDefaultMessage());
        }
        List<User> users = userService.getUserByUsername(registerParam.getUsername());
        if (users.size() >= 1) {
            return Response.invalid("你已经注册过了");
        }
        userService.insertUser(registerParam);
        return Response.success("注册成功");
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @DeleteMapping("/unfreeze/{id}")
    public Response<String> unFreeze (@PathVariable Integer id) {
        if (userService.unFreezeUser(id)) {
            return Response.success("解冻成功");
        } else {
            return Response.invalid("解冻失败");
        }
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/freeze/{id}")
    public Response<String> freeze (@PathVariable Integer id) {
        if (userService.freezeUser(id)) {
            return Response.success("冻结成功");
        }
        return Response.invalid("冻结失败");
    }

    @InterceptCheck(checkers = {LoginChecker.class})
    @PostMapping("/revise")
    public Response<String> revise (@Valid @RequestBody ReviseParam reviseParam,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Response.invalid(Objects.requireNonNull(bindingResult.getFieldError())
                                           .getDefaultMessage());
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
    public void render () {
        kaptcha.render();
    }

    @PostMapping("/validTime")
    public void validCustomTime (@RequestParam String code) {
        kaptcha.validate(code, 60);
    }

    // 获取当前用户信息
    @GetMapping("/info")
    public Response<ResponseUserInfoParam> getUserInfo () {
        return Response.success("", userService.getUserInfo());
    }

    // 修改用户签名
    @InterceptCheck(checkers = {LoginChecker.class})
    @PostMapping("/signature")
    public Response<String> updateSignature (@RequestBody Map<String, String> signature) {
        userService.updateSignature(currUserService.getCurrUser(), signature.get("signature"));
        return Response.success("success");
    }

    // 根据姓名获取用户信息
    @InterceptCheck(checkers = {LoginChecker.class})
    @GetMapping("/info/{name}")
    public Response<Map<String, Object>> getUserInfoByName (@PathVariable String name) {
        User user = userService.getUserByUsername(name)
                               .get(0);
        Map<String, Object> result = new HashMap<>();
        result.put("signature", user.getSignature());
        result.put("username", user.getUsername());
        result.put("avatarUuid", baseUrl + user.getAvatarUUID());
        return Response.success("", result);
    }

    // 获取全部用户信息
    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/all")
    public Response<Map<String, Object>> getAllUserInfo () {
        List<User> users = userService.getAllUser();
        Map<String, Object> result = new HashMap<>();
        result.put("users", users);
        return Response.success("", result);
    }

    @InterceptCheck(checkers = {LoginChecker.class})
    @PostMapping("/logout")
    public Response<String> logout () {
        CurrUserService.destroy();
        return Response.success("success");
    }
}
