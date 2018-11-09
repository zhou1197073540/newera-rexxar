package com.mzkj.mock_trading_system.trade.valobj;

import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * create by zhouzhenyang on 2018/8/16
 */
@Data
@Accessors(chain = true)
public class AspectExceptionInfo {

    private String method;
    private List<UserOrder> userOrders;
    private Map<String, StockMarketSnapshotPO> stocks;
    private Exception exception;
}
