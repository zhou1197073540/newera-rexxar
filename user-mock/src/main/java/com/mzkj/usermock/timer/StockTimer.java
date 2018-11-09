package com.mzkj.usermock.timer;

import com.mzkj.usermock.service.MockService;
import com.mzkj.usermock.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalTime;

@Component
public class StockTimer {
    @Autowired
    MockService mockService;

    @Scheduled(cron="0/20 30 23 * * MON-FRI")
    public void job1() {
//        mockService.updateAvaiPosition();
        mockService.updateAllAvaiPosition();
        mockService.deleteInvalidStock();
        System.out.println("执行可用持仓定时器..."+TimeUtil.getDateTime());
    }
}
