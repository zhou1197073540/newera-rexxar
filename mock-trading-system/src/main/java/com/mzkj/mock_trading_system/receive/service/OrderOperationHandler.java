package com.mzkj.mock_trading_system.receive.service;

import com.mzkj.mock_trading_system.receive.valobj.OrderContext;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import com.mzkj.mock_trading_system.trade.repo.mapper.ow.StockOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.mzkj.mock_trading_system.common.consts.Consts.FINISH;
import static com.mzkj.mock_trading_system.common.consts.Consts.WAIT;

/**
 * create by zhouzhenyang on 2018/7/31
 */
public abstract class OrderOperationHandler {

    @Autowired
    StockOrderMapper stockOrderMapper;

    @Transactional
    void createOrder(UserOrder userOrder) throws Exception {
        userOrder.setTradePrice(userOrder.getPrice());
        stockOrderMapper.doCreateOrder(userOrder
                , null
                , WAIT);
    }

    @Transactional
    void createFinishOrder(UserOrder userOrder) throws Exception {
        stockOrderMapper.doCreateOrder(userOrder,
                LocalDateTime.now(),
                FINISH);
    }

    abstract public void process(OrderContext orderContext) throws Exception;
}
