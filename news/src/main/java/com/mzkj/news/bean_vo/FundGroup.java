package com.mzkj.news.bean_vo;

public class FundGroup {
    private String fund_name;
    private String fund_code;
    private String fund_hold_rate;
    private String date_time;

    public String getFund_name() {
        return fund_name;
    }

    public void setFund_name(String fund_name) {
        this.fund_name = fund_name;
    }

    public String getFund_code() {
        return fund_code;
    }

    public void setFund_code(String fund_code) {
        this.fund_code = fund_code;
    }

    public String getFund_hold_rate() {
        return fund_hold_rate;
    }

    public void setFund_hold_rate(String fund_hold_rate) {
        this.fund_hold_rate = fund_hold_rate;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        if(date_time!=null&&date_time.length()>19){
            date_time=date_time.substring(0,19);
        }
        this.date_time = date_time;
    }
}
