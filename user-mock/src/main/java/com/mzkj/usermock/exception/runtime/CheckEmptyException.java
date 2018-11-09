package com.mzkj.usermock.exception.runtime;

import com.mzkj.usermock.enums.CodeEnum;

public class CheckEmptyException extends RuntimeException {
    private Integer status;
    private String msg;

    public CheckEmptyException(CodeEnum ce) {
        super(ce.getMsg());
        this.msg=ce.getMsg();
        this.status=ce.getStatus();
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
}
