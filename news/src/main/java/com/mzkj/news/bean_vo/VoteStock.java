package com.mzkj.news.bean_vo;

public class VoteStock {
    private String up_num;
    private String up_rate;
    private String down_num;
    private String down_rate;

    public String getUp_num() {
        return up_num;
    }

    public void setUp_num(String up_num) {
        this.up_num = up_num;
    }

    public String getUp_rate() {
        return up_rate;
    }

    public void setUp_rate(String up_rate) {
        this.up_rate = up_rate;
    }

    public String getDown_num() {
        return down_num;
    }

    public void setDown_num(String down_num) {
        this.down_num = down_num;
    }

    public String getDown_rate() {
        return down_rate;
    }

    public void setDown_rate(String down_rate) {
        this.down_rate = down_rate;
    }
}
