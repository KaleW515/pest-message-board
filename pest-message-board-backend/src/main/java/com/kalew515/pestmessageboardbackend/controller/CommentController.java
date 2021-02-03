package com.kalew515.pestmessageboardbackend.controller;

import com.kalew515.pestmessageboardbackend.param.comment.CommentParam;
import com.kalew515.pestmessageboardbackend.param.comment.ResponseCommentParam;
import com.kalew515.pestmessageboardbackend.service.CommentService;
import com.kalew515.pestmessageboardbackend.service.UserService;
import com.kalew515.pestmessageboardbackend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @PostMapping("/comment")
    public Response<List<ResponseCommentParam>> getAllComments (@RequestBody CommentParam commentParam) {
        return Response.success("", commentService.getComments(commentParam));
    }
}
