package com.mzkj.mock_trading_system.trade.repo.mapper.ow;

import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import com.mzkj.mock_trading_system.trade.valobj.MockStockUserOrderPO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * create by zhouzhenyang on 2018/7/31
 */
@Repository
public interface StockOrderMapper {

    @Insert("<script>" +
            "insert into mock_stock_finished_with_error_order " +
            "(oid,user_account,name,full_code,price,amount,operation,type,order_status," +
            "create_datetime,trade_price) values " +
            "<foreach separator=',' collection='uOrders' item='uOrder'>" +
            "(#{uOrder.oid},#{uOrder.userAccount},#{uOrder.name},#{uOrder.fullCode},#{uOrder.price}::numeric(20,4)," +
            "#{uOrder.amount},#{uOrder.operation},#{uOrder.type},#{status},#{uOrder.datetime}::timestamp with time zone,#{uOrder.tradePrice}::numeric(20,4)) " +
            "</foreach>" +
            "on conflict(oid) do nothing" +
            "</script>")
    void doCreateFinishedWithErrorOrder(@Param("uOrders") List<UserOrder> order, @Param("status") String orderStatus) throws Exception;

    @Insert("insert into mock_stock_user_order " +
            "(oid,user_account,name,full_code,price,amount,operation,type,order_status," +
            "create_datetime,trade_datetime,trade_price) values " +
            "(#{uOrder.oid},#{uOrder.userAccount},#{uOrder.name},#{uOrder.fullCode},#{uOrder.price}::numeric(20,4)," +
            "#{uOrder.amount},#{uOrder.operation},#{uOrder.type},#{status}," +
            "#{uOrder.datetime}::timestamp with time zone," +
            "#{tradeTime}," +
            "#{uOrder.tradePrice}::numeric(20,4)) " +
            "on conflict(oid) do nothing")
    void doCreateOrder(@Param("uOrder") UserOrder order,
                       @Param("tradeTime") LocalDateTime tradeTime,
                       @Param("status") String orderStatus) throws Exception;

    @Select("select order_status from mock_stock_user_order where oid = #{oid}")
    String doGetOrderStatusByOid(@Param("oid") String oid) throws Exception;

    @Update("update mock_stock_user_order set order_status = #{status} where oid = #{oid}")
    void doUpdateOrderStatusByOid(@Param("oid") String oid, @Param("status") String status) throws Exception;

    @Select("<script> select order_status as orderStatus,oid from mock_stock_user_order where oid in " +
            "<foreach open='(' separator=',' close=')' collection='userOrders' item='userOrder'>" +
            "#{userOrder.oid}" +
            "</foreach>" +
            "</script>")
    List<MockStockUserOrderPO> doGetOrderStatusByOids(@Param("userOrders") List<UserOrder> userOrders);

    @Update("<script>update mock_stock_user_order set order_status = #{status} where oid in " +
            "<foreach open='(' separator=',' close=')' collection='userOrders' item='userOrder'>" +
            "#{userOrder.oid}" +
            "</foreach>" +
            "</script>")
    void doUpdateOrderStatusByOids(@Param("userOrders") List<UserOrder> userOrders, @Param("status") String status);

    @Transactional
    @Update("<script>update mock_stock_user_order as mm set" +
            " order_status = #{status}, " +
            " trade_datetime = #{tradeTime}::timestamp with time zone, " +
            "trade_price = tt.tradePrice::numeric(20,4) from (" +
            " values " +
            "<foreach separator=',' collection='userOrders' item='userOrder'>" +
            "(#{userOrder.oid},#{userOrder.tradePrice})" +
            "</foreach>" +
            ")" +
            " as tt (oid,tradePrice)" +
            " where mm.oid = tt.oid" +
            "</script>")
    void doUpdateTradeOrderByOids(
            @Param("userOrders") List<UserOrder> finishedOrders,
            @Param("status") String finish,
            @Param("tradeTime") String tradeTime);


    @Select("select * from mock_stock_finished_with_error_order")
    @Results({
            @Result(column = "user_account", property = "userAccount"),
            @Result(column = "full_code", property = "fullCode"),
            @Result(column = "create_datetime", property = "datetime"),
            @Result(column = "trade_price", property = "tradePrice")
    })
    List<UserOrder> doGetErrorOrders(@Param("datetime") LocalDateTime datetime);

    @Update("<script>" +
            "update mock_stock_account_info as mm set " +
            "assets_available = (assets_available::decimal + tt.cashBack)::varchar " +
            "from (values " +
            "<foreach separator=',' collection='cashBackOrders' item='userOrder'>" +
            "(#{userOrder.userAccount},#{userOrder.cashBack})" +
            "</foreach>" +
            ")" +
            "as tt (account,cashBack) " +
            "where mm.a_account = tt.account" +
            "</script>")
    void doUpdateUserCashBack(@Param("cashBackOrders") List<UserOrder> cashBackOrders);

    @Delete("delete from mock_stock_finished_with_error_order")
    void doDeleteErrorOrders();

    @Select("select * from mock_stock_user_order where order_status = #{status}")
    @Results({
            @Result(column = "user_account", property = "userAccount"),
            @Result(column = "full_code", property = "fullCode"),
            @Result(column = "create_datetime", property = "datetime"),
            @Result(column = "trade_price", property = "tradePrice")
    })
    List<UserOrder> doGetOrdersByStatus(@Param("status") String status);


    @Delete("with latest_orders as ( " +
            "delete from mock_stock_user_order where create_datetime <= #{datetime} " +
            "and order_status = #{status} " +
            "returning * " +
            ") " +
            "insert into mock_stock_user_order_history select * from latest_orders;")
    void doDeleteLatestUserOrdersByTimeAndStatus(
            @Param("datetime") LocalDateTime dateTime,
            @Param("status") String orderStatus);
}
