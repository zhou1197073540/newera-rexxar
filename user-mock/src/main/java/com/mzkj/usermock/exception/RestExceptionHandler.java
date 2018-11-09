package com.mzkj.usermock.exception;

import com.mzkj.usermock.dto.RespResults;
import com.mzkj.usermock.enums.CodeEnum;
import com.mzkj.usermock.exception.runtime.RequestException;
import com.mzkj.usermock.exception.runtime.SettlementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestExceptionHandler {
    public static Logger logger=LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T> RespResults<T> runtimeExceptionHandler(Exception e){
        logger.error("---------> huge error!{}", e);
        if(e instanceof HttpRequestMethodNotSupportedException){
            return RespResults.genErrorResult(CodeEnum.REQUEST_TYPE_ERROR,e.getMessage());
        }else if(e instanceof HttpMediaTypeNotSupportedException){
            return RespResults.genErrorResult(CodeEnum.REQUEST_CONTENT_TYPE_ERROR,e.getMessage());
        }else if(e instanceof RequestException){
            RequestException es=(RequestException)e;
            return RespResults.genErrorResult(es.getCodeEnum());
        }else if(e instanceof SettlementException){
            SettlementException es=(SettlementException)e;
            return RespResults.genErrorResult(es.getCodeEnum());
        }else{
            return RespResults.genErrorResult(CodeEnum.ERROR,e.getMessage());
        }
    }
}
