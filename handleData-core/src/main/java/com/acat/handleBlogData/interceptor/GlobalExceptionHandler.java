package com.acat.handleBlogData.interceptor;

import com.acat.handleBlogData.constants.RestResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Integer ERROR_CODE = 503;
    private static String DEFAULT_ERROR_MSG = "服务端错误！！！";

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public RestResult handleException(Exception exception) {
        String exMessage = StringUtils.isNotBlank(exception.getMessage())
                ? exception.getMessage() : DEFAULT_ERROR_MSG;
        return new RestResult(ERROR_CODE, exMessage);
    }
}
