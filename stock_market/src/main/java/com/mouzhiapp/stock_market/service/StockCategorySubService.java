package com.mouzhiapp.stock_market.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mouzhiapp.stock_market.bean.CategoryStock;
import com.mouzhiapp.stock_market.bean.StockIndex;
import com.mouzhiapp.stock_market.repo.PO.StockMarketSnapshotPO;
import com.mouzhiapp.stock_market.repo.cache.StockMarketSnapShotCache;
import com.mouzhiapp.stock_market.repo.mapper.master.StockMarketUtilsMasterMapper;
import com.mouzhiapp.stock_market.repo.mapper.standBy.StockMarketOriginStandByMapper;
import com.mouzhiapp.stock_market.repo.mapper.standBy.StockMarketUtilsStandByMapper;
import com.mouzhiapp.stock_market.repo.mapper205.StockInfoUtilMapper;
import com.mouzhiapp.stock_market.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mouzhiapp.stock_market.consts.Consts.DATA;
import static com.mouzhiapp.stock_market.consts.Consts.RISE_AND_FALL;

/**
 * create by zhouzhenyang on 2018/6/4
 */
@Service
public class StockCategorySubService {

    @Autowired
    StockMarketUtilsStandByMapper marketUtilsStandByMapper;
    @Autowired
    StockMarketUtilsMasterMapper marketUtilsMasterMapper;
    @Autowired
    StockInfoUtilMapper infoUtilMapper;
    @Autowired
    StockMarketOriginStandByMapper originMapper;
    @Autowired
    StockUtilService utilService;
    @Autowired
    StockMarketSnapShotCache snapShotCache;

    @Autowired
    ObjectMapper jacksonMapper;

    private CategoryStock toCategoryStock(StockMarketSnapshotPO snapshot) {
        try {
            CategoryStock categoryStock = new CategoryStock();
            BeanUtils.copyProperties(categoryStock, snapshot);
            categoryStock.setDatetime(utilService.getDateTime(snapshot.getDate(), snapshot.getTime(), " "));
            return categoryStock;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isTradingTime() {
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.of(9, 30))
                || now.isAfter(LocalTime.of(15, 2))) {
            return false;
        } else if (now.isAfter(LocalTime.of(11, 30))
                && now.isBefore(LocalTime.of(13, 0))) {
            return false;
        }
        return true;
    }

    public List<StockIndex> getIndexes() throws Exception {
        final List<StockMarketSnapshotPO> indexes = snapShotCache.getIndexes();
        final List<StockIndex> collect =
                indexes.stream().map(this::toStockIndex)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
        return collect;
    }

    private StockIndex toStockIndex(StockMarketSnapshotPO snapshotPO) {
        try {
            StockIndex stockIndex = new StockIndex();
            BeanUtils.copyProperties(snapshotPO, stockIndex);
            stockIndex = utilService.setChangeAndChangeP(stockIndex);
            return stockIndex;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getRiseAndFall() throws Exception {
        try (Jedis jedis = RedisUtil.getJedis()) {
            String raf = "";
            if (jedis != null) {
                raf = jedis.hget(RISE_AND_FALL, DATA);
            }
            return raf;
        }
    }
}
