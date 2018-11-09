package com.mouzhiapp.stock_market.repo.mapper.standBy;

import com.mouzhiapp.stock_market.repo.PO.LineDotPO;
import com.mouzhiapp.stock_market.bean.TimelineDot;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * create by zhouzhenyang on 2018/5/22
 */
@Repository
public interface StockMarketMinStandByMapper {

    @Select("select price,volume::text,to_char(datetime :: time,'HH24:MI') as datetime " +
            "from ${table} where code = #{code}" +
            " and datetime >= #{time}::timestamp with time zone " +
            " order by datetime asc")
    List<TimelineDot> doGetTimeLineByCode(@Param("table") String table,
                                          @Param("code") String code,
                                          @Param("time") String time) throws Exception;

    @Select("select open as open,price as close,high as high,low as low,volume::text as volume,datetime as datetime " +
            "from ${table} where code = #{code} order by datetime desc limit 1")
    LineDotPO doGetLatestDayKlineDotByCode(@Param("table") String table, @Param("code") String code);
}
