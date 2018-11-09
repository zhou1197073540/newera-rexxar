package com.mzkj.mock_trading_system.receive.service;

import com.mzkj.mock_trading_system.receive.valobj.OrderContext;
import com.mzkj.mock_trading_system.trade.entity.OrderMatcher;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import com.mzkj.mock_trading_system.trade.repo.cache.OrderQueue;
import com.mzkj.mock_trading_system.trade.repo.cache.StockSnapShotCache;
import com.mzkj.mock_trading_system.trade.valobj.StockMarketSnapshotPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.mzkj.mock_trading_system.common.consts.Consts.OPERATION_SELL;
import static com.mzkj.mock_trading_system.common.consts.Message.SUCCESS;
import static com.mzkj.mock_trading_system.common.consts.Message.UN_SUPPORTED_STOCK;

/**
 * create by zhouzhenyang on 2018/7/31
 */
@Component(OPERATION_SELL)
public class SellHandler extends OrderOperationHandler {

    @Autowired
    OrderQueue orderQueue;
    @Autowired
    StockSnapShotCache stockSnapShotCache;
    @Autowired
    AsyncSendResultService asyncSendResultService;
    @Autowired
    OrderMatcher orderMatcher;

    @Override
    public void process(OrderContext orderContext) throws Exception {
        UserOrder userOrder = orderContext.getUserOrder();
        final StockMarketSnapshotPO snapshotPO
                = stockSnapShotCache.getByCode(userOrder.getFullCode());
        if (snapshotPO != null) {
            //是否符合立即成交规则
            if (orderMatcher.tryImmediateSell(snapshotPO, userOrder)) {
                createFinishOrder(userOrder);
                final List<UserOrder> userOrders = Collections.singletonList(userOrder);
                asyncSendResultService
                        .sendUserOrderResult(userOrders);
            } else {
                //等待成交
                createOrder(userOrder);
                orderQueue.add(orderContext.getUserOrder());
            }
            orderContext.setStatusCode(SUCCESS.statusCode).setMsg(SUCCESS.message);
        } else {
            orderContext.setStatusCode(UN_SUPPORTED_STOCK.statusCode).setMsg(UN_SUPPORTED_STOCK.message);
        }
    }
}
