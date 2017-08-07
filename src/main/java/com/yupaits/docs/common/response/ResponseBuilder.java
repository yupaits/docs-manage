package com.yupaits.docs.common.response;

import org.springframework.http.HttpStatus;

/**
 * Created by yupaits on 2017/8/4.
 */
public class ResponseBuilder {

    public static <T> Response buildWithNoData(HttpStatus httpStatus) {
        return build(httpStatus, null);
    }

    public static <T> Response<T> build(HttpStatus httpStatus, T data) {
        return build(httpStatus, httpStatus.getReasonPhrase(), data);
    }

    public static <T> Response<T> build(HttpStatus httpStatus, String msg, T data){
        Response<T> response = new Response<>();
        response.setCode(httpStatus.value());
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

    public static Response buildWithNoData(HttpStatus httpStatus, String msg) {
        return build(httpStatus, msg, null);
    }
}
