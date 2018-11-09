package com.mzkj.news.secondMapper;

import com.mzkj.news.bean.StockInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StockMapper {

    @Transactional
    @Select("SELECT " +
            "code,c_value :: VARCHAR,datetime :: TEXT  " +
            "FROM " +
            "stock_market_origin_${date} " +
            "WHERE " +
            "code=#{code} " +
            "ORDER BY " +
            "datetime DESC " +
            "LIMIT 1")
    StockInfo findStockInfo(@Param("code")String code, @Param("date")String date);
}
