package com.mzkj.news.mapper;

import com.mzkj.news.bean.News;
import com.mzkj.news.bean.Stock;
import com.mzkj.news.bean.StockRanking;
import com.mzkj.news.bean.StockRecommend;
import com.mzkj.news.bean_vo.PointsPraise;
import com.mzkj.news.bean_vo.StockUpDownVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NewsMapper {

    @Transactional
    @Select("select * from news_crawler order by publish_time desc limit ${page_size} OFFSET ${page_index}")
    List<News> selectNewsByPublishTime( @Param("page_size")int page_size,@Param("page_index") int page_index);

    @Transactional
    @Select("select * from news_crawler where type=#{type} and flag='1' order by publish_time desc limit ${page_size} OFFSET ${page_index}")
    List<News> selectNewsByTypePublishTime(@Param("page_size")int page_size,@Param("page_index") int page_index,@Param("type") String type);

    @Transactional
    @Select("select \"content\" from news_crawler_content where guid=#{guid} limit 1")
    String selectNewsContentByGuid(@Param("guid")String guid);

    @Transactional
    @Select("select count(*) from sw_industry_type where ticker_type in (${ticker})")
    Integer selectStockNumByCondition(@Param("ticker")String ticker);

    @Transactional
    @Select("select ticker code,ticker_type as code_type,stockname as name from sw_industry_type where " +
            "ticker_type in (${ticker}) order by ticker limit ${page_size} OFFSET ${page_index}")
    List<Stock> selectStockByCode(@Param("ticker")String ticker,@Param("page_size")int page_size,@Param("page_index") int page_index);

    @Transactional
    @Select("select ticker code,ticker_type as code_type,stockname as name from sw_industry_type order by ticker " +
            "limit ${page_size} OFFSET ${page_index}")
    List<Stock> selectStockByPage( @Param("page_size")int page_size,@Param("page_index") int page_index);

    @Transactional
    @Select("select ticker_type as code,stockname as name from sw_industry_type ORDER BY random() limit 10")
    List<Stock> selectStockDefault();

    @Transactional
    @Select("select count(*) from sw_industry_type")
    Integer selectStockNum();

    @Transactional
    @Select("select r.ticker code,round(r.\"PErank\"::numeric,2) perank,round(r.\"PBrank\"::numeric,2) pbrank,round(r.\"Frank\"::numeric,2) frank, " +
            "round(r.\"ROErank\"::numeric,2) roerank,round(r.\"Qrank\"::numeric,2) qrank,e.evaluation,r.\"tradedate\"  " +
            "from stock_daily_rank r INNER JOIN stock_daily_eval e on r.ticker=e.ticker  " +
            "where r.ticker=#{ticker} ORDER BY r.\"tradedate\" desc LIMIT 1")
    List<StockRanking> analysisRatingByCode(@Param("ticker")String ticker);

    @Transactional
    @Select("select ticker code,round(sigval::numeric,2) sigval,round(\"close\"::numeric,2) \"close\",round(atr::numeric,2) atr,strategyid,datetime " +
            "from stockrecommend where datetime=(select MAX(datetime) from stockrecommend)")
    List<StockRecommend> stockRecommend();

    @Transactional
    @Select("SELECT s.stockname \"name\",t.* from sw_industry_type s RIGHT JOIN " +
            "(select ticker code,round(sigval::numeric,2) sigval,round(\"close\"::numeric,2) \"close\",round(atr::numeric,2) atr,strategyid,datetime from stockrecommend where datetime=(select MAX(datetime) from stockrecommend)) t " +
            "on s.ticker=t.code")
    List<StockRecommend> stockRecommends();

    @Transactional
    @Select("select " +
            "(select count(*) FROM user_thumbs_up where news_uid=#{news_guid}) points, " +
            "(select count(*) from user_thumbs_up where user_uid=#{user_uid} and news_uid=#{news_guid}) praise  " +
            "from user_thumbs_up limit 1")
    PointsPraise selectPointsAndPraise(@Param("user_uid") String uid, @Param("news_guid")String news_guid);

    @Transactional
    @Select("select ticker from stockrecommend where datetime=(select MAX(datetime) from stockrecommend)")
    List<String> selectStockRecommend();
    @Transactional
    @Select("select ticker code,ticker_type code_type from sw_stock_industry where ticker in " +
            "(select ticker from stockrecommend where datetime=(select MAX(datetime) from stockrecommend))")
    List<StockUpDownVO> selectStockRecommends();

    @Select("select * from news_analysis aa JOIN news_crawler cc ON aa.news_guid=cc.guid " +
            "where union_code LIKE #{code} LIMIT 5")
    List<News> queryUnionNewsByCode(String code);
}
