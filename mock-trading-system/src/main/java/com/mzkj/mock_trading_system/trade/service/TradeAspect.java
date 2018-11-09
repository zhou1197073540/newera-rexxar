package com.mzkj.mock_trading_system.trade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import com.mzkj.mock_trading_system.trade.valobj.AspectExceptionInfo;
import com.mzkj.mock_trading_system.trade.valobj.StockMarketSnapshotPO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * create by zhouzhenyang on 2018/8/14
 */
@Aspect
@Configuration
public class TradeAspect {

    Logger logger = LoggerFactory.getLogger(TradeAspect.class);
    @Autowired
    ObjectMapper objectMapper;

    @AfterThrowing(value = "execution(* com.mzkj.mock_trading_system.trade.entity.OrderMatcher.doMatching(..))", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        AspectExceptionInfo aspectExceptionInfo = new AspectExceptionInfo();
        Signature signature = joinPoint.getSignature();
        aspectExceptionInfo.setMethod((signature.getModifiers()) + " " +
                signature.getDeclaringTypeName() +
                "#" +
                signature.getName());
        aspectExceptionInfo.setException(e);
        //获取传入目标方法的参数
        Object[] args = joinPoint.getArgs();
        for (Object o : args) {
            //UserOrders
            if (o instanceof List) {
                aspectExceptionInfo.setUserOrders((List<UserOrder>) o);
            } else if (o instanceof Map) {
                aspectExceptionInfo.setStocks((Map<String, StockMarketSnapshotPO>) o);
            }
        }
        try {
            final String s = objectMapper.writeValueAsString(aspectExceptionInfo);
            logger.error("doMatch aspect error json", s);
        } catch (JsonProcessingException e1) {
            logger.error("doMatch aspect error string", aspectExceptionInfo.toString());
            e1.printStackTrace();
        }
    }
}
