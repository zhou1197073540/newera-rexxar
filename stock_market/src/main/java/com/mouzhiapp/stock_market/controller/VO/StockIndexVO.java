package com.mouzhiapp.stock_market.controller.VO;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * 各种指数,注意和股票基本指标区别
 * create by zhouzhenyang on 2018/6/9
 */
public class StockIndexVO {

    private String code;
    @JsonAlias("full_code")
    private String fullCode;
    private String name;
    private String close;
    private String now;
    private String change;
    private String changeP;

    public String getClose() {
        return close;
    }

    public StockIndexVO setClose(String close) {
        this.close = close;
        return this;
    }

    public String getCode() {
        return code;
    }

    public StockIndexVO setCode(String code) {
        this.code = code;
        return this;
    }

    public String getFullCode() {
        return fullCode;
    }

    public StockIndexVO setFullCode(String fullCode) {
        this.fullCode = fullCode;
        return this;
    }

    public String getName() {
        return name;
    }

    public StockIndexVO setName(String name) {
        this.name = name;
        return this;
    }

    public String getNow() {
        return now;
    }

    public StockIndexVO setNow(String now) {
        this.now = now;
        return this;
    }

    public String getChange() {
        return change;
    }

    public StockIndexVO setChange(String change) {
        this.change = change;
        return this;
    }

    public String getChangeP() {
        return changeP;
    }

    public StockIndexVO setChangeP(String changeP) {
        this.changeP = changeP;
        return this;
    }
}
