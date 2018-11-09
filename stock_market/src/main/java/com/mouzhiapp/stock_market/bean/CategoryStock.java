package com.mouzhiapp.stock_market.bean;

/**
 * create by zhouzhenyang on 2018/6/3
 */
public class CategoryStock implements CalculateChangeAndChangeP<CategoryStock>{

    private String now;
    private String high;
    private String low;
    private String open;
    private String close;
    private String name;
    private String code;
    private String fullCode;
    private String volume;
    private String change;
    private String changeP;
    private String datetime;

    public String getNow() {
        return now;
    }

    public CategoryStock setNow(String now) {
        this.now = now;
        return this;
    }

    public String getHigh() {
        return high;
    }

    public CategoryStock setHigh(String high) {
        this.high = high;
        return this;
    }

    public String getLow() {
        return low;
    }

    public CategoryStock setLow(String low) {
        this.low = low;
        return this;
    }

    public String getOpen() {
        return open;
    }

    public CategoryStock setOpen(String open) {
        this.open = open;
        return this;
    }

    public String getClose() {
        return close;
    }

    public CategoryStock setClose(String close) {
        this.close = close;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryStock setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public CategoryStock setCode(String code) {
        this.code = code;
        return this;
    }

    public String getFullCode() {
        return fullCode;
    }

    public CategoryStock setFullCode(String fullCode) {
        this.fullCode = fullCode;
        return this;
    }

    public String getVolume() {
        return volume;
    }

    public CategoryStock setVolume(String volume) {
        this.volume = volume;
        return this;
    }

    public String getDatetime() {
        return datetime;
    }

    public CategoryStock setDatetime(String datetime) {
        this.datetime = datetime;
        return this;
    }

    public String getChange() {
        return change;
    }

    public CategoryStock setChange(String change) {
        this.change = change;
        return this;
    }

    public String getChangeP() {
        return changeP;
    }

    public CategoryStock setChangeP(String changeP) {
        this.changeP = changeP;
        return this;
    }

    @Override
    public String toString() {
        return "CategoryStock{" +
                "now='" + now + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", open='" + open + '\'' +
                ", close='" + close + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", fullCode='" + fullCode + '\'' +
                ", volume='" + volume + '\'' +
                ", change='" + change + '\'' +
                ", changeP='" + changeP + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
