package com.yupaits.docs.common.response;

/**
 * Created by yupaits on 2017/8/8.
 */
public enum ResponseCode {
    OK(200, "成功"),
    FAIL(201, "失败"),
    NotLoggedIN(202, "未登录或登录失效"),
    UNAUTHORIZED(401, "无效的授权信息"),
    FORBIDDEN(403, "不允许访问");

    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
