package com.mzkj.xiaoxin.service;

import com.mzkj.xiaoxin.classifier.WordExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * create by zhouzhenyang on 2018.10.11
 */
@Service
public class TimerService {

    @Autowired
    WordExtractor wordExtractor;

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void doTrade() throws Exception {
        wordExtractor.refreshWords();
    }
}
