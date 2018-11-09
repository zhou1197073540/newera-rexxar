package com.mzkj.xiaoxin.repo.PO;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * create by zhouzhenyang on 2018/9/19
 */
@Data
@Accessors(chain = true)
public class StockBasicInfoPO {
    private String name;
    private String now;
    private String open;
    private String close;
    private String high;
    private String low;
    private String buy;
    private String sell;
    private String turnover;
    private String volume;
    private String date;
    private String time;
    private String fullCode;
    private String code;
    private String status;
    private String type;
}
