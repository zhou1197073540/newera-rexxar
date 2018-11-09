package com.mzkj.xiaoxin.repo.mapper205;

import com.mzkj.xiaoxin.repo.PO.ZhengguPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * create by zhouzhenyang on 2018/9/18
 */
@Repository
public interface ZhengguMapper {


    @Select("select * from zhenggu " +
            "where ticker = #{ticker} order by date desc limit 1")
    @Results({
            @Result(column = "costprice", property = "costPrice")
    })
    ZhengguPO getLatestZhengguByTicker(@Param("ticker") String ticker);



}
