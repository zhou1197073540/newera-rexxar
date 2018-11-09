package com.mzkj.mock_trading_system.trade.entity;

import com.mzkj.mock_trading_system.common.consts.Consts;
import com.mzkj.mock_trading_system.trade.repo.cache.OidLock;
import com.mzkj.mock_trading_system.trade.repo.cache.OrderQueue;
import com.mzkj.mock_trading_system.trade.repo.mapper.ow.StockOrderMapper;
import com.mzkj.mock_trading_system.trade.service.UtilService;
import com.mzkj.mock_trading_system.trade.valobj.MockStockUserOrderPO;
import com.mzkj.mock_trading_system.trade.valobj.StockMarketSnapshotPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mzkj.mock_trading_system.common.consts.Consts.CANCEL;

/**
 * create by zhouzhenyang on 2018/8/16
 */
@Service
public class OrderMatcher {

    @Autowired
    private OidLock oidLock;
    @Autowired
    private OrderQueue orderQueue;
    @Autowired
    private StockOrderMapper stockOrderMapper;
    @Autowired
    private UtilService utilService;

    Logger logger = LoggerFactory.getLogger(OrderMatcher.class);

//    preMatch

    public List<UserOrder> doMatching(List<UserOrder> userOrders, Map<String, StockMarketSnapshotPO> stocks) throws Exception {
        logger.info(LocalDateTime.now().toString() + " match start");
        if (userOrders == null || userOrders.isEmpty() || stocks == null || stocks.isEmpty()) {
            logger.info(LocalDateTime.now().toString() + " match empty end");
            return userOrders;
        }
        List<UserOrder> tradeOrders = new ArrayList<>();
        List<UserOrder> unTradeOrders = new ArrayList<>();
        for (UserOrder userOrder : userOrders) {
            if (userOrder.getOperation().equals(Consts.OPERATION_SELL)) {
                sell(userOrder, stocks, tradeOrders, unTradeOrders);
            } else if (userOrder.getOperation().equals(Consts.OPERATION_BUY)) {
                buy(userOrder, stocks, tradeOrders, unTradeOrders);
            }
        }
        List<UserOrder> finishedOrders = null;
        if (!tradeOrders.isEmpty()) {
            finishedOrders = dealTradeOrders(tradeOrders);
        }

        if (!unTradeOrders.isEmpty()) {
            dealUnTradeOrders(unTradeOrders);
        }
        logger.info(LocalDateTime.now().toString() + " match end");
        return finishedOrders;
    }

    public boolean tryImmediateBuy(
            StockMarketSnapshotPO snapshotPO,
            UserOrder userOrder) throws Exception {
        if (!utilService.isTradingTime(LocalDateTime.now())) return false;
        if (isTradeAble(snapshotPO, Consts.OPERATION_BUY)) {
            BigDecimal ask1 = new BigDecimal(snapshotPO.getAsk1());
            //立即成交
            if (ask1.compareTo(new BigDecimal(userOrder.getPrice())) <= 0) {
                userOrder.setTradePrice(snapshotPO.getAsk1());
                return true;
            }
        }
        return false;
    }

    public boolean tryImmediateSell(StockMarketSnapshotPO snapshotPO,
                                    UserOrder userOrder) throws Exception {
        if (!utilService.isTradingTime(LocalDateTime.now())) return false;
        if (isTradeAble(snapshotPO, Consts.OPERATION_SELL)) {
            BigDecimal bid1 = new BigDecimal(snapshotPO.getBid1());
            //立即成交
            if (bid1.compareTo(new BigDecimal(userOrder.getPrice())) >= 0) {
                userOrder.setTradePrice(snapshotPO.getBid1());
                return true;
            }
        }
        return false;
    }

    private void sell(UserOrder order, Map<String, StockMarketSnapshotPO> stocks
            , List<UserOrder> tradeOrders, List<UserOrder> unTradeOrders) {
        StockMarketSnapshotPO snapshotPO = stocks.get(order.getFullCode());
        if (snapshotPO != null) {
            if (isTradeAble(snapshotPO, Consts.OPERATION_SELL)) {
                BigDecimal bid1 = new BigDecimal(snapshotPO.getBid1());
                if (bid1.compareTo(new BigDecimal(order.getPrice())) >= 0) {
                    //等待成交都按照price
                    order.setTradePrice(order.getPrice());
                    tradeOrders.add(order);
                } else {
                    unTradeOrders.add(order);
                }
                // 无人可以交易
            } else {
                unTradeOrders.add(order);
            }
        }
    }

    private void buy(UserOrder order, Map<String, StockMarketSnapshotPO> stocks
            , List<UserOrder> updateOrders, List<UserOrder> unUpdateOrders) {
        StockMarketSnapshotPO snapshotPO = stocks.get(order.getFullCode());
        if (snapshotPO != null) {
            if (isTradeAble(snapshotPO, Consts.OPERATION_BUY)) {
                BigDecimal ask1 = new BigDecimal(snapshotPO.getAsk1());
                if (ask1.compareTo(new BigDecimal(order.getPrice())) <= 0) {
                    //等待成交都按照price
                    order.setTradePrice(order.getPrice());
                    updateOrders.add(order);
                } else {
                    unUpdateOrders.add(order);
                }
                // 无人可以交易
            } else {
                unUpdateOrders.add(order);
            }
        }
    }


    private static BigDecimal V100 = new BigDecimal(100);

    /**
     * 涨跌停不能交易
     *
     * @param snapshotPO
     * @return
     */
    private boolean isTradeAble(StockMarketSnapshotPO snapshotPO, String operation) {
        BigDecimal now = new BigDecimal(snapshotPO.getNow());
        BigDecimal open = new BigDecimal(snapshotPO.getOpen());
        BigDecimal bid1 = new BigDecimal(snapshotPO.getBid1());
        BigDecimal ask1 = new BigDecimal(snapshotPO.getAsk1());
        if (Consts.OPERATION_SELL.equals(operation)) {
            if (open.subtract(now).divide(V100).doubleValue() >= 0.1) {
                return false;
            }
            if (bid1.compareTo(BigDecimal.ZERO) == 0) {
                return false;
            }
        } else if (Consts.OPERATION_BUY.equals(operation)) {
            if (now.subtract(open).divide(V100).doubleValue() >= 0.1) {
                return false;
            }
            if (ask1.compareTo(BigDecimal.ZERO) == 0) {
                return false;
            }
        }
        return true;
    }

    private void dealUnTradeOrders(List<UserOrder> unTradeOrders) {
        final List<UserOrder> unCanceledOrders = getUnCanceledOrders(unTradeOrders);
        unCanceledOrders.forEach(userOrder -> orderQueue.add(userOrder));
    }


    private List<UserOrder> getUnCanceledOrders(List<UserOrder> userOrders) {
        final Map<String, String> orderStatuses = stockOrderMapper.doGetOrderStatusByOids(userOrders)
                .stream()
                .collect(Collectors
                        .toMap(MockStockUserOrderPO::getOid, MockStockUserOrderPO::getOrderStatus));

        final List<UserOrder> collect = userOrders.stream()
                .filter(userOrder -> {
                    String status = orderStatuses.getOrDefault(userOrder.getOid(), "-1");
                    return !status.equals("-1") && !status.equals(CANCEL);
                }).collect(Collectors.toList());
        return collect;

    }


    private List<UserOrder> dealTradeOrders(List<UserOrder> tradeOrders) {
        try {
            final List<UserOrder> userOrderWithLocks = getUserOrderWithLocks(tradeOrders);
            final List<UserOrder> unCanceledOrders = getUnCanceledOrders(userOrderWithLocks);
            if (unCanceledOrders != null && !unCanceledOrders.isEmpty()) {
                String tradeTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                stockOrderMapper.doUpdateTradeOrderByOids(unCanceledOrders, Consts.FINISH, tradeTime);
            }
            return unCanceledOrders;
        } finally {
            releaseUserOrderLocks(tradeOrders);
        }
    }

    private List<UserOrder> getUserOrderWithLocks(List<UserOrder> userOrders) {
        final List<UserOrder> collect = userOrders.stream()
                .filter(userOrder -> oidLock.tryLock(userOrder))
                .collect(Collectors.toList());
        return collect;
    }

    private void releaseUserOrderLocks(List<UserOrder> userOrders) {
        userOrders.forEach(userOrder -> oidLock.unlock(userOrder));
    }


//    postMatch
}
