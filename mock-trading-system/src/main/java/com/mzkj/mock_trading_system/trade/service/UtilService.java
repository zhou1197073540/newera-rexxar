package com.mzkj.mock_trading_system.trade.service;

import com.mzkj.mock_trading_system.common.consts.Consts;
import com.mzkj.mock_trading_system.trade.repo.mapper.stock_data.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * create by zhouzhenyang on 2018/7/29
 */
@Repository
public class UtilService {

    @Autowired
    StockMapper stockMapper;

    public boolean isTradingTime(LocalDateTime now) {
        if (!isTradingDate(now)) return false;
        LocalTime time = now.toLocalTime();
        if (time.isAfter(Consts.v0930) && time.isBefore(Consts.v1130)) {
            return true;
        } else if (time.isAfter(Consts.v1300) && time.isBefore(Consts.v1500)) {
            return true;
        }
        return false;
    }

    public boolean isAvailable(LocalDateTime now) {
        if (!isTradingDate(now)) {
            return true;
        }
        LocalTime time = now.toLocalTime();
        return !time.isAfter(Consts.v1500) || !time.isBefore(Consts.v1800);
    }

    public boolean isTradingDate(LocalDateTime now) {
        try {
            if (stockMapper.doCheckIsTradingDate(now.toLocalDate().toString())
                    == null) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }
}
