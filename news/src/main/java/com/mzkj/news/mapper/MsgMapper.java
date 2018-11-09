package com.mzkj.news.mapper;

import com.mzkj.news.bean.MsgPush;
import com.mzkj.news.bean.MsgPushNewsVO;
import com.mzkj.news.bean.Stock;
import com.mzkj.news.bean.StockPushMsg;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MsgMapper {

    @Insert("INSERT INTO app_push_msg(app_token,user_account,platform) " +
            "values(#{token},#{account},#{platform}) " +
            "ON conflict (user_account) " +
            "do UPDATE set app_token=#{token}")
    Integer saveOrUpdatePushMsg(MsgPush msg);

    @Select("select sw.stockname stock_name,bb.* from sw_stock_industry sw," +
            "(select aa.app_token,uu.uid,uu.code code_type,uu.warm_price,uu.setup,uu.send,uu.warm_type " +
            "from app_push_msg aa LEFT JOIN user_self_selected_stock uu " +
            "ON aa.user_account=uu.uid  " +
            "where  uu.warm_price>0 and aa.platform='android' and uu.setup='1' and uu.send='0') bb " +
            "where sw.ticker_type=bb.code_type")
    List<StockPushMsg> queryAllStockBySetPrice();

    @Select("select stockname \"name\",ticker code,ticker_type code_type from sw_stock_industry " +
            "where ticker_type=#{codeType}")
    Stock selectStockNameByCodeType(String codeType);

    @Update("update user_self_selected_stock set " +
            "send='1' " +
            "where uid=#{uid} and code=#{code_type}")
    void updateSended(StockPushMsg sp);

    @Select("select aa.app_token token,uu.uid account,uu.code from app_push_msg aa JOIN user_self_selected_stock uu " +
            "ON aa.user_account=uu.uid where app_token!='' and code like #{code}")
    MsgPushNewsVO selectPushNewsCode(String code);
}
