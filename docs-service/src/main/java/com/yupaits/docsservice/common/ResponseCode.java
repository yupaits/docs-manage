package com.yupaits.docsservice.common;

/**
 * Created by yupaits on 2017/8/4.
 */
public enum ResponseCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败");

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
