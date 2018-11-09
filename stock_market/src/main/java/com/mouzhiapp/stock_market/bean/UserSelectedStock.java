package com.mouzhiapp.stock_market.bean;

import com.mouzhiapp.stock_market.controller.VO.StockBasicIndexesVO;

/**
 * create by zhouzhenyang on 2018/5/31
 */
public class UserSelectedStock {

    private StockBasicIndexesVO basicIndexes;
    private String isSelected;

    public UserSelectedStock(String isSelected) {
        this.isSelected = isSelected;
    }

    public UserSelectedStock(StockBasicIndexesVO stockBasicIndexesVO) {
        this.basicIndexes = stockBasicIndexesVO;
    }

    public StockBasicIndexesVO getBasicIndexes() {
        return basicIndexes;
    }

    public UserSelectedStock setBasicIndexes(StockBasicIndexesVO basicIndexes) {
        this.basicIndexes = basicIndexes;
        return this;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public UserSelectedStock setIsSelected(String isSelected) {
        this.isSelected = isSelected;
        return this;
    }
}
