package com.mzkj.news.bean;

public class FundInfo {
    private String fund_code;
    private String fund_name;
    private String fund_type;
    private String fund_scale;
    private String fund_manager;
    private String administrator;

    public String getFund_code() {
        return fund_code;
    }

    public void setFund_code(String fund_code) {
        this.fund_code = fund_code;
    }

    public String getFund_name() {
        return fund_name;
    }

    public void setFund_name(String fund_name) {
        this.fund_name = fund_name;
    }

    public String getFund_type() {
        return fund_type;
    }

    public void setFund_type(String fund_type) {
        this.fund_type = fund_type;
    }

    public String getFund_scale() {
        return fund_scale;
    }

    public void setFund_scale(String fund_scale) {
        this.fund_scale = fund_scale;
    }

    public String getFund_manager() {
        return fund_manager;
    }

    public void setFund_manager(String fund_manager) {
        this.fund_manager = fund_manager;
    }

    public String getAdministrator() {
        return  administrator;
    }

    public void setAdministrator(String administrator) {
        this.administrator = administrator;
    }
}
