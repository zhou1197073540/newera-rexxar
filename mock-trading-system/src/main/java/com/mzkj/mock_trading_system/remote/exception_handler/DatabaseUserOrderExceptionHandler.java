package com.mzkj.mock_trading_system.remote.exception_handler;

import com.mzkj.mock_trading_system.remote.Consts;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import com.mzkj.mock_trading_system.trade.repo.mapper.ow.StockOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * create by zhouzhenyang on 2018/8/14
 */
@Component
public class DatabaseUserOrderExceptionHandler implements IUserOrderExceptionHandler {

    @Autowired
    private StockOrderMapper stockOrderMapper;
    private Logger logger = LoggerFactory.getLogger(DatabaseUserOrderExceptionHandler.class);

    @Override
    public void handleUserOrders(List<UserOrder> userOrders) {

        try {
            stockOrderMapper.doCreateFinishedWithErrorOrder(userOrders, Consts.FINISH_WITH_ERROR);
        } catch (Exception e) {
            logger.error(this.getClass().getName(), e);
        }
    }
}
