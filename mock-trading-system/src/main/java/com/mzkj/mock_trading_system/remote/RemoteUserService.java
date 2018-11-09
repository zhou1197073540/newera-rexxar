package com.mzkj.mock_trading_system.remote;

import com.mzkj.mock_trading_system.common.dto.RespDto;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * create by zhouzhenyang on 2018/8/1
 */
@FeignClient(name = "user-mock")
public interface RemoteUserService {

    @RequestMapping(value = "/um/stock-operation-settlement", method = RequestMethod.POST)
    RespDto sendOrderResult(List<UserOrder> userOrder) throws Exception;
}
