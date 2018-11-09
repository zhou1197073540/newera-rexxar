package com.mzkj.news.mapper;

import com.mzkj.news.bean.*;
import com.mzkj.news.bean_vo.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public interface StockerMapper {

    @Transactional
    @Select("select * from stock_recommend_alpha ORDER BY \"index\" DESC limit 6")
    List<StockRecommendAlpha> selectStockRecommendLastOne();

    @Transactional
    @Select("select * from app_index_timing  " +
            "where ticker=#{code} and " +
            "datetime>(select substr(\"max\"(datetime),0,11) from app_index_timing) ORDER BY datetime")
    List<IndexTiming> getIndexTimimg(@Param("code") String code);

    @Transactional
    @Select("select index as date,cumalpha from stock_recommend_alpha ORDER BY \"index\"")
    List<CumAlpha> getCumAlpha();

    @Transactional
    @Select("select concat(date,' ',time) date_time,ticker,action,closeval,profit,cumprofit from sig_profit_record \n" +
            "where ticker=#{ticker} ORDER BY date_time DESC limit 500")
    List<GrailTime> grailTime(@Param("ticker") String ticker);

    @Transactional
    @Update(" update user_self_selected_stock set \"rank\"=#{rank}::INT  " +
            "where uid=#{uid} and code=#{code}")
    void updateStockRanking(@Param("uid") String uid,@Param("code") String code,@Param("rank") String rank);

    @Transactional
    @Select("select * from fof_weights ORDER BY \"index\" DESC limit 1")
    FofWeight selectFundGroupFromFofWeight();

    @Transactional
    @Select("SELECT fund_name from fund_information where fund_code=#{fund_code}")
    String selectFundName(@Param("fund_code") String fund_code);

    @Transactional
    @Select("select \"index\"::TIMESTAMP date_time,cumrate,bcumrate from fof_weights " +
            "where cumrate is NOT NULL or bcumrate is NOT null ORDER BY \"index\"")
    List<FundHistory> selectFundHistory();

    @Transactional
    @Select("select fi.fund_code,fi.fund_name,fw.* from fof_weights fw INNER JOIN fund_information fi  " +
            "ON fw.t1=fi.fund_code or fw.t2=fi.fund_code or fw.t3=fi.fund_code or fw.t4=fi.fund_code or fw.t5=fi.fund_code  " +
            "ORDER BY \"index\" DESC")
    List<FofWeight> selectAllFundGroupFromFofWeight();

    @Transactional
    @Select("select * from fund_information where fund_code=#{fund_code} limit 1")
    FundInfo selectFundInfo(@Param("fund_code")String fund_code);

    @Transactional
    @Select("select * from fund_history where fund_code=#{fund_code} ORDER BY date DESC LIMIT 90")
    List<FundNetValue> selectFundHistoryByCode(@Param("fund_code") String fund_code);

    @Transactional
    @Select("SELECT * FROM \"fund_change_rate\" where fund_code=#{fund_code} ORDER BY date desc limit 1")
    FundChange selectFundChange(@Param("fund_code") String fund_code);

    @Transactional
    @Select("SELECT stock code_type,* FROM trend_prediction where stock=#{code_type} order by date desc limit 1")
    StockPredictionVO selectStockTrendPrediction(@Param("code_type") String code_type);

    @Transactional
    @Select("select ff.* from  " +
            "(SELECT \"time\",\"close\",industry FROM industry_index_value  " +
            "where industry=#{industry_name} ORDER BY \"time\" DESC limit 200) ff  " +
            "ORDER BY ff.\"time\"")
    List<IndustryIndex> stockIndustryIndex(@Param("industry_name")String industry_name);

    @Update("update user_info set kline_level=kline_level+${experience} where uid=#{uid}")
    void saveKlineExperience(KPracticeVO vo);

    @Insert("INSERT INTO kline_practice" +
            "(uid,\"name\",full_code,profit,date_time,interval_profit,insert_time,robot_profit,difficulty_level) " +
            "VALUES" +
            "(#{uid},#{name},#{fullCode},#{profit},#{time},#{intervalProfit},now()::TEXT,#{robot_profit},#{difficulty_level})")
    void saveKlineResult(KPracticeVO vo);

    @Select("select phone_num,kline_level as experience from user_info where uid=#{uid}")
    KPracticeVO selectKlineExperience(KPracticeVO vo);

    @Select("select count(*) as total,(select count(*) from kline_practice where profit>#{profit}) as big_num " +
            "from kline_practice")
    Map<String,String> selectKlineBeatRate(KPracticeVO vo);

    @Select("select name,full_code fullCode,profit,interval_profit intervalProfit," +
            "date_time \"time\",robot_profit,difficulty_level " +
            "from kline_practice where uid=#{uid} order by insert_time desc limit 100")
    List<KPracticeVO> kLinePracticeSelfRecords(String uid);

    @Select("select aa.phone_num,bb.profit from user_info aa," +
            "(select DISTINCT(uid) uid,MAX(profit::FLOAT) profit from kline_practice where profit != 'NaN' GROUP BY uid) bb  " +
            "where aa.uid=bb.uid ORDER BY profit::FLOAT DESC")
    List<KlineRank> kLinePracticeAllRecords();

    @Select("select phone_num,(kline_level/500)+1 \"level\",kline_level experience from user_info " +
            "where uid=#{uid}")
    Map<String,String> kLineUserInfo(String uid);

    @Select("select uu.phone_num,ee.profit,ee.robot_profit,ee.difficulty_level from user_info uu," +
            "(select * from kline_practice aa where " +
            "profit::FLOAT = " +
            "(select max(bb.profit::FLOAT) from " +
            "kline_practice bb " +
            "where " +
            "aa.uid = bb.uid and " +
            "bb.profit != 'NaN' and " +
            "bb.difficulty_level=#{type} and "+
            "aa.difficulty_level=#{type} "+
            ")" +
            ") ee " +
            "where uu.uid=ee.uid ORDER BY ee.profit::FLOAT DESC")
    List<KlineRank> kLinePracticeAllRecordsByType(String type);

    @Select("select * from images_upload ORDER BY \"id\" desc limit 10")
    List<StockReport> selectLatestStockReport();

    @Select("select * from zhenggu  where ticker=#{code} limit 1")
    ZhengGu searchStockReport(String code);

    @Select("select * from ahk_realtime_price_ratio " +
            "where datetime=(SELECT \"max\"(datetime) from ahk_realtime_price_ratio) " +
            "ORDER BY price_ratio DESC limit ${page_size} OFFSET ${page_index}")
    List<AKShares> stockPriceRatio(@Param("page_size") int page_size, @Param("page_index")int page_index);

    @Select("select code,zybl,zysz,wxsgzys,xsgzys,c1 from stock_pledge_ratio " +
            "where code=#{code} ORDER BY tdate DESC LIMIT 1")
    PledgeRatio queryStockPledgeRatio(String code);

    @Select("select code,gdmc,gbbl,pcx,yjx,c2 from stock_pledge_gd where code=#{code}")
    List<PledgeGD> queryStockPledgeGD(String code);

    @Select("select aa.stockname code_name,bb.* from sw_stock_industry aa RIGHT JOIN " +
            "(select r.code code,r.zybl,r.zygs,r.c1,s.c2 from stock_pledge_ratio r INNER JOIN  " +
            "(select code,\"avg\"(c2) c2 from stock_pledge_gd where c2 is not null GROUP BY(code)) s ON  " +
            "r.code=s.code where r.tdate=(select max(tdate) from stock_pledge_ratio)) bb " +
            "on aa.ticker=bb.code ORDER BY zybl::float desc limit 50")
    List<StockPledgeRiskVO> stockPledgeRatioRisk();

    @Select("select * from hang_qing ORDER BY \"time\" DESC limit ${page_size} OFFSET ${page_index}")
    List<HangQing> stockHangQing(@Param("page_size") int page_size,@Param("page_index") int page_index);
}
