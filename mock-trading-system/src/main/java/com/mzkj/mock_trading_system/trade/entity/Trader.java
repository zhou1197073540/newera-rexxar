package com.mzkj.mock_trading_system.trade.entity;


import com.mzkj.mock_trading_system.common.consts.Consts;
import com.mzkj.mock_trading_system.remote.RetryAbleRemoteUserService;
import com.mzkj.mock_trading_system.trade.repo.cache.OrderQueue;
import com.mzkj.mock_trading_system.trade.repo.cache.StockSnapShotCache;
import com.mzkj.mock_trading_system.trade.repo.mapper.ow.StockOrderMapper;
import com.mzkj.mock_trading_system.trade.service.UtilService;
import com.mzkj.mock_trading_system.trade.valobj.StockMarketSnapshotPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * create by zhouzhenyang on 2018/7/26
 */
@Service
public class Trader {

    private final
    StockSnapShotCache stockSnapShotCache;
    private final
    OrderQueue orderQueue;
    private final
    RetryAbleRemoteUserService remoteUserService;
    private final
    OrderMatcher orderMatcher;
    private final StockOrderMapper stockOrderMapper;
    private final UtilService utilService;

    private Logger logger = LoggerFactory.getLogger(Trader.class);

    @Autowired
    public Trader(StockSnapShotCache stockSnapShotCache,
                  OrderQueue orderQueue,
                  RetryAbleRemoteUserService remoteUserService,
                  OrderMatcher orderMatcher,
                  StockOrderMapper stockOrderMapper,
                  UtilService utilService) {
        this.stockSnapShotCache = stockSnapShotCache;
        this.orderQueue = orderQueue;
        this.remoteUserService = remoteUserService;
        this.orderMatcher = orderMatcher;
        this.stockOrderMapper = stockOrderMapper;
        this.utilService = utilService;
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void doTrade() throws Exception {
        if (utilService.isTradingTime(LocalDateTime.now())) {
            final List<UserOrder> orders = getOrders();
            final Map<String, StockMarketSnapshotPO> stocks = getStocks();
            final List<UserOrder> finishedOrders = orderMatcher.doMatching(orders, stocks);
            if (finishedOrders != null && !finishedOrders.isEmpty()) {
                doCashBack(finishedOrders);
                logger.info("finishOrders:" + finishedOrders.size());
                informUpStream(finishedOrders);
            } else {
                logger.info("finishOrders:0");
            }
        }
    }

    /**
     * 买 多余的返现
     *
     * @param finishedOrders
     */
    @Async("MyExecutor")
    public void doCashBack(List<UserOrder> finishedOrders) {
        final List<UserOrder> cashBackOrders = finishedOrders.stream()
                .filter(userOrder -> userOrder.getOperation().equals(Consts.OPERATION_BUY))
                .map(this::calCashBack)
                .collect(Collectors.toList());
        //同一账号合并
        if (!cashBackOrders.isEmpty()) {
            Map<String, UserOrder> userOrderMap = new HashMap<>();
            UserOrder tmp = null;
            for (UserOrder userOrder : cashBackOrders) {
                String account = userOrder.getUserAccount();
                if (userOrderMap.containsKey(account)) {
                    tmp = userOrderMap.get(account);
                    tmp.setCashBack(tmp.getCashBack().add(
                            calCashBack(userOrder).getCashBack()
                    ));
                    userOrderMap.put(account, tmp);
                } else {
                    userOrderMap.put(account, calCashBack(userOrder));
                }
            }
            stockOrderMapper.doUpdateUserCashBack(new ArrayList<>(userOrderMap.values()));
        }
    }

    private UserOrder calCashBack(UserOrder userOrder) {
        BigDecimal price = new BigDecimal(userOrder.getPrice());
        BigDecimal tradePrice = new BigDecimal(userOrder.getTradePrice());

        final BigDecimal subtract = price.subtract(tradePrice);
        final BigDecimal cashBack = subtract.multiply(new BigDecimal(userOrder.getAmount()));
        return userOrder.setCashBack(cashBack);
    }

    private void informUpStream(List<UserOrder> finishedOrders) {
        try {
            if (finishedOrders != null && !finishedOrders.isEmpty()) {
                remoteUserService.sendOrderResultWithExceptionHandle(finishedOrders);
            }
        } catch (Exception e) {
            logger.error("informUpStream throws Exception ", e);
        }
    }

    private List<UserOrder> getOrders() {
        final List<UserOrder> all = orderQueue.getAll();
        return all;
    }

    private Map<String, StockMarketSnapshotPO> getStocks() {
        return stockSnapShotCache.getAllAsMap();
    }
}
