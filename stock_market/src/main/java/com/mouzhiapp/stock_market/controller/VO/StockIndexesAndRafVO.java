package com.mouzhiapp.stock_market.controller.VO;

import java.util.List;

/**
 * create by zhouzhenyang on 2018/6/19
 */
public class StockIndexesAndRafVO {

   private String riseAndFall;
   private List<StockIndexVO> stockIndexes;

    public StockIndexesAndRafVO(String riseAndFall, List<StockIndexVO> stockIndexes) {
        this.riseAndFall = riseAndFall;
        this.stockIndexes = stockIndexes;
    }

    public String getRiseAndFall() {
        return riseAndFall;
    }

    public StockIndexesAndRafVO setRiseAndFall(String riseAndFall) {
        this.riseAndFall = riseAndFall;
        return this;
    }

    public List<StockIndexVO> getStockIndexes() {
        return stockIndexes;
    }

    public StockIndexesAndRafVO setStockIndexes(List<StockIndexVO> stockIndexes) {
        this.stockIndexes = stockIndexes;
        return this;
    }
}
