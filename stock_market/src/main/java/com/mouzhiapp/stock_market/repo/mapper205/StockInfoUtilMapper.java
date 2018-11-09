package com.mouzhiapp.stock_market.repo.mapper205;

import com.mouzhiapp.stock_market.bean.StockCategory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * create by zhouzhenyang on 2018/5/31
 */
@Repository("StockInfoUtilMapper")
public interface StockInfoUtilMapper {

    @Select("select per as pe,pb,turnoverratio from stockrealinfo" +
            " where code = #{code} order by datetime desc")
    Map<String, Object> doGetLatestStockIndexByCode(@Param("code") String code) throws Exception;

    @Select("select industry as category,array_to_string(array_agg(ticker), ',') as tickers from " +
            "sw_industry_type where industry is not null group by industry")
    List<StockCategory> doGetIndustryCategoryStocks() throws Exception;

    @Select("select theme as category,array_to_string(array_agg(ticker), ',') as tickers from stock_theme_related group by theme")
    List<StockCategory> doGetThemeCategoryStocks() throws Exception;

}
