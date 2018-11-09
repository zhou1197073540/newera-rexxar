package com.mouzhiapp.stock_market.service;

import com.google.common.collect.ImmutableMap;
import com.mouzhiapp.stock_market.bean.UserSelectedStock;
import com.mouzhiapp.stock_market.consts.Consts;
import com.mouzhiapp.stock_market.controller.VO.StockBasicIndexesVO;
import com.mouzhiapp.stock_market.controller.VO.StockSnapShotForMockVO;
import com.mouzhiapp.stock_market.controller.VO.StockSnapShotWithDayKline;
import com.mouzhiapp.stock_market.exception.DateFormatServiceException;
import com.mouzhiapp.stock_market.repo.PO.BidAndAskPO;
import com.mouzhiapp.stock_market.repo.PO.KLineWithLimitDate;
import com.mouzhiapp.stock_market.repo.PO.LineDotPO;
import com.mouzhiapp.stock_market.repo.PO.StockMarketSnapshotPO;
import com.mouzhiapp.stock_market.repo.mapper.standBy.StockMarketDayStandByMapper;
import com.mouzhiapp.stock_market.repo.mapper.standBy.StockMarketMinStandByMapper;
import com.mouzhiapp.stock_market.repo.mapper.standBy.StockMarketWeekStandByMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mouzhiapp.stock_market.consts.Consts.BID_AND_ASK;
import static com.mouzhiapp.stock_market.consts.Consts.TIME_LINE;

/**
 * create by zhouzhenyang on 2018/5/22
 */
@Service
public class StockMarketService {

    private final
    StockMarketMinStandByMapper minMapper;
    private final
    StockMarketDayStandByMapper dayMapper;
    private final
    StockUtilService utilService;
    private final
    StockMarketSubService marketSubService;
    private final StockMarketWeekStandByMapper weekMapper;

    @Autowired
    public StockMarketService(StockMarketMinStandByMapper minMapper,
                              StockMarketDayStandByMapper dayMapper,
                              StockUtilService utilService,
                              StockMarketSubService marketSubService, StockMarketWeekStandByMapper weekMapper) {
        this.minMapper = minMapper;
        this.dayMapper = dayMapper;
        this.utilService = utilService;
        this.marketSubService = marketSubService;
        this.weekMapper = weekMapper;
    }


    /**
     * 获取最新分时图+买5卖5数据
     *
     * @param code
     * @return
     * @throws Exception
     */
    public Map<String, Object> getTimeline(String code) throws Exception {
        final StockMarketSnapshotPO snapShot = marketSubService.getStockMarketSnapShotByCode(code);
        if (snapShot == null) {
            return ImmutableMap.of();
        }
        BidAndAskPO hehe = new BidAndAskPO();
        BeanUtils.copyProperties(snapShot, hehe);
        String minTable =
                Consts.MINUTE + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 30));
        String timeline =
                marketSubService.getTimelineByTableAndCode(minTable, dateTime, code, Double.valueOf(hehe.getClose()));
        return ImmutableMap.of(BID_AND_ASK, hehe, TIME_LINE, timeline);
    }

    /**
     * 获取上一个交易日分时图+买5卖5数据
     *
     * @param code
     * @return
     * @throws Exception
     */
    public Map<String, Object> getLastTradingDayTimeline(String code) throws Exception {
        String lastTradingDate = utilService.getLastTradingDate("yyyyMMdd");
        final StockMarketSnapshotPO snapShot = marketSubService.getStockMarketSnapShotByCode(code);
        if (snapShot == null) {
            return ImmutableMap.of();
        }
        BidAndAskPO hehe = new BidAndAskPO();
        BeanUtils.copyProperties(snapShot, hehe);
        String minTable =
                Consts.MINUTE + lastTradingDate;
        LocalDateTime dateTime =
                LocalDateTime.of(LocalDate.parse(lastTradingDate, DateTimeFormatter.ofPattern("yyyyMMdd")), LocalTime.of(9, 30));
        String timeline = marketSubService.getTimelineByTableAndCode(minTable, dateTime, code, Double.valueOf(hehe.getClose()));
        return ImmutableMap.of(BID_AND_ASK, hehe, TIME_LINE, timeline);
    }

    public String getDayKLine(String code, String startDate, String endDate) throws Exception {
        return getDayKLineWithLimitedDots(code, startDate, endDate, 0).getKLine();
    }

    public String getWeekKLine(String code, String startDate, String endDate) throws Exception {
        CheckDateFormat(startDate, endDate);
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        List<LineDotPO> lineDots =
                weekMapper.doGetWeekKLineByCodeAndDate(code, start, end);
        if (lineDots == null || lineDots.isEmpty()) {
            return null;
        }
        LocalDate retLastDate = lineDots.get(lineDots.size() - 1).getDatetime().toLocalDate();
        if (needToday(retLastDate, endDate)) {
            LineDotPO latestDayKlineDot = getLatestDayKlineDot(code);
            if (utilService.isSameWeek(LocalDate.now(), retLastDate)) {
                LineDotPO lastLineDot = lineDots.remove(lineDots.size() - 1);
                lastLineDot = weekCombine(lastLineDot, latestDayKlineDot);
                lineDots.add(lastLineDot);
            } else {
                lineDots.add(latestDayKlineDot);
            }
        }
        String weekKline = lineDots
                .stream().map(this::KlineDotsToString)
                .collect(Collectors.joining(","));
        return weekKline;
    }

    private LineDotPO weekCombine(LineDotPO lastLineDot, LineDotPO latestDayKlineDot) {
        if (Float.valueOf(latestDayKlineDot.getHigh()) > Float.valueOf(lastLineDot.getHigh())) {
            lastLineDot.setHigh(latestDayKlineDot.getHigh());
        }
        if (Float.valueOf(latestDayKlineDot.getLow()) < Float.valueOf(lastLineDot.getLow())) {
            lastLineDot.setHigh(latestDayKlineDot.getHigh());
        }
        lastLineDot.setClose(latestDayKlineDot.getClose());
        lastLineDot.setVolume(
                new BigDecimal(lastLineDot.getVolume())
                        .add(new BigDecimal(latestDayKlineDot.getVolume()))
                        .toString()
        );
        return lastLineDot;
    }

    public KLineWithLimitDate getDayKLineWithLimitedDots(String code, String startDate,
                                                         String endDate, int count) throws Exception {
        CheckDateFormat(startDate, endDate);
        String table = Consts.DAY + code;
        List<LineDotPO> lineDots = dayMapper.doGetDayKLineByCodeAndDate(table, startDate, endDate);
        if (lineDots == null || lineDots.isEmpty()) {
            return null;
        }
        LocalDate retLastDate = lineDots.get(lineDots.size() - 1).getDatetime().toLocalDate();
        if (needToday(retLastDate, endDate)) {
            lineDots.add(getLatestDayKlineDot(code));
        }
        String dayKline = null;
        LocalDateTime limitEndDate = null;
        LocalDateTime limitStartDate = null;
        if (count != 0) {
            if (lineDots.size() < 60) {
                return null;
            }
            List<LineDotPO> retList = lineDots
                    .subList(lineDots.size() - count, lineDots.size() - 1);
            dayKline = retList
                    .stream().map(this::KlineDotsToString)
                    .collect(Collectors.joining(","));
            limitEndDate = retList.get(retList.size() - 1).getDatetime();
            limitStartDate = retList.get(0).getDatetime();
        } else {
            dayKline = lineDots.stream().map(this::KlineDotsToString)
                    .collect(Collectors.joining(","));
            limitEndDate = lineDots.get(lineDots.size() - 1).getDatetime();
            limitStartDate = lineDots.get(0).getDatetime();
        }
        return new KLineWithLimitDate(limitStartDate, limitEndDate, dayKline);
    }

    private LineDotPO getLatestDayKlineDot(String code) {
        String minTable = Consts.MINUTE + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return minMapper.doGetLatestDayKlineDotByCode(minTable, code);
    }

    /**
     * 在交易过程中, 获取时间段上限>=当前时间
     *
     * @param retLastDate
     * @param end
     * @return
     * @throws Exception
     */
    private boolean needToday(LocalDate retLastDate, String end) throws Exception {
        if (!utilService.isTradingDate(LocalDate.now().toString())) {
            return false;
        }
        if (!utilService.isTradingTime()) {
            return false;
        }
        LocalDate searchEndDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate today = LocalDate.now();
        if ((searchEndDate.isAfter(today) || searchEndDate.isEqual(today))
                && retLastDate.isBefore(today)) {
            return true;
        }
        return false;
    }

    private void CheckDateFormat(String start, String end) {
        final DateTimeFormatter yyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        try {
            yyyMMdd.parse(start);
            yyyMMdd.parse(end);
        } catch (DateTimeParseException e) {
            throw new DateFormatServiceException(e.getParsedString(), e.getMessage(), e);
        }
    }

    private String KlineDotsToString(LineDotPO dot) {
        return String.valueOf(dot.getOpen()) +
                "|" +
                String.valueOf(dot.getClose()) + "|" +
                String.valueOf(dot.getHigh()) + "|" +
                String.valueOf(dot.getLow()) + "|" +
                dot.getVolume().replaceAll("\\..*", "") + "|" +
                String.valueOf(dot.getDatetime().toEpochSecond(ZoneOffset.ofHours(8)));
    }

    /**
     * 获取最新股票基本指标
     *
     * @param code
     * @return
     * @throws Exception
     */
    public UserSelectedStock getLatestStockBasicIndexes(String code) throws Exception {
        StockBasicIndexesVO stockIndexes =
                marketSubService.getStockBasicIndexesByCode(code);
        return new UserSelectedStock(stockIndexes);
    }

    public StockSnapShotForMockVO getStockSnapShotForMock(String code) throws Exception {
        final StockMarketSnapshotPO stockMarketSnapshotPO = marketSubService.getStockMarketSnapShotByCode(code);
        StockSnapShotForMockVO stockSnapShotForMockVO = new StockSnapShotForMockVO();
        BeanUtils.copyProperties(stockMarketSnapshotPO, stockSnapShotForMockVO);
        utilService.setChangeAndChangeP(stockSnapShotForMockVO);
        return stockSnapShotForMockVO;
    }

    public StockSnapShotWithDayKline getStockForKlineTraining(String fullCode) throws Exception {
        for (int i = 0; i < 3; i++) {
            if (fullCode == null) {
                fullCode = marketSubService.getRandomStockFullCode();
            }
            String end = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String start = LocalDate.now().minusMonths(6).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            final KLineWithLimitDate dayKLineWithLimitedDots = getDayKLineWithLimitedDots(fullCode, start, end, 60);
            if (dayKLineWithLimitedDots != null) {
                final StockMarketSnapshotPO snapshotPO = marketSubService.getStockMarketSnapShotByCode(fullCode);

                StockSnapShotWithDayKline vo = new StockSnapShotWithDayKline();
                BeanUtils.copyProperties(snapshotPO, vo);
                vo.setDayKLine(dayKLineWithLimitedDots.getKLine())
                        .setStartDate(
                                dayKLineWithLimitedDots
                                        .getLimitStartDate()
                                        .format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                        )
                        .setEndDate(
                                dayKLineWithLimitedDots
                                        .getLimitEndDate()
                                        .format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                        );
                return vo;
            }
        }
        return null;
    }
}
