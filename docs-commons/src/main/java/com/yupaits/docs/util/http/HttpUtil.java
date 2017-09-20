package com.yupaits.docs.util.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Http工具类
 * Created by yupaits on 2017/9/20.
 */
public class HttpUtil {

    private static final ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();

    public static void setRequest(HttpServletRequest request) {
        requestThreadLocal.set(request);
    }

    public static HttpServletRequest getRequest() {
        return requestThreadLocal.get();
    }

    public static HttpSession getSession() {
        HttpServletRequest request = requestThreadLocal.get();
        if (request == null) {
            return null;
        }
        return request.getSession();
    }
}
