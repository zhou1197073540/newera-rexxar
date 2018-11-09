package com.mzkj.usermock.bean_vo;

public class HoldStockInfoVO {
    private String stock_name;
    private String code_type;
    private String market;//市值
    private String pro_loss;
    private String pro_loss_rate;
    private String position;
    private String position_available;
    private String cost;
    private String current_price;

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getCode_type() {
        return code_type;
    }

    public void setCode_type(String code_type) {
        this.code_type = code_type;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getPro_loss() {
        return pro_loss;
    }

    public void setPro_loss(String pro_loss) {
        this.pro_loss = pro_loss;
    }

    public String getPro_loss_rate() {
        return pro_loss_rate;
    }

    public void setPro_loss_rate(String pro_loss_rate) {
        this.pro_loss_rate = pro_loss_rate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition_available() {
        return position_available;
    }

    public void setPosition_available(String position_available) {
        this.position_available = position_available;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }
}
