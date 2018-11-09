package com.mzkj.mock_trading_system.remote.exception_handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * create by zhouzhenyang on 2018/8/14
 */
@Component
public class LogUserOrderExceptionHandler implements IUserOrderExceptionHandler {
    @Autowired
    private ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(LogUserOrderExceptionHandler.class);

    @Override
    public void handleUserOrders(List<UserOrder> userOrders) {
        userOrders.forEach(userOrder -> {
            String userOrderStr = null;
            try {
                userOrderStr = objectMapper.writeValueAsString(userOrder);
                logger.error("reach max retries and error json:{}", userOrderStr);
            } catch (JsonProcessingException e1) {
                userOrderStr = userOrder.toString();
                logger.error("reach max retries and error string:{}", userOrderStr);
            }
        });
    }
}
