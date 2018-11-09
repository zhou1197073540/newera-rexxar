package com.mzkj.usermock.bean_vo;

public class AccountInfoVO {
    private String total_assets="0";
    private String total_pro_loss="0";
    private String day_pro_loss="0";
    private String total_market="0";
    private String assets_available="0";
    private String cash="0";

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
