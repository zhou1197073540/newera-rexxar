package com.mouzhiapp.stock_market.bean;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.mouzhiapp.stock_market.repo.PO.LineDotPO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * create by zhouzhenyang on 2018/7/13
 */
@Data
@Accessors(chain = true)
public class StockIndex extends LineDotPO implements CalculateChangeAndChangeP<StockIndex> {

    private String code;
    @JsonAlias("full_code")
    private String fullCode;
    private String name;
    private String now;
    private String change;
    private String changeP;
    private String status;
    private String type;

}
