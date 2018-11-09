package com.mzkj.usermock.utils;

import com.mzkj.usermock.bean.StockAccountInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class RedisUtilTest {
    @Test
    public void test(){
        boolean res=RedisUtil.tryGetDistributedLock("zcxx");
        System.out.println("res:"+res);
    }

}