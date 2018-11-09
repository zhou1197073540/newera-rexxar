package com.mzkj.xiaoxin.service;

import com.mzkj.xiaoxin.classifier.PO.StockAnswerRoot;
import com.mzkj.xiaoxin.consts.Consts;
import com.mzkj.xiaoxin.consts.Message;
import com.mzkj.xiaoxin.controller.VO.QandAVO;
import com.mzkj.xiaoxin.dto.RespDto;
import com.mzkj.xiaoxin.remote.StockMarketService;
import com.mzkj.xiaoxin.repo.PO.StockBasicInfoPO;
import com.mzkj.xiaoxin.repo.PO.StockMarketSnapshotPO;
import com.mzkj.xiaoxin.repo.PO.ZhengguPO;
import com.mzkj.xiaoxin.repo.cache.StockMarketSnapShotCache;
import com.mzkj.xiaoxin.repo.mapper205.ZhengguMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 * create by zhouzhenyang on 2018/9/18
 */
@Service
public class StockService {


    private final StockMarketSnapShotCache snapShotCache;
    private final StockMarketService stockMarketService;
    private final ZhengguMapper zhengguMapper;

    @Autowired
    public StockService(StockMarketSnapShotCache snapShotCache, StockMarketService stockMarketService, ZhengguMapper zhengguMapper) {
        this.snapShotCache = snapShotCache;
        this.stockMarketService = stockMarketService;
        this.zhengguMapper = zhengguMapper;
    }


    public QandAVO<?> getStockAnswer(String fullCode) throws Exception {
        Set<String> codes = snapShotCache.getFullCodes();
        if (codes.contains(fullCode)) {
            final StockMarketSnapshotPO stockMarketSnapshot
                    = snapShotCache.getByCode(fullCode);
            final RespDto<Map<String, Object>> stockTimeline
                    = stockMarketService.getStockTimeline(fullCode);
            final Object timeLine = stockTimeline.getData().get(Consts.TIME_LINE);
            final ZhengguPO zhenggu = getZhenggu(fullCode.replaceAll("sh|sz", ""));
            StockBasicInfoPO stockBasicInfo = new StockBasicInfoPO();
            BeanUtils.copyProperties(stockMarketSnapshot, stockBasicInfo);
            return new QandAVO<>(
                    Message.STOCK.type,
                    new StockAnswerRoot(
                            stockBasicInfo,
                            timeLine,
                            zhenggu
                    ));
        }
        return new QandAVO<>(Message.UN_KNOWN.type, Message.UN_KNOWN.message);
    }

    private ZhengguPO getZhenggu(String ticker) {
        final ZhengguPO zhenggu = zhengguMapper.getLatestZhengguByTicker(ticker);
        if (zhenggu == null) {
            return new ZhengguPO();
        }
        zhenggu.setAlpha(zhenggu.getAlpha().setScale(4, BigDecimal.ROUND_UP));
        zhenggu.setCostPrice(zhenggu.getCostPrice().setScale(4, BigDecimal.ROUND_UP));
        zhenggu.setRate5(zhenggu.getRate5().setScale(4, BigDecimal.ROUND_UP));
        zhenggu.setSupport(zhenggu.getSupport().setScale(4, BigDecimal.ROUND_UP));
        zhenggu.setResistance(zhenggu.getResistance().setScale(4, BigDecimal.ROUND_UP));
        return zhenggu;

    }

}
