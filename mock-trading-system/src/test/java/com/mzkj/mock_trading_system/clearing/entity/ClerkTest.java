package com.mzkj.mock_trading_system.clearing.entity;

import com.netflix.discovery.converters.Auto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;


/**
 * create by zhouzhenyang on 2018/8/24
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class ClerkTest {

    @Autowired
    Clerk clerk;

    @Test
    public void cleanUserOrder() {
        clerk.cleanUserOrder();
    }
}