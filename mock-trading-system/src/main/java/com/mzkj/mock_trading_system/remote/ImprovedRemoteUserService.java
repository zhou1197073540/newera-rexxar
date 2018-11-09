package com.mzkj.mock_trading_system.remote;

import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * create by zhouzhenyang on 2018/8/9
 */
@Service
public class ImprovedRemoteUserService {


    @Autowired
    RetryAbleRemoteUserService retryAbleRemoteUserService;

    /**
     * 分段发送
     *
     * @param userOrders
     * @return 发送成功的条数
     * @throws Exception
     */
    public int sendOrderResultSegmented(List<UserOrder> userOrders) {
        if (userOrders == null || userOrders.isEmpty()) {
            return 0;
        }
        int length = 100;
        int size = userOrders.size();
        if (userOrders.size() <= 100) {
            try {
                retryAbleRemoteUserService.sendOrderResultWithExceptionHandle(userOrders);
            } catch (Exception e) {
                size = 0;
                //todo 其实已经处理过了
            }
            return size;
        }
        int parts = (int) Math.ceil(((float) userOrders.size() / length));
        int end = userOrders.size() - 1;
        for (int i = 0; i < parts; i++) {
            int endLength = (length * (i + 1) > end) ? length * (i + 1) : end;
            final List<UserOrder> subList = userOrders.subList(i * length, endLength);
            try {
                retryAbleRemoteUserService.sendOrderResultWithExceptionHandle(userOrders);
            } catch (Exception e) {
                size = size - subList.size();
                //todo 其实已经处理过了
            }
        }
        return size;
    }
}
