package com.mzkj.usermock.bean;

public class StockAccountInfo {
    private int id;
    private String a_account;
    private String total_assets;
    private String total_pro_loss;
    private String day_pro_loss;
    private String total_market;
    private String assets_available;
    private String cash;

    public StockAccountInfo() {
    }

    public StockAccountInfo(String a_account, String assets_available) {
        this.a_account = a_account;
        this.assets_available = assets_available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getA_account() {
        return a_account;
    }

    public void setA_account(String a_account) {
        this.a_account = a_account;
    }

    public String getTotal_assets() {
        return total_assets;
    }

    public void setTotal_assets(String total_assets) {
        this.total_assets = total_assets;
    }

    public String getTotal_pro_loss() {
        return total_pro_loss;
    }

    public void setTotal_pro_loss(String total_pro_loss) {
        this.total_pro_loss = total_pro_loss;
    }

    public String getDay_pro_loss() {
        return day_pro_loss;
    }

    public void setDay_pro_loss(String day_pro_loss) {
        this.day_pro_loss = day_pro_loss;
    }

    public String getTotal_market() {
        return total_market;
    }

    public void setTotal_market(String total_market) {
        this.total_market = total_market;
    }

    public String getAssets_available() {
        return assets_available;
    }

    public void setAssets_available(String assets_available) {
        this.assets_available = assets_available;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }
}
