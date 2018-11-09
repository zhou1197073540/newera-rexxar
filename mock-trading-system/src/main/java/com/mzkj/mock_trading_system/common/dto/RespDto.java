package com.mzkj.mock_trading_system.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * create by zhouzhenyang on 2018/7/31
 */
@Data
@Accessors(chain = true)
public class RespDto<T> {

    String msg;
    String status;
    T data;

    public RespDto() {
    }

    public RespDto(String status, String msg) {
        this.msg = msg;
        this.status = status;
    }


}
