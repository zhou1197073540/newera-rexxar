package com.mzkj.mock_trading_system.receive.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * create by zhouzhenyang on 2018/7/31
 */
@Component("operationHandlerFactory")
public class OrderOperationHandlerFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public <T> T getHandler(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
