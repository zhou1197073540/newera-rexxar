package com.mzkj.mock_trading_system.trade.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * create by zhouzhenyang on 2018/8/23
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class TraderTest {

    @Autowired
    Trader trader;

    @Test
    public void doTrade() throws Exception {
        trader.doTrade();
    }
}