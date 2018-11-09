package com.mzkj.mock_trading_system.receive.service;

import com.mzkj.mock_trading_system.receive.valobj.OrderContext;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import com.mzkj.mock_trading_system.trade.repo.cache.OidLock;
import com.mzkj.mock_trading_system.trade.repo.mapper.ow.StockOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static com.mzkj.mock_trading_system.common.consts.Consts.*;
import static com.mzkj.mock_trading_system.common.consts.Message.*;

/**
 * create by zhouzhenyang on 2018/7/31
 */
@Component(OPERATION_CANCEL)
public class CancelHandler extends OrderOperationHandler {

    @Autowired
    StockOrderMapper orderMapper;
    @Autowired
    OidLock userAccountFullCodeLock;
    @Autowired
    AsyncSendResultService asyncSendResultService;

    @Override
    public void process(OrderContext orderContext) throws Exception {
        UserOrder userOrder = orderContext.getUserOrder();
        if (userAccountFullCodeLock.tryLock(userOrder)) {
            try {
                String orderStatus = orderMapper.doGetOrderStatusByOid(userOrder.getOid());
                if (orderStatus == null) {
                    orderContext.setStatusCode(NOT_EXIST_ORDER.statusCode)
                            .setMsg(NOT_EXIST_ORDER.message);
                } else if (orderStatus.equals(CANCEL)) {
                    orderContext.setStatusCode(ALREADY_CANCELED.statusCode)
                            .setMsg(ALREADY_CANCELED.message);
                } else if (orderStatus.equals(FINISH)) {
                    orderContext.setStatusCode(ALREADY_FINISHED.statusCode)
                            .setMsg(ALREADY_FINISHED.message);
                } else {
                    cancelOrder(userOrder.getOid());
                    asyncSendResultService
                            .sendUserOrderResult(Collections.singletonList(userOrder));
                    orderContext.setStatusCode(SUCCESS.statusCode)
                            .setMsg(SUCCESS.message);
                }
            } finally {
                userAccountFullCodeLock.unlock(userOrder);
            }
        }
    }

    @Transactional
    void cancelOrder(String oid) throws Exception {
        orderMapper.doUpdateOrderStatusByOid(oid, CANCEL);
    }


}
