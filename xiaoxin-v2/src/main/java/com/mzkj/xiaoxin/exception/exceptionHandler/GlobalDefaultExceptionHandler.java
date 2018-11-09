package com.mzkj.xiaoxin.exception.exceptionHandler;


import com.mzkj.xiaoxin.dto.RespDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Created by zhouzhenyang on 2017/6/5.
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    private Logger logger =
            LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public <T> RespDto<T> defaultErrorHandler(Throwable e) throws Exception {

        logger.error("ERROR!!!", e);
        return new RespDto<>(false, "未处理异常");
    }
}
