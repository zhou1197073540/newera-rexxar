package com.mzkj.mock_trading_system.common.exception_handler;


import com.mzkj.mock_trading_system.common.dto.RespDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.mzkj.mock_trading_system.common.consts.Message.UNCAUGHT_EXCEPTION;


/**
 * Created by zhouzhenyang on 2017/6/5.
 */
@ControllerAdvice(basePackages = "com.mzkj.mock_trading_system.receive.controller")
public class GlobalDefaultExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RespDto defaultErrorHandler(Throwable e) {
        logger.error("ERROR!!!", e);
        return new RespDto<>(UNCAUGHT_EXCEPTION.statusCode, UNCAUGHT_EXCEPTION.message);
    }
}
