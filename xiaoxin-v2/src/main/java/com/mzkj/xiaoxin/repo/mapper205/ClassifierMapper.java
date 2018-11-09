package com.mzkj.xiaoxin.repo.mapper205;

import com.mzkj.xiaoxin.repo.PO.SearchStockPO;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * create by zhouzhenyang on 2018.10.11
 */
@Repository
public interface ClassifierMapper {

    @Select("select ticker_type,pinyin_firstword,ticker,stockname from sw_stock_industry" +
            " where \"startDate\" is not null")
    @Results({
            @Result(column = "ticker_type", property = "fullCode"),
            @Result(column = "pinyin_firstword", property = "pinyinFirstWord"),
            @Result(column = "ticker", property = "code"),
            @Result(column = "stockname", property = "name")
    })
    List<SearchStockPO> doGetSearchStockWords() throws Exception;
}
