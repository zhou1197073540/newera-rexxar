package com.mzkj.xiaoxin.remote.PO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockBasicIndexesVO {

    private String open;
    private String close;
    private String high;
    private String low;
    private String volume;
    private String now;
    private String name;
    private String code;
    @JsonAlias("full_code")
    private String fullCode;
    private String pe;
    private String pb;
    private String turnoverRatio;
    private String status; //如果为A股 则对应股票状态
    private String type; //1:A股 2:B股 9:A,B股指数
    private String datetime;
}
