package com.mzkj.usermock.mapper;

import com.mzkj.usermock.bean.StockAccountInfo;
import com.mzkj.usermock.bean.StockOperation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AAccountMapper {

    @Transactional
    @Select("select * from mock_stock_account_info where a_account=#{account}")
    StockAccountInfo getAccountInfo(@Param("account") String account);

    @Transactional
    @Insert("INSERT INTO mock_stock_account_info (a_account,assets_available) " +
            "VALUES(#{a_account},#{assets_available})")
    void saveStockAccount(StockAccountInfo stockTradingAccount);

    @Transactional
    @Update("update mock_stock_account_info set assets_available=#{assets_available} " +
            "where a_account=#{a_account}")
    Integer updateAvaliableMoney(StockAccountInfo stock);

    @Transactional
    @Update("update mock_stock_operation set trading_status=#{trading_status} " +
            "where order_num=#{order_num}")
    void updateTradingStatus(StockOperation stock);

}
