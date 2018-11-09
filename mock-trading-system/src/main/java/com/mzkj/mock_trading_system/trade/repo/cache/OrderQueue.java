package com.mzkj.mock_trading_system.trade.repo.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzkj.mock_trading_system.common.util.RedisUtil;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mzkj.mock_trading_system.common.consts.Consts.A_TRADING_QUEUE;

/**
 * create by zhouzhenyang on 2018/7/31
 */
@Repository
public class OrderQueue {

    @Autowired
    private ObjectMapper jacksonMapper;

    private Logger logger = LoggerFactory.getLogger(OrderQueue.class);

    public void add(UserOrder userOrder) {
        String queueName = userOrder.getFullCode() + A_TRADING_QUEUE;
        try (Jedis jedis = RedisUtil.getJedis()) {
            String order = null;
            try {
                order = jacksonMapper.writeValueAsString(userOrder);
            } catch (Exception e) {
                logger.error(String.format("order序列化错误:%s", userOrder.toString()));
            }
            jedis.lpush(queueName, order);
        }
    }

    public List<UserOrder> getAll() {
        try (Jedis jedis = RedisUtil.getJedis();
             final Pipeline pipelined = jedis.pipelined()) {
            final Set<String> keys = jedis.keys("*" + A_TRADING_QUEUE);
            for (String key : keys) {
                pipelined.lrange(key, 0, -1);
                pipelined.del(key);
            }
            final List<Object> results = pipelined.syncAndReturnAll();
            if (results == null) {
                return null;
            }
            final List<UserOrder> collect = results.stream()
                    .map(this::toUserOrder)
                    .filter(Objects::nonNull)
                    .flatMap(Collection::stream)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return collect;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<UserOrder> toUserOrder(Object o) {
        try {
            List<String> ordersStr = (List<String>) o;
            return ordersStr.stream().map(str -> {
                try {
                    return jacksonMapper.readValue(str, UserOrder.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
    }
}
