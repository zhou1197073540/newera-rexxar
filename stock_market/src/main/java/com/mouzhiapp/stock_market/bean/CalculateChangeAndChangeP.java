package com.mouzhiapp.stock_market.bean;

/**
 * create by zhouzhenyang on 2018/6/9
 */
public interface CalculateChangeAndChangeP<T> {

    String getNow();
    String getClose();
    String getChange();
    T setChange(String change);
    String getChangeP();
    T setChangeP(String changeP);
}
