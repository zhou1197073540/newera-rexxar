package com.mzkj.news.bean_vo;

public class FundChange {
    private String fund_code;
    private String total_net_value;
    private String weekly_total_chg;
    private String monthly_total_chg;
    private String date;
    private String monthly_rank;
    private String daily_chg_rate;

    public String getDaily_chg_rate() {
        return daily_chg_rate;
    }

    public void setDaily_chg_rate(String daily_chg_rate) {
        this.daily_chg_rate = daily_chg_rate;
    }

    public String getFund_code() {
        return fund_code;
    }

    public void setFund_code(String fund_code) {
        this.fund_code = fund_code;
    }

    public String getTotal_net_value() {
        return total_net_value;
    }

    public void setTotal_net_value(String total_net_value) {
        this.total_net_value = total_net_value;
    }

    public String getWeekly_total_chg() {
        return weekly_total_chg;
    }

    public void setWeekly_total_chg(String weekly_total_chg) {
        this.weekly_total_chg = weekly_total_chg;
    }

    public String getMonthly_total_chg() {
        return monthly_total_chg;
    }

    public void setMonthly_total_chg(String monthly_total_chg) {
        this.monthly_total_chg = monthly_total_chg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonthly_rank() {
        return monthly_rank;
    }

    public void setMonthly_rank(String monthly_rank) {
        this.monthly_rank = monthly_rank;
    }
}
