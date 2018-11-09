package com.mzkj.usermock.dto;

public class RespResult<T> {
    private String status;
    private String msg;
    private T data;

    private RespResult(){}

    public static <T> RespResult<T> genRespResult(String status, T t, String msg){
        RespResult<T> resp=new RespResult();
        resp.setStatus(status);
        resp.setMsg(msg);
        resp.setData(t);
        return resp;
    }

    /**
     * 返回成功的消息
     */
    public static <T> RespResult<T> genSuccessResult(T data){
        return genRespResult("success",data,null);
    }
    /**
     * 返回错误的消息
     */
    public static <T> RespResult<T> genErrorResult(String msg){
        return genRespResult("error",null,msg);
    }

    public void RestResult(T t){
        this.data=t;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
