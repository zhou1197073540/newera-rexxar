package com.mzkj.mock_trading_system.trade.repo.cache;

import com.mzkj.mock_trading_system.common.util.RedisUtil;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

/**
 * create by zhouzhenyang on 2018/7/30
 */
@Repository
public class OidLock {

    Logger logger = LoggerFactory.getLogger(OidLock.class);

    public boolean tryLock(UserOrder userOrder) {
        String uLock = userOrder.getOid() + ".a.trading.lock";

        //上锁
        try (Jedis r_conn = RedisUtil.getJedis()) {
            if (r_conn != null) {
                if (r_conn.setnx(uLock, userOrder.getOperation()) == 1) {
                    r_conn.expire(uLock, 60);
                    return true;
                } else {
                    if (r_conn.ttl(uLock) == -1) {
                        r_conn.expire(uLock, 60);
                    }
                    return false;
                }
            }
            logger.error("Redis connection is null");
            return false;
        }
    }

    public void unlock(UserOrder userOrder) {
        String uLock = userOrder.getOid() + ".a.trading.lock";
        try (Jedis r_conn = RedisUtil.getJedis()) {
            if (r_conn != null) {
                r_conn.del(uLock);
            } else {
                logger.error("Redis connection is null");
            }
        }
    }
}
