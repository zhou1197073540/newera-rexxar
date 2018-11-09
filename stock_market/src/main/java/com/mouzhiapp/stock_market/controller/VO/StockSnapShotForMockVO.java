package com.mouzhiapp.stock_market.controller.VO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.mouzhiapp.stock_market.bean.CalculateChangeAndChangeP;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * create by zhouzhenyang on 2018/8/2
 */
@Data
@Accessors(chain = true)
public class StockSnapShotForMockVO implements CalculateChangeAndChangeP<StockSnapShotForMockVO> {

    private String name;
    private String now;
    private String close;
    private String bid1_volume;
    private String bid1;
    private String bid2_volume;
    private String bid2;
    private String bid3_volume;
    private String bid3;
    private String bid4_volume;
    private String bid4;
    private String bid5_volume;
    private String bid5;
    private String ask1_volume;
    private String ask1;
    private String ask2_volume;
    private String ask2;
    private String ask3_volume;
    private String ask3;
    private String ask4_volume;
    private String ask4;
    private String ask5_volume;
    private String ask5;
    @JsonAlias("full_code")
    private String fullCode;
    private String code;
    private String status;
    private String type;
    private String change;
    private String changeP;

}
