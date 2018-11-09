package com.mouzhiapp.stock_market.controller.VO;

import com.mouzhiapp.stock_market.bean.CategoryStock;

import java.util.List;

/**
 * create by zhouzhenyang on 2018/6/21
 */
public class CategoryStocksAndFundFlowVO {

    String smallBuy;
    String smallSale;
    String mediumBuy;
    String mediumSale;
    String largeBuy;
    String largeSale;
    List<CategoryStock> stocks;

    public CategoryStocksAndFundFlowVO(String smallBuy, String smallSale, String mediumBuy, String mediumSale, String largeBuy, String largeSale) {
        this.smallBuy = smallBuy;
        this.smallSale = smallSale;
        this.mediumBuy = mediumBuy;
        this.mediumSale = mediumSale;
        this.largeBuy = largeBuy;
        this.largeSale = largeSale;
    }

    public String getSmallBuy() {
        return smallBuy;
    }

    public CategoryStocksAndFundFlowVO setSmallBuy(String smallBuy) {
        this.smallBuy = smallBuy;
        return this;
    }

    public String getSmallSale() {
        return smallSale;
    }

    public CategoryStocksAndFundFlowVO setSmallSale(String smallSale) {
        this.smallSale = smallSale;
        return this;
    }

    public String getMediumBuy() {
        return mediumBuy;
    }

    public CategoryStocksAndFundFlowVO setMediumBuy(String mediumBuy) {
        this.mediumBuy = mediumBuy;
        return this;
    }

    public String getMediumSale() {
        return mediumSale;
    }

    public CategoryStocksAndFundFlowVO setMediumSale(String mediumSale) {
        this.mediumSale = mediumSale;
        return this;
    }

    public String getLargeBuy() {
        return largeBuy;
    }

    public CategoryStocksAndFundFlowVO setLargeBuy(String largeBuy) {
        this.largeBuy = largeBuy;
        return this;
    }

    public String getLargeSale() {
        return largeSale;
    }

    public CategoryStocksAndFundFlowVO setLargeSale(String largeSale) {
        this.largeSale = largeSale;
        return this;
    }

    public List<CategoryStock> getStocks() {
        return stocks;
    }

    public CategoryStocksAndFundFlowVO setStocks(List<CategoryStock> stocks) {
        this.stocks = stocks;
        return this;
    }
}
