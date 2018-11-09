package com.mzkj.usermock.bean;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.mzkj.usermock.utils.HashUtil;
import com.mzkj.usermock.utils.TimeUtil;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class StockOperation {
    private int id;
    @NotNull(message = "a_account 为空")
    private String a_account;//A股账号
    private String order_num; //订单号
    private String stock_name;//股票名
    @NotNull(message = "code_type为空")
    private String code_type; //股票全称
    private String trading_status;//交易状态

    @NotNull(message = "operation类型为空")
    private String operation; //操作类型(买入，卖出，撤单)
    @NotNull(message = "买入股票num为空")
    private String num;//买入、卖出数量
    @NotNull(message = "买入股票price为空")
    private String price; //买入、卖出价格
    private String datetime= TimeUtil.getDateTime();
    private String assets_change;
    private String position;//持仓
    private String position_available; //可用持仓
    private String cost;//成本

    private String current_price; //当前价格
    private String yesterday_price; //昨日收盘价格
    private String settlement_time; //结算时间

    public String getSettlement_time() {
        return settlement_time;
    }

    public void setSettlement_time(String settlement_time) {
        this.settlement_time = settlement_time;
    }

    public String getYesterday_price() {
        return yesterday_price;
    }

    public void setYesterday_price(String yesterday_price) {
        this.yesterday_price = yesterday_price;
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

    public StockOperation() throws Exception {
        genOrder_num();
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getAssets_change() {
        return assets_change;
    }

    public void setAssets_change(String assets_change) {
        this.assets_change = assets_change;
    }

    public String getA_account() {
        return a_account;
    }

    public void setA_account(String a_account) {
        this.a_account = a_account;
    }

    public String getOrder_num() {
        return order_num;
    }

    private void genOrder_num() throws Exception {
        this.order_num = TimeUtil.getDateTimes()+ HashUtil.generateRandomNum(14);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getTrading_status() {
        return trading_status;
    }

    public void setTrading_status(String trading_status) {
        this.trading_status = trading_status;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
