package com.mzkj.news.bean_vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class CheckStock {
    private String code;
    private String code_type;
    private String stock_name;
    private String result="exist";

    public CheckStock() {
    }

    public CheckStock(String code, String result) {
        this.code = code;
        this.result = result;
    }
}
