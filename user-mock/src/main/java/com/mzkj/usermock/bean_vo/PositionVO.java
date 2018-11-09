package com.mzkj.usermock.bean_vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PositionVO {
    private String stock_name;
    private String code_type;
    private String position;
    private String position_available;

    @Override
    public String toString() {
        return "PositionVO{" +
                "stock_name='" + stock_name + '\'' +
                ", code_type='" + code_type + '\'' +
                ", position='" + position + '\'' +
                ", position_available='" + position_available + '\'' +
                '}';
    }
}
