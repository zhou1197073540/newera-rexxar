package com.mouzhiapp.stock_market.bean;

/**
 * create by zhouzhenyang on 2018/5/22
 */
public class TimelineDot {

    private Double price;
    private String Volume;
    private String datetime;

    public Double getPrice() {
        return price;
    }

    public TimelineDot setPrice(Double price) {
        this.price = price;
        return this;
    }

    public String getVolume() {
        return Volume;
    }

    public TimelineDot setVolume(String volume) {
        this.Volume = volume;
        return this;
    }

    public String getDatetime() {
        return datetime;
    }

    public TimelineDot setDatetime(String datetime) {
        this.datetime = datetime;
        return this;
    }

}
