package com.mzkj.usermock.exception.runtime;

import com.mzkj.usermock.bean_rmi.StockOrderRMI;
import com.mzkj.usermock.enums.CodeEnum;

public class SettlementException extends RuntimeException {
    private CodeEnum codeEnum;
    public SettlementException(CodeEnum ce, StockOrderRMI ss) {
        super(ce.getMsg()+"=="+ss.toString());
        this.codeEnum=ce;
    }

    public SettlementException(CodeEnum ce) {
        super(ce.getMsg());
        this.codeEnum=ce;
    }

    public CodeEnum getCodeEnum() {
        return codeEnum;
    }
}
