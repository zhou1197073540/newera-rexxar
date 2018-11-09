package com.mzkj.news.bean;

public class StockRecommend {
    private String code;
    private String sigval;
    private String close;
    private String atr;
    private String strategyid;
    private String datetime;
    private String code_type;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode_type() {
        return code_type;
    }

    public void setCode_type(String code_type) {
        this.code_type = code_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSigval() {
        return sigval;
    }

    public void setSigval(String sigval) {
        this.sigval = sigval;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getAtr() {
        return atr;
    }

    public void setAtr(String atr) {
        this.atr = atr;
    }

    public String getStrategyid() {
        return strategyid;
    }

    public void setStrategyid(String strategyid) {
        this.strategyid = strategyid;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
