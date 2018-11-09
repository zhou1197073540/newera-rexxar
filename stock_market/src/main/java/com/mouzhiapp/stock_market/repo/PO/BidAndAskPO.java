package com.mouzhiapp.stock_market.repo.PO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * create by zhouzhenyang on 2018/5/22
 */
@Data
@Accessors(chain = true)
public class BidAndAskPO {

    private String ask1;
    private String ask2;
    private String ask3;
    private String ask4;
    private String ask5;
    private String bid1;
    private String bid2;
    private String bid3;
    private String bid4;
    private String bid5;
    private String ask1_volume;
    private String ask2_volume;
    private String ask3_volume;
    private String ask4_volume;
    private String ask5_volume;
    private String bid1_volume;
    private String bid2_volume;
    private String bid3_volume;
    private String bid4_volume;
    private String bid5_volume;
    private String close; //上一个交易日的close
}
