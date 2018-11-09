package com.mzkj.news.bean_vo;

import lombok.Data;
import lombok.experimental.Accessors;

public class IndustryIndex {
    private String time;
    private String close;
    private String industry;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}
