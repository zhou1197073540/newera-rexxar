package com.mzkj.news.bean;

public class FundNetValue {
    private String fund_code;
    private String total_net_value;
    private String date;

    public String getTotal_net_value() {
        return total_net_value;
    }

    public void setTotal_net_value(String total_net_value) {
        this.total_net_value = total_net_value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFund_code() {
        return fund_code;
    }

    public void setFund_code(String fund_code) {
        this.fund_code = fund_code;
    }

}
