package com.yupaits.docs.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupaits.docs.common.response.ResponseBuilder;
import com.yupaits.docs.common.response.ResponseCode;
import com.yupaits.docs.security.helper.TokenHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yupaits on 2017/8/7.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        objectMapper.writeValue(httpServletResponse.getWriter(), ResponseBuilder.fail(ResponseCode.FORBIDDEN));
    }
}
