package com.mouzhiapp.stock_market.repo.mapper.standBy;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * create by zhouzhenyang on 2018/5/22
 */
@Repository
public interface StockMarketOriginStandByMapper {

    @Select("select code,c_value,extract(epoch from datetime)::text AS datetime " +
            "from( SELECT code, c_value :: VARCHAR, datetime FROM " +
            "${table} WHERE code = #{code} ORDER BY datetime DESC LIMIT 1) t")
    Map<String, Object> doGetLatestOriginByCode(@Param("table") String table, @Param("code") String code) throws Exception;

    @Select("SELECT code,c_value,datetime FROM" +
            "( SELECT code,c_value::varchar,datetime::text, " +
            "ROW_NUMBER () OVER ( PARTITION BY code ORDER BY datetime DESC) rn " +
            "FROM ${table} WHERE datetime >= #{time}::timestamp with time zone ) T WHERE T.rn = 1")
    List<Map<String, Object>> doGetLatestOrigin(@Param("table") String table, @Param("time") String time);

    @Select("SELECT code,c_value,datetime FROM" +
            "( SELECT code,c_value::varchar,datetime::text, " +
            "ROW_NUMBER () OVER ( PARTITION BY code ORDER BY datetime DESC) rn " +
            "FROM stock_index_origin_latest ) T WHERE T.rn = 1")
    List<Map<String,Object>> doGetLatestOriginIndexes() throws Exception;
}
