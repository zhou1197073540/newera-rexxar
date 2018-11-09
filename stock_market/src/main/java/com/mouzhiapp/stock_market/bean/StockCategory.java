package com.mouzhiapp.stock_market.bean;

/**
 * create by zhouzhenyang on 2018/6/4
 */
public class StockCategory {
    String category;
    String tickers;

    public String getCategory() {
        return category;
    }

    public StockCategory setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getTickers() {
        return tickers;
    }

    public StockCategory setTickers(String tickers) {
        this.tickers = tickers;
        return this;
    }
}
