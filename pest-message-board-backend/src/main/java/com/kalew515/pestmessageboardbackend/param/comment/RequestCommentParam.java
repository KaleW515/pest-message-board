package com.kalew515.pestmessageboardbackend.param.comment;

import lombok.Data;

@Data
public class RequestCommentParam {

    private String searchField;

    private Integer page;

    private Boolean seqValue;

    private Integer userId;

}
