package com.mzkj.usermock.utils;

import com.alibaba.fastjson.JSONObject;
import com.mzkj.usermock.bean.StockOperation;
import com.mzkj.usermock.bean_rmi.StockOrderRMI;

public class ObjChangeUtil {

    public static StockOrderRMI change2StockOrder(StockOperation stock) {
        StockOrderRMI order=new StockOrderRMI();
        order.setOid(stock.getOrder_num());
        order.setAmount(stock.getNum());
        order.setDatetime(stock.getDatetime());
        order.setFullCode(stock.getCode_type());
        order.setUserAccount(stock.getA_account());
        order.setOperation(stock.getOperation());
        order.setType("A");
        order.setName(stock.getStock_name());
        order.setPrice(stock.getPrice());
        return order;
    }
}
