package com.mzkj.mock_trading_system.common.consts;

import java.time.LocalTime;

/**
 * create by zhouzhenyang on 2018/5/22
 */
public class Consts {

    public static final String STOCK_LATEST = "stock_latest";

    public static final LocalTime v0930 = LocalTime.of(9, 30);
    public static final LocalTime v1130 = LocalTime.of(11, 30);
    public static final LocalTime v1300 = LocalTime.of(13, 0);
    public static final LocalTime v1500 = LocalTime.of(15, 0);
    public static final LocalTime v1800 = LocalTime.of(18, 0);


    public static final String OPERATION_BUY = "buy";
    public static final String OPERATION_SELL = "sell";
    public static final String OPERATION_CANCEL = "cancel";

    public static final String WAIT = "wait";
    public static final String FINISH = "finish";
    public static final String CANCEL = "cancel";

    public static final String A_TRADING_QUEUE = ".A.trading.queue";
}
