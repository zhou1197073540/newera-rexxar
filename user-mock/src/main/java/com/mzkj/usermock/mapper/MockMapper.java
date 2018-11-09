package com.mzkj.usermock.mapper;

import com.mzkj.usermock.bean.StockAccountInfo;
import com.mzkj.usermock.bean.StockOperation;
import com.mzkj.usermock.bean_rmi.StockOrderRMI;
import com.mzkj.usermock.bean_vo.PositionVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MockMapper {
    @Transactional
    @Insert("INSERT INTO mock_stock_operation(a_account,order_num,stock_name,code_type,trading_status,operation,num,price,datetime) " +
            "VALUES(#{a_account},#{order_num},#{stock_name},#{code_type},'wait',#{operation},#{num},#{price},#{datetime})")
    void saveStockOperation(StockOperation stock);

    @Transactional
    @Insert("INSERT INTO mock_stock_wallet_log(a_account,assets_change,datetime) VALUES" +
            "(#{a_account},#{assets_change},#{datetime})")
    void saveStockWalletLog(@Param("a_account") String account,@Param("assets_change") String assets_change,@Param("datetime") String datetime);

    @Transactional
    @Select("select * from mock_stock_settlement where a_account=#{a_account} and \"position\">'0'")
    List<StockOperation> selectListPositionStock(@Param("a_account") String account);

    @Select("select * from mock_stock_settlement where a_account=#{a_account} and \"position\">'0'")
    List<PositionVO> selectListPositionStocks(@Param("a_account") String account);

    @Transactional
    @Select("select * from mock_stock_settlement where a_account=#{a_account} and code_type=#{code_type}")
    StockOperation selectOnePositionStock(@Param("a_account") String account,@Param("code_type") String code_type);

    @Transactional
    @Select("select * from mock_stock_account_info where a_account=#{a_account}")
    StockAccountInfo selectStockAccountInfo(@Param("a_account") String account);

    @Transactional
    @Select("select * from mock_stock_settlement where a_account=#{userAccount} and code_type=#{fullCode}")
    StockOperation selectCostByAccount(StockOrderRMI stock);

    @Transactional
    @Select("insert into mock_stock_settlement(a_account,stock_name,code_type,position,position_available,cost,settlement_time)" +
            "values(#{userAccount},#{name},#{fullCode},#{amount},'0',#{tradePrice},#{datetime})")
    void saveSettlementCost(StockOrderRMI stock);

    @Transactional
    @Update("update mock_stock_settlement set " +
            "position=#{position}," +
            "cost=#{cost},settlement_time=#{settlement_time} " +
            "where a_account=#{a_account} and code_type=#{code_type}")
    Integer updateSettleCost(StockOperation op);

    @Transactional
    @Update("update mock_stock_settlement set " +
            "position_available=#{position_available} " +
            "where a_account=#{a_account} and code_type=#{code_type}")
    Integer updateAvaiPosition(StockOperation op);

    @Update("UPDATE mock_stock_settlement set position_available=\"position\" WHERE \"id\" in  " +
            "(select ss.\"id\" from mock_stock_settlement ss LEFT JOIN mock_stock_user_order oo  " +
            "on ss.a_account=oo.user_account and ss.code_type=oo.full_code  " +
            "where \"position\"::INT>0 and \"position\"::INT>position_available::INT  " +
            "and oo.\"operation\"='buy' and oo.order_status='finish'  " +
            "and oo.trade_datetime::TEXT>(select to_char((SELECT now()),'yyyy-mm-dd')) " +
            "GROUP BY \"id\")")
    void updateAllUserAvaiPosition();

    @Transactional
    @Select("select oid,user_account userAccount,name,full_code fullCode,price,amount,\"operation\",\"type\" " +
            "from mock_stock_user_order where oid=#{oid}")
    StockOrderRMI selectResultByOrderNum(@Param("oid") String oid);


    @Transactional
    @Select("select oid,user_account userAccount,name,full_code fullCode," +
            "trade_price price,amount,\"operation\",\"type\",order_status,create_datetime datetime " +
            "from mock_stock_user_order where user_account=#{account}  " +
            "and (create_datetime::TEXT>(select to_char((SELECT now()),'yyyy-mm-dd')) or order_status='wait') " +
            "ORDER BY create_datetime DESC")
    List<StockOrderRMI> selectResultByAccount(@Param("account")String account);

    @Transactional
    @Update("update mock_stock_operation set trading_status=#{trading_status},settlement_time=now()::TEXT " +
            "where order_num=#{order_num}")
    Integer updateTradingStatusFinish(@Param("order_num") String order_num,@Param("trading_status") String trading_status);

    @Transactional
    @Select("select * from mock_stock_user_order " +
            "where user_account=#{account} and order_status='wait' and \"operation\"='buy'")
    List<StockOrderRMI> selectWaitSettleBuyStock(@Param("account")String account);

    @Delete("DELETE from mock_stock_settlement where \"position\"='0' and position_available='0'")
    void deleteInvalidStock();

    @Update("UPDATE mock_stock_settlement " +
            "set position_available=\"position\" " +
            "WHERE \"id\" in " +
            "(select \"id\" from mock_stock_settlement " +
            "where \n" +
            "\"position\"::INT8>position_available::INT8" +
            ")")
    void updateAllAvaiPosition();

    @Select("select \"count\"(*) from mock_stock_operation where " +
            "order_num=#{order_num} and trading_status='wait'")
    Integer checkSettle(String order_num);
}
