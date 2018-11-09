package com.mouzhiapp.stock_market.service;

import com.mouzhiapp.stock_market.bean.CalculateChangeAndChangeP;
import com.mouzhiapp.stock_market.consts.Consts;
import com.mouzhiapp.stock_market.repo.PO.StockMarketSnapshotPO;
import com.mouzhiapp.stock_market.repo.mapper.standBy.StockMarketUtilsStandByMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

import static java.math.BigDecimal.ROUND_UP;
import static java.math.RoundingMode.UNNECESSARY;

/**
 * create by zhouzhenyang on 2018/6/9
 */
@Service
public class StockUtilService {

    @Autowired
    StockMarketUtilsStandByMapper utilsMapper;

    public <T extends CalculateChangeAndChangeP> T setChangeAndChangeP(T t) {
        BigDecimal now = new BigDecimal(t.getNow());
        BigDecimal yclose = new BigDecimal(
                t.getClose());
        BigDecimal change = now.subtract(yclose).setScale(5, UNNECESSARY);
        BigDecimal changeP =
                (yclose.equals(BigDecimal.ZERO)) ? BigDecimal.ONE :
                        change.divide(yclose, MathContext.DECIMAL32).setScale(5, ROUND_UP);
        t.setChange(change.toString());
        t.setChangeP(changeP.toString());
        return t;
    }

    public boolean isBeforeTradingTime() {
        return LocalTime.now().isBefore(LocalTime.of(9, 0));
    }

    public boolean isTradingTime() {
        return LocalTime.now().isAfter(LocalTime.of(9, 30));
    }

    public boolean isTradingDate(String day) throws Exception {
        if (utilsMapper.doCheckIsTradingDate(day) != null) {
            return true;
        }
        return false;
    }


    /**
     * 获取上个交易日日期
     *
     * @param format
     * @return
     * @throws Exception
     */
    public String getLastTradingDate(String format) throws Exception {
        String lastTradingDay = utilsMapper.doGetLastTradingDate(LocalDate.now().toString());
        return LocalDate.parse(lastTradingDay,
                DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofPattern(format));
    }


    public String getDateTimeUnix(String date, String time) {
        final LocalDateTime dateTime = LocalDateTime.parse(date + " " + time,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.valueOf(dateTime.toEpochSecond(ZoneOffset.ofHours(8)));
    }

    public String getDateTime(String date, String time, String sep) {
        return date + sep + time;
    }

    public boolean isSuspension(StockMarketSnapshotPO snapshotPO) {
        if (snapshotPO.getStatus() != null &&
                snapshotPO.getStatus().equals(Consts.SUSPENSION)) {
            return true;
        }
        return false;
    }


    public boolean isSameWeek(LocalDate date, LocalDate date1) {
        WeekFields weekFields = WeekFields.of(Locale.CHINESE);
        return date.get(weekFields.weekOfWeekBasedYear()) ==
                date1.get(weekFields.weekOfWeekBasedYear());

    }
}

