package com.mouzhiapp.stock_market.consts;

/**
 * create by zhouzhenyang on 2018/7/31
 */
public enum Message {

    SUCCESS("000101", "成功"),
    TOKEN_EXPIRED("000201", "token过期"); //暂时不区分

    public String message;
    public String statusCode;

    Message(String statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
