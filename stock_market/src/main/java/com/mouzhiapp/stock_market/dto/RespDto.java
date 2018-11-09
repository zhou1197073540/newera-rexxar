package com.mouzhiapp.stock_market.dto;

import java.io.Serializable;

public class RespDto<T> implements Serializable {

    private static final long serialVersionUID = -5445762954238303616L;

    private boolean status;
    private String statusCode;
    private String msg;
    private T data;

    public RespDto() {
    }

    public RespDto(boolean success) {
        this.status = success;
    }

    public RespDto(boolean success, String msg) {
        this.status = success;
        this.msg = msg;
    }

    public RespDto(boolean status, String statusCode, String msg) {
        this.status = status;
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public RespDto(boolean success, String msg, T data) {
        this.status = success;
        this.msg = msg;
        this.data = data;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public RespDto<T> setStatusCode(String statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
