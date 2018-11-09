package com.mzkj.news.bean_vo;

import lombok.Data;

import java.util.List;

@Data
public class StockPledgeRatioVO {
    private String code;
    private String code_name;
    private String price;
    private float avg_c2;
    private List<PledgeGD> gds;
    private PledgeRatio ratio;

    public StockPledgeRatioVO() {
    }
    public StockPledgeRatioVO(String code, float avg_c2, String price,String code_name,List<PledgeGD> gds, PledgeRatio ratio) {
        this.code = code;
        this.avg_c2 = avg_c2;
        this.gds = gds;
        this.ratio = ratio;
        this.price=price;
        this.code_name=code_name;
    }
}
