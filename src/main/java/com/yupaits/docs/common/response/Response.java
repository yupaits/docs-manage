package com.yupaits.docs.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by yupaits on 2017/8/8.
 */
public class Response<T> {
    private int code;
    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
