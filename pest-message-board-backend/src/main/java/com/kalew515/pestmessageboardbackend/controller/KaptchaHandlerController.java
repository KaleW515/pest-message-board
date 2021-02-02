package com.kalew515.pestmessageboardbackend.controller;

import com.baomidou.kaptcha.exception.KaptchaException;
import com.baomidou.kaptcha.exception.KaptchaIncorrectException;
import com.baomidou.kaptcha.exception.KaptchaNotFoundException;
import com.baomidou.kaptcha.exception.KaptchaTimeoutException;
import com.kalew515.pestmessageboardbackend.util.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class KaptchaHandlerController {
    @ExceptionHandler(value = KaptchaException.class)
    public Response<String> kaptchaExceptionHandler (KaptchaException kaptchaException) {
        if (kaptchaException instanceof KaptchaIncorrectException) {
            return Response.invalid("验证码不正确");
        } else if (kaptchaException instanceof KaptchaNotFoundException) {
            return Response.invalid("验证码未找到");
        } else if (kaptchaException instanceof KaptchaTimeoutException) {
            return Response.invalid("验证码过期");
        } else {
            return Response.invalid("验证码渲染失败");
        }
    }
}
