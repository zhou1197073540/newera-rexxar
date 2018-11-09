package com.mzkj.mock_trading_system.remote;

import com.mzkj.mock_trading_system.remote.exception_handler.IUserOrderExceptionHandler;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by zhouzhenyang on 2018/8/14
 */
@Service
public class RetryAbleRemoteUserService extends BaseRemoteUserService {

    @Autowired
    List<IUserOrderExceptionHandler> userOrderExceptionHandlers;

    private Logger logger = LoggerFactory.getLogger(RetryAbleRemoteUserService.class);
    @Autowired
    BaseRemoteUserService baseRemoteUserService;


    /**
     * todo 要在httpclient或okhttp中集成实现
     * 带重发
     *
     * @param userOrders
     */
    @Override
    public boolean sendOrderResult(List<UserOrder> userOrders) throws Exception {
        for (int i = 0; i < 3; i++) {
            try {
                boolean isSuccess = super.sendOrderResult(userOrders);
                if (isSuccess) {
                    return true;
                }
                logger.warn("{} retry:{}", Thread.currentThread().getName(), i);
            } catch (Exception e) {
                logger.warn("{} retry:{}, exception:{}", Thread.currentThread().getName(), i, e);
                if (i == 2) {
                    throw e;
                }
            }
            Thread.sleep(1000);
        }
        return false;
    }

    public void sendOrderResultWithExceptionHandle(List<UserOrder> userOrders) throws Exception {
        if (!sendOrderResult(userOrders)) {
            if (userOrderExceptionHandlers != null
                    && !userOrderExceptionHandlers.isEmpty()) {
                userOrderExceptionHandlers.forEach(
                        userOrderExceptionHandler ->
                                userOrderExceptionHandler.handleUserOrders(userOrders)
                );
            }
        }
    }
}
