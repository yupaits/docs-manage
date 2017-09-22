package com.yupaits.docs.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 接口返回对象
 * Created by yupaits on 2017/8/8.
 */
@Data
@AllArgsConstructor
public class Result<T> {
    //返回码
    private int code;

    //返回描述
    private String msg;

    //返回数据
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private static <T> Result build(ResultCode resultCode, T data) {
        //noinspection unchecked
        return new Result(resultCode.getCode(), resultCode.getMsg(), data);
    }

    public static Result ok() {
        return ok(null);
    }

    public static <T> Result ok(T data) {
        return build(ResultCode.OK, data);
    }

    public static Result fail(ResultCode resultCode) {
        return build(resultCode, null);
    }

    public static Result fail(String msg) {
        //noinspection unchecked
        return new Result(ResultCode.FAIL.getCode(), msg, null);
    }
}
