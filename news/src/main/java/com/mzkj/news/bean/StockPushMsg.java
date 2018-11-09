package com.mzkj.news.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StockPushMsg {
    private String app_token;//根据此token推送安卓消息
    private String code_type;    //股票全称
    private String stock_name; //股票名称
    private float warm_price;//设置的提醒价格
    private String setup; //是否设置
    private String send;//是否发送
    private String uid; //用户uid
    private String warm_type;//高价提醒还是提价提醒

}
