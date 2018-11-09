package com.mzkj.mock_trading_system.clearing.POJO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * create by zhouzhenyang on 2018/8/13
 */
@Data
@Accessors(chain = true)
public class MailValue {

    @JsonProperty("开始时间")
    private String startDateTime;
    @JsonProperty("结束时间")
    private String endDateTime;
    @JsonProperty("未成交数")
    private Integer waitCount = 0;
    @JsonProperty("未成交退回数")
    private Integer clearWaitCount = 0;
    @JsonProperty("回调出错数")
    private Integer errorCount = 0;
    @JsonProperty("回调出错数清算数")
    private Integer clearErrorCount = 0;

}
