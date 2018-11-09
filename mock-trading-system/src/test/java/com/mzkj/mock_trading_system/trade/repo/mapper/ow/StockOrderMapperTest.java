package com.mzkj.mock_trading_system.trade.repo.mapper.ow;

import com.mzkj.mock_trading_system.common.consts.Consts;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


/**
 * create by zhouzhenyang on 2018/8/6
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class StockOrderMapperTest {

    @Autowired
    StockOrderMapper stockOrderMapper;

    @Test
    public void doUpdateTradeOrderByOids() {
        List<UserOrder> userOrders = Arrays.asList(
                new UserOrder().setTradePrice("100.01")
                        .setOid("2018080211313801691973483483"),
                new UserOrder().setTradePrice("200.01")
                        .setOid("2018080211315519191541210341"),
                new UserOrder().setTradePrice("300.01")
                        .setOid("2018080314432292699906611562")
        );
        stockOrderMapper.doUpdateTradeOrderByOids(
                userOrders, Consts.FINISH, "2020-12-31 15:00"
        );
    }

    @Test
    public void doCreateOrder() throws Exception {
        UserOrder userOrder = new UserOrder()
                .setOid("fuck")
                .setUserAccount("hehehehe")
                .setType("A")
                .setAmount(100)
                .setDatetime("2030-01-01 15:00")
                .setFullCode("sh600000")
                .setPrice("100.01")
                .setTradePrice("222.22")
                .setName("浦发银行")
                .setOperation(Consts.OPERATION_BUY);
        try {
            stockOrderMapper.doCreateOrder(userOrder,
                    null,
                    Consts.WAIT);
        } catch (Exception e) {
            System.out.println("fuck!");
            e.printStackTrace();
        }
    }

    @Test
    public void doGetOrdersByStatus() {
        final List<UserOrder> userOrders = stockOrderMapper.doGetOrdersByStatus(Consts.WAIT);
        userOrders.forEach(System.out::println);
    }
}