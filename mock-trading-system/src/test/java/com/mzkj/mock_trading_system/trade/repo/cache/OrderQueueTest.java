package com.mzkj.mock_trading_system.trade.repo.cache;

import com.mzkj.mock_trading_system.common.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.mzkj.mock_trading_system.common.consts.Consts.A_TRADING_QUEUE;


/**
 * create by zhouzhenyang on 2018/8/9
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class OrderQueueTest {

    @Autowired
    OrderQueue orderQueue;

    @Test
    public void pipeTest() {
        try (Jedis jedis = RedisUtil.getJedis();
             final Pipeline pipelined = jedis.pipelined()) {
            final Set<String> keys = jedis.keys("*" + A_TRADING_QUEUE);
            for (String key : keys) {
                pipelined.lrange(key, 0, -1);
                pipelined.del(key);
                final List<Object> objects = pipelined.syncAndReturnAll();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}