package com.mzkj.mock_trading_system.receive.valobj;

import lombok.Data;
import lombok.experimental.Accessors;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;

/**
 * create by zhouzhenyang on 2018/7/29
 */
@Data
@Accessors(chain = true)
public class OrderContext {

    String operation;
    UserOrder order;
    String msg;
    String statusCode;

    public OrderContext(UserOrder order) {
        this.operation = order.getOperation();
        this.order = order;
    }

    public UserOrder getUserOrder() {
        return this.order;
    }
}
