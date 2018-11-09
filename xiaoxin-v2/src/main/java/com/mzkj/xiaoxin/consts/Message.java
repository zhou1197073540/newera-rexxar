package com.mzkj.xiaoxin.consts;

/**
 * create by zhouzhenyang on 2018/7/31
 */
public enum Message {

    UN_KNOWN("unknown", "小新还小,不懂您在说什么~"),
    STOCK("stock", "过"),;

    public String type;
    public String message;

    Message(String type, String message) {
        this.type = type;
        this.message = message;
    }
}
