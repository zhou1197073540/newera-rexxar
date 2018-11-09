package com.mzkj.news.bean_vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class KlinePractice extends KPracticeVO{
    private String token;
    private String uid;
}
