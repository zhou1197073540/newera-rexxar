package com.mouzhiapp.stock_market.controller.VO;

import com.mouzhiapp.stock_market.bean.CategoryStock;

/**
 * create by zhouzhenyang on 2018/6/7
 */
public class CategoryWithTopStockVO {

    String guid;
    String name;
    String changeP;
    CategoryStock stock;

    public CategoryWithTopStockVO(String guid, String name, String changeP) {
        this.guid = guid;
        this.name = name;
        this.changeP = changeP;
    }

    public CategoryWithTopStockVO setStock(CategoryStock stock) {
        this.stock = stock;
        return this;
    }

    public String getChangeP() {
        return changeP;
    }

    public String getGuid() {
        return guid;
    }

    public CategoryWithTopStockVO setGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryWithTopStockVO setName(String name) {
        this.name = name;
        return this;
    }

    public CategoryWithTopStockVO setChangeP(String changeP) {
        this.changeP = changeP;
        return this;
    }

    public CategoryStock getStock() {
        return stock;
    }
}
