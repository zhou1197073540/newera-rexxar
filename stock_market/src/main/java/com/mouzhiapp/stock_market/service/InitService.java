package com.mouzhiapp.stock_market.service;

/**
 * create by zhouzhenyang on 2018/6/19
 */

import com.mouzhiapp.stock_market.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.xml.ws.soap.Addressing;

@Service
public class InitService {

    @Autowired
    RedisUtil redisUtil;

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        redisUtil.init();
    }
}
