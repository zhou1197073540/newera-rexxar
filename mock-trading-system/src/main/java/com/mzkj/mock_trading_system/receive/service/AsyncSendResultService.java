package com.mzkj.mock_trading_system.receive.service;

import com.mzkj.mock_trading_system.remote.RemoteUserService;
import com.mzkj.mock_trading_system.remote.RetryAbleRemoteUserService;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * create by zhouzhenyang on 2018/8/9
 */
@Service
public class AsyncSendResultService {

    @Autowired
    RetryAbleRemoteUserService remoteUserService;

    @Bean("MyExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("AsyncSendResultLookup-");
        executor.initialize();
        return executor;
    }

    @Async("MyExecutor")
    public void sendUserOrderResult(List<UserOrder> userOrders) throws Exception {
        remoteUserService.sendOrderResultWithExceptionHandle(userOrders);
    }
}
