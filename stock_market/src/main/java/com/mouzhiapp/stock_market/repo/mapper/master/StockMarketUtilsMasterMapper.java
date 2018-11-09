package com.mouzhiapp.stock_market.repo.mapper.master;

import com.mouzhiapp.stock_market.repo.PO.CategoryPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * create by zhouzhenyang on 2018/5/22
 */
@Repository
public interface StockMarketUtilsMasterMapper {

    @Transactional
    @Insert("<script>insert into stock_industry (guid,industry) " +
            "values " +
            "<foreach collection='industries' item='industry' separator=','>" +
            " (#{industry.guid},#{industry.categoryValue}::jsonb) " +
            "</foreach>" +
            " on conflict(guid) do update " +
            "set industry = excluded.categoryValue " +
            "</script>")
    void doAddIndustries(@Param("industries") List<CategoryPO> industries) throws Exception;


    @Transactional
    @Insert("<script>insert into stock_theme (guid,theme) " +
            "values " +
            "<foreach collection='themes' item='theme' separator=','>" +
            " (#{theme.guid},#{theme.categoryValue}::jsonb) " +
            "</foreach>" +
            " on conflict(guid) do update " +
            "set theme = excluded.categoryValue " +
            "</script>")
    void doAddThemes(@Param("themes") List<CategoryPO> themes) throws Exception;
}
