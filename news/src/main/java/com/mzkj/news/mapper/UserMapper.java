package com.mzkj.news.mapper;

import com.mzkj.news.bean.*;
import com.mzkj.news.bean_vo.CheckStock;
import com.mzkj.news.bean_vo.StockUpDownVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserMapper {
    @Transactional
    @Insert("insert into user_info(uid,phone_num,password,type,last_login_time,username,a_account) " +
            "values (#{uid},#{phone_num},#{password},#{type},#{time},#{username},#{a_account})")
    void saveUser(User user);

    @Transactional
    @Select("select * from user_info where phone_num=#{phone_num}")
    User selectCountByPhone(@Param("phone_num") String phone_num);

    @Transactional
    @Select("select * FROM user_self_selected_stock where uid=#{uid} and code=#{code}")
    List<SelfSelectStock> selectCountByUidInSelfStock(@Param("uid")String uid,@Param("code")String code);

    @Select("select count(*) FROM user_self_selected_stock where uid=#{uid} and code=#{code}")
    Integer selectCountByUidInSelfStocks(@Param("uid")String uid,@Param("code")String code);

    @Transactional
    @Insert("insert into user_self_selected_stock(uid,code) values(#{uid},#{code})")
    void saveUserSelfStock(SelfSelectStock selfSelectStock);

    @Transactional
    @Update("update user_self_selected_stock set code=#{code} where uid=#{uid}")
    void updateUserSelfStock(SelfSelectStock stock);

    @Transactional
    @Select("select code code_type,\"rank\",warm_price,setup,send FROM user_self_selected_stock where uid=#{uid} ORDER BY \"rank\"")
    List<StockUpDownVO> selectListCodeByUidInSelfStock(@Param("uid")String uid);
    @Transactional
    @Select("select code FROM user_self_selected_stock where uid=#{uid} ORDER BY \"rank\"")
    List<String> selectStringListCodeByUidInSelfStock(@Param("uid")String uid);

    @Select("select code code_type,\"rank\" FROM user_self_selected_stock " +
            "where uid=#{uid} ORDER BY \"rank\" LIMIT 3")
    List<StockUpDownVO> selectListCodeByUidInSelfStockRandom(@Param("uid")String uid);

    @Transactional
    @Delete("delete from user_self_selected_stock where uid=#{uid} and code=#{code}")
    void delSelfSelectStock(SelfSelectStock stock);

    @Transactional
    @Insert("INSERT INTO user_thumbs_up(user_uid,news_uid,time,status) VALUES(#{user_uid},#{news_uid},now(),'1')")
    void addThumbsUp(@Param("user_uid")String user_uid, @Param("news_uid")String news_guid);

    @Transactional
    @Delete("DELETE from user_thumbs_up where user_uid=#{user_uid} and news_uid=#{news_uid}")
    void delThumbsUp(@Param("user_uid")String user_uid, @Param("news_uid")String news_guid);

    @Transactional
    @Select("select * from app_version limit 1")
    AppVersion selectAppVersion();

    @Transactional
    @Select("select title,content,datetime::TEXT,type,user_uid from app_message " +
            "where platform='app' and (type='com.mzkj.mock_trading_system.common' or user_uid=#{user_uid}) order by datetime desc limit 50")
    List<MessageCenter>  messageCenter(@Param("user_uid")String user_uid);

    @Transactional
    @Insert("insert into app_message(title,content,datetime,type,user_uid,platform) values " +
            "(#{title},#{content},#{datetime}::timestamp,#{type},#{user_uid},#{platform})")
    void saveAppMessage(MessageCenter messageCenter);

    @Transactional
    @Select("select count(*) from app_message where user_uid=#{user_uid}")
    int isExistMessage(@Param("user_uid")String user_uid);

    @Transactional
    @Select("select ticker code,ticker_type code_type,stockname \"name\" from sw_stock_industry " +
            "where ticker_type=#{ticker_type} limit 1")
    StockUpDownVO selectStockCodeAndName(@Param("ticker_type")String ticker_type);

    @Transactional
    @Update("update user_info set a_account=#{a_account} where uid=#{uid}")
    void updateAaccount(User us);

    @Transactional
    @Insert("INSERT INTO mock_stock_account_info (a_account,assets_available) " +
            "VALUES(#{a_account},'200000')")
    void saveMockStockAssets(@Param("a_account") String a_account);

    @Select("SELECT ticker as code,stockname stock_name,ticker_type as code_type " +
            "from sw_stock_industry " +
            "where ticker in(${codes})")
    List<CheckStock> checkStockExists(@Param("codes")String codes);

    @Select("select * from news_crawler where " +
            "type='news' and \"flag\"='1' and " +
            "insert_time>(SELECT now()::timestamp + '-1 month')::TEXT and " +
            "\"content\" !='' AND" +
            "\"content\" is not NULL " +
            "ORDER BY random() limit 10")
    List<News> queryTenNewsByRandom();

    @Select("select * from news_crawler where \"type\"='news' and \"flag\"='1' and \"content\" !=''  " +
            "ORDER BY publish_time DESC limit 10")
    List<News> queryTenNewsByRandoms();

    @Update("update user_self_selected_stock set " +
            "warm_price=${price},setup='1',send='0',warm_type=#{type} " +
            "where " +
            "uid=#{uid} and code=#{code}")
    Integer updateWarmPriceByUid(@Param("uid") String uid, @Param("code")String code,
                                 @Param("price")String price,@Param("type")String type);

    @Update("update user_self_selected_stock set setup='0',send='0' " +
            "where " +
            "uid=#{uid} and code=#{code}")
    Integer cancelStockWarning(@Param("uid") String uid,@Param("code")  String code);
}
