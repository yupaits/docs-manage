package com.yupaits.docsservice.common;

/**
 * 接口返回体封装
 * Created by yupaits on 2017/8/4.
 */
public class Response<T> {

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回描述
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

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
