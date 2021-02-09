package com.kalew515.pestmessageboardbackend.controller;

import com.kalew515.pestmessageboardbackend.checker.LoginChecker;
import com.kalew515.pestmessageboardbackend.interceptor.InterceptCheck;
import com.kalew515.pestmessageboardbackend.param.comment.RequestCommentParam;
import com.kalew515.pestmessageboardbackend.service.CommentService;
import com.kalew515.pestmessageboardbackend.service.CurrUserService;
import com.kalew515.pestmessageboardbackend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CurrUserService currUserService;

    @Autowired
    private WebSocketController webSocketController;

    @InterceptCheck(checkers = {LoginChecker.class})
    @PostMapping("/comment")
    public Response<Map<String, Object>> getAllComments (@RequestBody RequestCommentParam requestCommentParam) {
        return Response.success("", commentService.getComments(requestCommentParam));
    }

    @InterceptCheck(checkers = {LoginChecker.class})
    @PostMapping("/publish")
    public Response<String> publishComment (@RequestBody @Valid Map<String, String> content, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Response.invalid(Objects.requireNonNull(bindingResult.getFieldError())
                                           .getDefaultMessage());
        }
        if (commentService.insertComment(content.get("content"), currUserService.getCurrUser()
                                                                                .getUserId()) >= 1) {
            return Response.success("添加成功");
        } else {
            return Response.invalid("添加失败");
        }
    }

    @InterceptCheck(checkers = {LoginChecker.class})
    @DeleteMapping("/comment/{id}")
    public Response<String> deleteComment (@PathVariable Integer id) {
        if (!commentService.getCommentByCommentId(id)
                           .getUserId()
                           .equals(currUserService.getCurrUser()
                                                  .getUserId())) {
            return Response.invalid("你没有权限删除");
        }
        if (commentService.deleteCommentByCommentId(id) >= 1) {
            return Response.success("success");
        }
        return Response.invalid("failed");
    }

    @InterceptCheck(checkers = {LoginChecker.class})
    @PostMapping("/like/{id}")
    public Response<String> incrLikeNum (@PathVariable Integer id) {
        if (commentService.addLikeNum(id, currUserService.getCurrUser()
                                                         .getUserId())) {
            return Response.success("success");
        }
        return Response.invalid("failed");
    }

    @InterceptCheck(checkers = {LoginChecker.class})
    @PostMapping("/dislike/{id}")
    public Response<String> incrDislikeNum (@PathVariable Integer id) {
        if (commentService.addDislikeNum(id, currUserService.getCurrUser()
                                                            .getUserId())) {
            return Response.success("success");
        }
        return Response.invalid("failed");
    }
}
