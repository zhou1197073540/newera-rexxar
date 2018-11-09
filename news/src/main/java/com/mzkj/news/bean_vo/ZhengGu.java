package com.mzkj.news.bean_vo;

import lombok.Data;

@Data
public class ZhengGu {
    private String date;
    private String ticker;
    private String stockname;
    private float chgprice;
    private float lastprice;
    private float lastrate;
    private float rate5;
    private float alpha;
    private float costprice;
    private float support;
    private float resistance;
    private String status;
}
