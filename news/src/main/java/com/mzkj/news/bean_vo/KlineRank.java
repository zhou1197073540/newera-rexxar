package com.mzkj.news.bean_vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class KlineRank {
    private String phone_num;
    private String profit ;
    private String robot_profit;
    private String difficulty_level;
}
