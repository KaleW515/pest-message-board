package com.kalew515.pestmessageboardbackend.controller;

import com.kalew515.pestmessageboardbackend.checker.LoginChecker;
import com.kalew515.pestmessageboardbackend.interceptor.InterceptCheck;
import com.kalew515.pestmessageboardbackend.param.reply.RequestReplyParam;
import com.kalew515.pestmessageboardbackend.service.CurrUserService;
import com.kalew515.pestmessageboardbackend.service.ReplyService;
import com.kalew515.pestmessageboardbackend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private CurrUserService currUserService;

    @InterceptCheck(checkers = {LoginChecker.class})
    @PostMapping("/reply")
    public Response<String> addReply (@RequestBody @Valid RequestReplyParam requestReplyParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Response.invalid(Objects.requireNonNull(bindingResult.getFieldError())
                                           .getDefaultMessage());
        }
        if (replyService.insertReply(requestReplyParam, currUserService.getCurrUser()
                                                                       .getUserId())) {
            return Response.success("success");
        }
        return Response.invalid("failed");
    }

    @InterceptCheck(checkers = {LoginChecker.class})
    @GetMapping("/reply/{id}/{page}")
    public Response<Map<String, Object>> getReply (@PathVariable Integer id, @PathVariable Integer page) {
        return Response.success("", replyService.getReply(page, id));
    }

    @InterceptCheck(checkers = {LoginChecker.class})
    @DeleteMapping("/reply/{id}")
    public Response<String> deleteReply (@PathVariable Integer id) {
        if (!currUserService.getCurrUser()
                            .getUserId()
                            .equals(replyService.getReplyByReplyId(id)
                                                .getFromUserId())) {
            return Response.invalid("你没有权限");
        }
        if (replyService.deleteReplyByReplyId(id) >= 1) {
            return Response.success("success");
        }
        return Response.failed("failed");
    }
}
