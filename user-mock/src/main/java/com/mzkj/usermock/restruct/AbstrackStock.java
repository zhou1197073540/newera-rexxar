package com.mzkj.usermock.restruct;

import com.mzkj.usermock.bean.StockOperation;
import com.mzkj.usermock.bean_rmi.StockOrderRMI;
import com.mzkj.usermock.constant.Const;
import com.mzkj.usermock.enums.CodeEnum;
import com.mzkj.usermock.exception.runtime.RequestException;
import com.mzkj.usermock.exception.runtime.SettlementException;
import com.mzkj.usermock.mapper.MockMapper;
import com.mzkj.usermock.service.MockService;
import com.mzkj.usermock.utils.RedisUtil;
import com.mzkj.usermock.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public abstract class AbstrackStock {
    @Autowired
    MockService mockService;

    public boolean checkSettle(String order_num){
        int num= mockService.checkSettle(order_num);
        return num>0;
    }

    public boolean getSettleLock(String key_lock){
        if (RedisUtil.tryGetDistributedLock(key_lock)) {
            return true;
        }
        return false;
    }

    public abstract boolean checkMoney(StockOperation stock) throws Exception;

    public abstract String dealOperateion(StockOperation stock) throws RequestException;

    public abstract boolean settlementOperation(StockOrderRMI stockRMI)throws Exception;


}
