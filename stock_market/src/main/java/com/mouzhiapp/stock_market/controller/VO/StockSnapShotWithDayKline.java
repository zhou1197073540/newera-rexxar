package com.mouzhiapp.stock_market.controller.VO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * create by zhouzhenyang on 2018/8/17
 */
@Data
@Accessors(chain = true)
public class StockSnapShotWithDayKline {

    private String fullCode;
    private String code;
    private String name;
    private String dayKLine;
    private String startDate;
    private String endDate;
}
