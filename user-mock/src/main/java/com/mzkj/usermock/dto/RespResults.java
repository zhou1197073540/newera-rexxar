package com.mzkj.usermock.dto;

import com.mzkj.usermock.enums.CodeEnum;

public class RespResults<T> {
    private Integer status;
    private String msg;
    private T data;

    private RespResults(){}

    public RespResults(T data) {
        this.data = data;
    }

    public static <T> RespResults<T> genSuccessResult(T data){
        RespResults<T> resp=new RespResults();
        resp.setStatus(CodeEnum.SUCCESS.getStatus());
        resp.setMsg(CodeEnum.SUCCESS.getMsg());
        resp.setData(data);
        return resp;
    }

    public static <T> RespResults<T> genErrorResult(CodeEnum re, String errorMsg){
        RespResults<T> resp=new RespResults();
        resp.setStatus(re.getStatus());
        resp.setMsg(errorMsg);
        resp.setData(null);
        return resp;
    }
    public static <T> RespResults<T> genErrorResult(CodeEnum re){
        RespResults<T> resp=new RespResults();
        resp.setStatus(re.getStatus());
        resp.setMsg(re.getMsg());
        resp.setData(null);
        return resp;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "RestResult{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
