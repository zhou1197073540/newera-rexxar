package com.mouzhiapp.stock_market.repo.mapper.standBy;

import com.mouzhiapp.stock_market.repo.PO.LineDotPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * create by zhouzhenyang on 2018/5/22
 */
@Repository
public interface StockMarketWeekStandByMapper {

    @Select("select \"open\",\"close\",high,low,volume::text, datetime as \"datetime\" " +
            "from stock_weekly where code = #{code} and datetime between #{start} and #{end} order by datetime asc")
    List<LineDotPO> doGetWeekKLineByCodeAndDate(@Param("code") String code, @Param("start") LocalDate start, @Param("end") LocalDate end) throws Exception;
}
