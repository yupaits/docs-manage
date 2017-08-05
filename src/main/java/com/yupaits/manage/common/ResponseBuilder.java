package com.yupaits.manage.common;

/**
 * Created by yupaits on 2017/8/4.
 */
public class ResponseBuilder {

    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> success(T data) {
        return normal(ResponseCode.SUCCESS, data);
    }

    public static <T> Response<T> fail() {
        return normal(ResponseCode.FAIL, null);
    }

    public static <T> Response<T> fail(String msg) {
        Response<T> response = normal(ResponseCode.FAIL, null);
        response.setMsg(msg);
        return response;
    }

    public static <T> Response<T> normal(ResponseCode responseCode, T data) {
        Response<T> response = new Response<>();
        response.setCode(responseCode.getCode());
        response.setMsg(responseCode.getMsg());
        response.setData(data);
        return response;
    }
}
