package com.mouzhiapp.stock_market.repo.mapper.standBy;

import com.mouzhiapp.stock_market.repo.PO.LineDotPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * create by zhouzhenyang on 2018/5/22
 */
@Repository
public interface StockMarketDayStandByMapper {

    @Select("select \"open\",\"close\",high,low,volume::text,datetime as \"datetime\" " +
            "from ${table} where datetime between #{start}::timestamp with time zone and #{end}::timestamp with time zone order by datetime asc")
    List<LineDotPO> doGetDayKLineByCodeAndDate(@Param("table") String table, @Param("start") String start, @Param("end") String end) throws Exception;
}
