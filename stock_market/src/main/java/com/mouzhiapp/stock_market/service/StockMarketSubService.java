package com.mouzhiapp.stock_market.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mouzhiapp.stock_market.bean.TimelineDot;
import com.mouzhiapp.stock_market.consts.Consts;
import com.mouzhiapp.stock_market.controller.VO.StockBasicIndexesVO;
import com.mouzhiapp.stock_market.repo.PO.StockMarketSnapshotPO;
import com.mouzhiapp.stock_market.repo.cache.StockMarketSnapShotCache;
import com.mouzhiapp.stock_market.repo.mapper.standBy.StockMarketMinStandByMapper;
import com.mouzhiapp.stock_market.repo.mapper.standBy.StockMarketOriginStandByMapper;
import com.mouzhiapp.stock_market.repo.mapper205.StockInfoUtilMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * create by zhouzhenyang on 2018/6/14
 */
@Service
public class StockMarketSubService {

    @Autowired
    StockMarketOriginStandByMapper originMapper;
    @Autowired
    StockMarketMinStandByMapper minMapper;
    @Autowired
    StockInfoUtilMapper infoUtilMapper;
    @Autowired
    StockUtilService utilService;
    @Autowired
    StockMarketSnapShotCache snapShotCache;
    @Autowired
    ObjectMapper jacksonMapper;

    Logger errLogger = LoggerFactory.getLogger(Consts.ERROR_LOGGER);


    /**
     * 按照code获取stock基本指标
     *
     * @param code
     * @return
     * @throws Exception
     */
    public StockBasicIndexesVO getStockBasicIndexesByCode(String code) throws Exception {
        StockMarketSnapshotPO stockMarketSnapshot = snapShotCache.getByCode(code);
        //停盘判断
        if (utilService.isSuspension(stockMarketSnapshot)) {
            return new StockBasicIndexesVO().setStatus(Consts.SUSPENSION);
        }
        StockBasicIndexesVO stockIndexes = new StockBasicIndexesVO();
        BeanUtils.copyProperties(stockMarketSnapshot, stockIndexes);
        stockIndexes.setDatetime(utilService.getDateTime(stockMarketSnapshot.getDate()
                , stockMarketSnapshot.getTime(), " "));
        if (Double.valueOf(stockIndexes.getOpen()) == 0) {
            return stockIndexes;
        }
        Map<String, Object> indexMap = infoUtilMapper.doGetLatestStockIndexByCode(code);
        System.out.println(indexMap);
        if (indexMap == null || indexMap.isEmpty()) {
            return stockIndexes.setPb("0").setPe("0").setTurnoverRatio("0");
        }
        //per,pb,turnoverratio as turnoverRatio
        String turnoverratio = null;
        try {
            turnoverratio = String.format("%.5f", indexMap.get("turnoverratio"));
        } catch (Exception e) {
            turnoverratio = "0";
        }
        stockIndexes.setPb(String.valueOf(indexMap.get("pb")))
                .setPe(String.valueOf(indexMap.get("pe")))
                .setTurnoverRatio(turnoverratio);
        return stockIndexes;
    }

    public StockMarketSnapshotPO getStockMarketSnapShotByCode(String code) throws Exception {
        StockMarketSnapshotPO stockMarketSnapshot = snapShotCache.getByCode(code);
        return stockMarketSnapshot;
    }

    public String getTimelineByTableAndCode(String tableName, LocalDateTime dateTime, String code, Double lastClose) throws Exception {
        String time = dateTime.toString();
        List<TimelineDot> rets = minMapper.doGetTimeLineByCode(tableName, code, time);
        return rets.stream()
                .map(dot -> TimelineDotToString(dot, lastClose))
                .collect(Collectors.joining(","));
    }

    private String TimelineDotToString(TimelineDot dot, Double lastClose) {
        StringBuilder sb = new StringBuilder(4096);
        sb.append(String.valueOf(dot.getPrice())).append("|")
                .append(dot.getVolume().replaceAll("\\..*", "")).append("|");
        if (lastClose == 0) {
            sb.append(String.valueOf(dot.getPrice())).append("|");
        } else {
            sb.append(String.format("%.5f", (dot.getPrice() - lastClose) / lastClose)).append("|");
        }
        sb.append(dot.getDatetime());
        return sb.toString();
    }

    private static List<String> fullCodes = new ArrayList<>(8192);

    public String getRandomStockFullCode() {
        if (fullCodes.isEmpty()) {
            fullCodes = snapShotCache.getKeys();
        }
        Random random = new Random();
        final int i = random.nextInt(fullCodes.size() - 1);
        return fullCodes.get(i);
    }
}
