package com.mzkj.news.bean_vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;
import org.jboss.logging.Field;

@Data
@Accessors(chain = true)
public class StockPredictionVO {
    private String code_type;
    private String date;
    private String future_1;
    private String future_2;
    private String future_3;
    private String future_4;
    private String future_5;
    private String past_1;
    private String past_2;
    private String past_3;
    private String past_4;
    private String past_5;
    private String past_6;
    private String past_7;
    private String past_8;
    private String past_9;
    private String past_10;
    private String past_1_date;
    private String past_2_date;
    private String past_3_date;
    private String past_4_date;
    private String past_5_date;
    private String past_6_date;
    private String past_7_date;
    private String past_8_date;
    private String past_9_date;
    private String past_10_date;


}
