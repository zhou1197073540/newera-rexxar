package com.mzkj.usermock.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockMarketSnapshotPO {
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
    private String date;
    private String time;
    private String full_code;
    private String code;
    private String status;
}
