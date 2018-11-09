package com.mzkj.mock_trading_system.trade.valobj;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * create by zhouzhenyang on 2018/8/1
 */
@Data
@Accessors(chain = true)
public class MockStockUserOrderPO {

    String oid;
    @JsonAlias("order_status")
    String orderStatus;
}
