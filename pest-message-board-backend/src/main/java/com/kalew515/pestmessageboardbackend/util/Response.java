package com.kalew515.pestmessageboardbackend.util;

import lombok.Data;

@Data
public class Response<T> {
    private int code;
    private String msg;
    private T payload;

    public static <T> Response<T> success (String msg, T payload) {
        Response<T> response = new Response<>();
        response.setCode(200);
        response.setMsg(msg);
        response.setPayload(payload);
        return response;
    }

    public static <T> Response<T> success (String msg) {
        Response<T> response = new Response<>();
        response.setCode(200);
        response.setMsg(msg);
        response.setPayload(null);
        return response;
    }

    public static <T> Response<T> failed (String msg, T payload) {
        Response<T> response = new Response<>();
        response.setCode(500);
        response.setMsg(msg);
        response.setPayload(payload);
        return response;
    }

    public static <T> Response<T> failed (String msg) {
        Response<T> response = new Response<>();
        response.setCode(500);
        response.setMsg(msg);
        response.setPayload(null);
        return response;
    }

    public static <T> Response<T> invalid (String msg, T payload) {
        Response<T> response = new Response<>();
        response.setCode(400);
        response.setMsg(msg);
        response.setPayload(payload);
        return response;
    }

    public static <T> Response<T> invalid (String msg) {
        Response<T> response = new Response<>();
        response.setCode(400);
        response.setMsg(msg);
        response.setPayload(null);
        return response;
    }
}
