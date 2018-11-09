package com.mzkj.usermock.exception.runtime;

import com.mzkj.usermock.enums.CodeEnum;

public class RequestException  extends  RuntimeException{
    private CodeEnum codeEnum;

    public RequestException(CodeEnum ce) {
        super(ce.getMsg());
        this.codeEnum=ce;
    }

    public CodeEnum getCodeEnum() {
        return codeEnum;
    }
}
