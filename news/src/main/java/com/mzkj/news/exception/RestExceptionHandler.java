package com.mzkj.news.exception;

import com.mzkj.news.constant.ErrorCode;
import com.mzkj.news.dto.RespResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.Controller;

@ControllerAdvice
public class RestExceptionHandler {
    public static Logger logger=LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T> RespResult<T> runtimeExceptionHandler(Exception e){
        logger.error("---------> huge error!", e);
        return RespResult.genErrorResult(ErrorCode.SERVER_ERROR);
    }
}
