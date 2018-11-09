package com.mouzhiapp.stock_market.aop;

/**
 * create by zhouzhenyang on 2018/5/31
 */

import com.mouzhiapp.stock_market.annotation.SelfSelected;
import com.mouzhiapp.stock_market.bean.UserSelectedStock;
import com.mouzhiapp.stock_market.dto.RespDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class SelfSelectedAspect {

    @Around("@annotation(selfSelected)")
    public RespDto<UserSelectedStock> doAround(ProceedingJoinPoint joinPoint, SelfSelected selfSelected) throws Throwable {

        RespDto<UserSelectedStock> ret = (RespDto<UserSelectedStock>) joinPoint.proceed();
        if (ret.getData() != null) {
            // TODO: 2018/5/31 查看是否是自选股
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            request.getHeader("token");
            ret.getData().setIsSelected("0");
        }

        return ret;
    }
}

