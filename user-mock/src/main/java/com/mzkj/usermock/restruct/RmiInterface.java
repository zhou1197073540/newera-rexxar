package com.mzkj.usermock.restruct;

import com.mzkj.usermock.bean.StockOperation;
import com.mzkj.usermock.bean_rmi.StockOrderRMI;
import com.mzkj.usermock.dto.RespResult;

public interface RmiInterface {

    /**
     * 用户买卖取消操作调用结算中心
     */
    RespResult userStockOperation(StockOrderRMI order);

}
