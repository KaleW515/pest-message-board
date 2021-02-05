package com.kalew515.pestmessageboardbackend.param.comment;

import com.kalew515.pestmessageboardbackend.model.Comment;
import lombok.Data;

import java.util.List;

@Data
public class CommentParams {

    private List<Comment> commentParams;

    private Long total;
}
