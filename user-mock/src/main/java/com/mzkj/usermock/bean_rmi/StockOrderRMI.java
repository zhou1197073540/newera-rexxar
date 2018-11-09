package com.mzkj.usermock.bean_rmi;

public class StockOrderRMI {
    public String oid;
    public String userAccount;
    public String fullCode;
    public String datetime;
    public String name;
    public String price;
    public String amount;
    public String operation;
    public String type;
    public String order_status;
    public String tradePrice; //成交价

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getFullCode() {
        return fullCode;
    }

    public void setFullCode(String fullCode) {
        this.fullCode = fullCode;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "StockOrderRMI{" +
                "oid='" + oid + '\'' +
                ", userAccount='" + userAccount + '\'' +
                ", fullCode='" + fullCode + '\'' +
                ", datetime='" + datetime + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", amount='" + amount + '\'' +
                ", operation='" + operation + '\'' +
                ", type='" + type + '\'' +
                ", order_status='" + order_status + '\'' +
                ", tradePrice='" + tradePrice + '\'' +
                '}';
    }
}
