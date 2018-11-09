package com.mzkj.xiaoxin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * create by zhouzhenyang on 2018/9/18
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients(basePackages = "com.mzkj.xiaoxin.remote")
public class XiaoxinApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoxinApplication.class, args);
    }
}
