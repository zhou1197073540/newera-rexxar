package com.mouzhiapp.stock_market.exception.exceptionHandler;


import com.mouzhiapp.stock_market.consts.Consts;
import com.mouzhiapp.stock_market.dto.RespDto;
import com.mouzhiapp.stock_market.exception.DateFormatServiceException;
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

    private Logger logger = LoggerFactory.getLogger(Consts.ERROR_LOGGER);

    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public <T> RespDto<T> defaultErrorHandler(Throwable e) throws Exception {

        if (e instanceof DateFormatServiceException) {
            return new RespDto(false, ((DateFormatServiceException) e).getErrorDate() + "格式错误");
        }
        logger.error("ERROR!!!", e);
        return new RespDto(false, "未处理异常");
    }
}
