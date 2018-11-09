package com.mzkj.mock_trading_system.trade.repo.mapper.stock_data;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * create by zhouzhenyang on 2018/7/29
 */
@Repository
public interface StockMapper {

    @Select("select 1 from trading_days where date = #{now}::date")
    String doCheckIsTradingDate(String now) throws Exception;
}
