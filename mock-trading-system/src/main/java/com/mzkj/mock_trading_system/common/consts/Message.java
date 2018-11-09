package com.mzkj.mock_trading_system.common.consts;

/**
 * create by zhouzhenyang on 2018/7/31
 */
public enum Message {

    SUCCESS("000101", "成功"),
    DUPLICATED_ORDER("000201", "订单已存在"), //暂时不区分
    NOT_EXIST_ORDER("000202", "订单不存在"),
    ALREADY_CANCELED("000203", "已取消订单"),
    ALREADY_FINISHED("000204", "已成交订单"),
    UN_TRADING_TIME("000210", "非下单时间"),
    UN_SUPPORTED_STOCK("000201", "不支持的股票"),
    UN_SUPPORTED_OPERATION("000299", "不支持的操作"),
    UNCAUGHT_EXCEPTION("000298", "未捕捉异常"),
    SCHEDULER_UNCAUGHT_EXCEPTION("000399", "未捕捉异常"),
    DO_TRADE_EXCEPTION("000301", "交易异常");

    public String message;
    public String statusCode;

    Message(String statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
