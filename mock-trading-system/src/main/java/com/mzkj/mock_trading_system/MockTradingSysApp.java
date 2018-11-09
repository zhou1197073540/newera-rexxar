package com.mzkj.mock_trading_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * create by zhouzhenyang on 2018/7/26
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MockTradingSysApp {

    public static void main(String[] args) {
        SpringApplication.run(MockTradingSysApp.class, args);
    }
}
