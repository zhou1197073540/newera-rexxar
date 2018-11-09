package com.mzkj.news.bean_vo;

/**
 * 自选股升降幅
 */
public class StockUpDownVO {
    private String code;
    private String code_type;
    private String name;
    private String price;
    private String up_down;
    private String rank;
    private float warm_price;
    private String setup;
    private String send;
    private String type;//1:A股，2：B股，9：AB股指数
    private String status;//1:上市，5：终止上市，11：停牌

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getWarm_price() {
        return warm_price;
    }

    public void setWarm_price(float warm_price) {
        this.warm_price = warm_price;
    }

    public String getSetup() {
        return setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public StockUpDownVO() {}

    public StockUpDownVO(String code, String price, String up_down) {
        this.code = code;
        this.price = price;
        this.up_down = up_down;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getCode_type() {
        return code_type;
    }

    public void setCode_type(String code_type) {
        this.code_type = code_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUp_down() {
        return up_down;
    }

    public void setUp_down(String up_down) {
        this.up_down = up_down;
    }
}
