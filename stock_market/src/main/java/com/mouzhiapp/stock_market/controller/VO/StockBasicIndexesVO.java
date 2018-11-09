package com.mouzhiapp.stock_market.controller.VO;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 股票基本指标,注意和指数区别
 * <p>
 * create by zhouzhenyang on 2018/5/31
 */
@Data
@Accessors(chain = true)
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
