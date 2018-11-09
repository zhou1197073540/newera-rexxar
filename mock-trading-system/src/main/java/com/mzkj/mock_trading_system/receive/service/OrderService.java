package com.mzkj.mock_trading_system.receive.service;

import com.mzkj.mock_trading_system.receive.valobj.OrderContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mzkj.mock_trading_system.trade.service.UtilService;
import com.mzkj.mock_trading_system.trade.repo.mapper.ow.OrderMapper;

/**
 * create by zhouzhenyang on 2018/7/29
 */
@Service
public class OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    UtilService utilService;

    public void saveOriginOrder(OrderContext orderContext) {
        orderMapper.saveOrder(orderContext.getUserOrder());
    }
}
