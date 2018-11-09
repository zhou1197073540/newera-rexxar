package com.mzkj.news.bean_vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KPracticeVO {
    private String phone_num;
    private String name;
    private String fullCode ;
    private String experience ;
    private String profit ;
    private String intervalProfit ;
    private String time ;
    private String level;
    private String defeat_people;
    private String robot_profit;
    private String difficulty_level;
}
