package com.yupaits.docs.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupaits.docs.common.constant.ApplicationConstant;
import com.yupaits.docs.common.response.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yupaits on 2017/8/7.
 */
@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType(ApplicationConstant.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), ResponseBuilder.buildWithNoData(HttpStatus.UNAUTHORIZED, exception.getMessage()));
    }
}
