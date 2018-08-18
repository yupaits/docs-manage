package com.yupaits.docs.controller.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yupaits.docs.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yupaits
 * @date 2018/8/17
 */
@ControllerAdvice
public class GlobalAdvice {

    private final ObjectMapper objectMapper;

    @Autowired
    public GlobalAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void validateErrorHandler(HttpServletResponse response, DataIntegrityViolationException exception) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        objectMapper.writeValue(response.getWriter(), Result.fail("数据库更新数据有误"));
    }
}
