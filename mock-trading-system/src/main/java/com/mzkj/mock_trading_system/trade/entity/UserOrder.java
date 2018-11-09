package com.mzkj.mock_trading_system.trade.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Property;

import java.math.BigDecimal;

/**
 * create by zhouzhenyang on 2018/7/26
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserOrder {

    String oid;
    @JsonAlias("user_account")
    String userAccount;
    @JsonAlias("full_code")
    String fullCode;
    String name;
    @JsonAlias("create_datetime")
    String datetime; //yyyy-MM-dd HH:mm:ss
    String price; //价格
    @JsonAlias("trade_price")
    String tradePrice; //成交价
    Integer amount; //数量
    String operation;  //买卖退
    String type; //A股票
    BigDecimal cashBack; //买 超过退款

}
