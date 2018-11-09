package com.mzkj.news.bean;

import java.util.HashMap;

public class MsgPushStock extends HashMap<String,Object>{
    public final static String package_name="com.newerarnd.stock.StockMainInfoActivity";
    private MsgPushStock(){}

    public MsgPushStock(String fullSymbol, String symbolName, String symbol) {
        super.put("fullSymbol",fullSymbol);
        super.put("symbolName",symbolName);
        super.put("symbol",symbol);
    }
}
