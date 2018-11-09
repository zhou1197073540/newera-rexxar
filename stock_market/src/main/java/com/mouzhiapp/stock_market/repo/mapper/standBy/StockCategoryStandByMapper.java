package com.mouzhiapp.stock_market.repo.mapper.standBy;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * create by zhouzhenyang on 2018/6/6
 */
@Repository
public interface StockCategoryStandByMapper {


    @Select("select guid as guid, (industry ->> 'name') as name, " +
            "(industry ->> 'changeP') as changep," +
            "((industry -> 'stocks') ->>0):: text as stocks " +
            "from stock_industry" +
            " where (industry ->> 'name') is not null " +
            " order by cast(industry ->> 'changeP' as float) desc ")
    List<Map<String, Object>> doGetIndustriesWithTopStock() throws Exception;


    @Select("select guid as guid, (theme ->> 'name') as name, " +
            "(theme ->> 'changeP') as changep," +
            "((theme -> 'stocks') ->>0):: text as stocks " +
            "from stock_theme " +
            " where (theme ->> 'name') is not null " +
            " order by cast(theme ->> 'changeP' as float) desc " +
            "limit 20")
    List<Map<String, Object>> doGetThemesWithTopStocks() throws Exception;


    @Select("SELECT small_buy::text AS smallBuy, small_sale::text AS smallSale, " +
            "medium_buy::text AS mediumBuy, " +
            "medium_sale::text AS mediumSale, large_buy::text AS largeBuy," +
            " large_sale::text AS largeSale, (industry -> 'stocks') AS stocks " +
            "FROM stock_industry where guid = #{guid} and (industry -> 'stocks') is not null")
    List<Map<String,Object>> doGetIndustryStocks(@Param("guid") String guid) throws Exception;

    @Select("SELECT small_buy::text AS smallBuy, small_sale::text AS smallSale, " +
            "medium_buy::text AS mediumBuy, " +
            "medium_sale::text AS mediumSale, large_buy::text AS largeBuy, " +
            "large_sale::text AS largeSale, " +
            "(theme -> 'stocks') AS stocks " +
            "FROM stock_theme where guid = #{guid} and (theme -> 'stocks') is not null")
    List<Map<String,Object>> doGetThemeStocks(String guid) throws Exception;
}
