package com.acat.handleblogdata.interceptor;

import com.acat.handleblogdata.constants.RestResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public RestResult handleException(Exception exception) {
        String exMessage = exception.getMessage();
        if (StringUtils.isBlank(exMessage)) {
            exMessage = "服务端错误！！！";
        }
        return new RestResult(501, exMessage);
    }
}
