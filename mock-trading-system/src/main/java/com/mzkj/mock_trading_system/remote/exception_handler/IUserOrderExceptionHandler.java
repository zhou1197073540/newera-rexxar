package com.mzkj.mock_trading_system.remote.exception_handler;

import com.mzkj.mock_trading_system.trade.entity.UserOrder;

import java.util.List;

/**
 * create by zhouzhenyang on 2018/8/14
 */
public interface IUserOrderExceptionHandler {

    void handleUserOrders(List<UserOrder> userOrders);
}
