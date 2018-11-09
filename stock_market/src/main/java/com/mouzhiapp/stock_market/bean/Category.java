package com.mouzhiapp.stock_market.bean;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedList;
import java.util.List;

import static java.math.BigDecimal.ROUND_UP;

/**
 * create by zhouzhenyang on 2018/6/6
 */
public class Category {

    String guid;
    String name;
    String changeP;
    public List<CategoryStock> stocks = new LinkedList<>();

    public Category() {
    }

    public Category(String guid, String name, String changeP) {
        this.guid = guid;
        this.name = name;
        this.changeP = changeP;
    }

    public void sortStocks() {
        stocks.sort((o1, o2) -> {
            if (Double.valueOf(o1.getChangeP()) >
                    Double.valueOf(o2.getChangeP())) {
                return -1;
            } else if (Double.valueOf(o1.getChangeP())
                    .equals(Double.valueOf(o2.getChangeP()))) {
                return 0;
            }
            return 1;
        });
    }

    public String calIndustryChangeP() {
        BigDecimal allVolumes =
                stocks.stream().map(stock -> new BigDecimal(stock.getVolume()))
                        .reduce(BigDecimal::add).orElse(new BigDecimal(1));
        if (allVolumes.longValue() == 0) {
            return "0";
        }

        BigDecimal changeP =
                stocks.stream()
                        .map(o -> {
                            BigDecimal vol = new BigDecimal(o.getVolume());
                            return new BigDecimal(o.getChangeP()).multiply(vol.divide(allVolumes, MathContext.DECIMAL32));
                        })
                        .reduce(BigDecimal::add).get();
        return changeP.setScale(5, ROUND_UP).toString();
    }

    public String getGuid() {
        return guid;
    }

    public Category setGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public String getChangeP() {
        return changeP;
    }

    public Category setChangeP(String changeP) {
        this.changeP = changeP;
        return this;
    }

    public Category addStock(CategoryStock categoryStock) {
        stocks.add(categoryStock);
        return this;
    }
}
