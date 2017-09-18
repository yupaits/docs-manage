package com.yupaits.docs.common.response;

/**
 * 返回码和返回描述封装类
 * Created by yupaits on 2017/8/8.
 */
public enum ResultCode {
    OK(200, "成功"),
    FAIL(201, "失败"),
    LOGIN_FAIL(202, "用户名或密码错误"),
    PARAMS_ERROR(203, "参数有误"),
    DATA_NOT_FOUND(204, "数据不存在或已被删除"),
    DATA_CANNOT_DELETE(205, "数据无法删除"),
    DB_ERROR(206, "数据库错误"),
    UNAUTHORIZED(401, "无效的授权信息"),
    FORBIDDEN(403, "不允许访问");

    private int code;
    private String msg;

    ResultCode(int code, String msg) {
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
