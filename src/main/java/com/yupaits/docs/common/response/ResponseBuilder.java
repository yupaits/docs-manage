package com.yupaits.docs.common.response;

/**
 * Created by yupaits on 2017/8/8.
 */
public class ResponseBuilder {

    public static Response ok() {
        return ok(null);
    }

    public static  <T> Response<T> ok(T data) {
        return build(ResponseCode.OK.getCode(), ResponseCode.OK.getMsg(), data);
    }

    public static Response fail(String msg) {
        return build(ResponseCode.FAIL.getCode(), msg, null);
    }

    public static Response fail(ResponseCode responseCode) {
        return build(responseCode.getCode(), responseCode.getMsg(), null);
    }

    private static  <T> Response<T> build(int code, String msg, T data) {
        return new Response<T>(code, msg, data);
    }
}
