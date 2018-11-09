package com.mouzhiapp.stock_market.repo.mapper.standBy;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * create by zhouzhenyang on 2018/5/22
 */
@Repository
public interface StockMarketUtilsStandByMapper {

    @Select("SELECT to_regclass(#{table}) is not null")
    boolean doCheckTableExist(@Param("table") String table) throws Exception;

    @Select("select date from trading_days where date < #{now}::date order by date desc limit 1")
    String doGetLastTradingDate(String now) throws Exception;

    @Select("select 1 from trading_days where date = #{now}::date")
    String doCheckIsTradingDate(String now) throws Exception;
}
