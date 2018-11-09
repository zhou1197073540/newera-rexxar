package com.mzkj.mock_trading_system.remote;

import com.mzkj.mock_trading_system.common.consts.Message;
import com.mzkj.mock_trading_system.common.dto.RespDto;
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
public abstract class BaseRemoteUserService {

    @Autowired
    private RemoteUserService remoteUserService;
    private final Logger logger = LoggerFactory.getLogger(BaseRemoteUserService.class);

    public boolean sendOrderResult(List<UserOrder> userOrders) throws Exception {
        try {
            final RespDto respDto = remoteUserService.sendOrderResult(userOrders);
            if (Consts.REMOTE_SUCCESS.equals(respDto.getStatus())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error(Message.UNCAUGHT_EXCEPTION.message, e);
            return false;
        }
    }
}
