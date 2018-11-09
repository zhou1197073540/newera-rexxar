package com.mzkj.news.bean;

public class GrailTime {
    private String date_time;
    private String ticker;
    private String action;
    private String closeval;
    private String profit;
    private String cumprofit;
    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCloseval() {
        return closeval;
    }

    public void setCloseval(String closeval) {
        this.closeval = closeval;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getCumprofit() {
        return cumprofit;
    }

    public void setCumprofit(String cumprofit) {
        this.cumprofit = cumprofit;
    }
}
