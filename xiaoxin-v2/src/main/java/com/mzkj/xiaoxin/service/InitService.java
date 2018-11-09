package com.mzkj.xiaoxin.service;

/**
 * create by zhouzhenyang on 2018/6/19
 */

import com.mzkj.xiaoxin.classifier.WordExtractor;
import com.mzkj.xiaoxin.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class InitService {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    WordExtractor wordExtractor;

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        redisUtil.init();
        wordExtractor.init();
    }
}
