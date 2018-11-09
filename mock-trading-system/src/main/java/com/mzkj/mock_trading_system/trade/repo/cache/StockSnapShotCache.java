package com.mzkj.mock_trading_system.trade.repo.cache;

/**
 * create by zhouzhenyang on 2018/7/29
 */

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzkj.mock_trading_system.common.util.RedisUtil;
import com.mzkj.mock_trading_system.trade.valobj.StockMarketSnapshotPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mzkj.mock_trading_system.common.consts.Consts.STOCK_LATEST;

@Repository
public class StockSnapShotCache {

    @Autowired
    ObjectMapper jacksonMapper;

    public StockMarketSnapshotPO getByCode(String fullCode) throws Exception {
        try (Jedis jedis = RedisUtil.getJedis()) {
            String str = jedis.hget(STOCK_LATEST, fullCode);
            final StockMarketSnapshotPO stockMarketSnapshotPO =
                    jacksonMapper.readValue(str, StockMarketSnapshotPO.class);
            return stockMarketSnapshotPO;
        }
    }

    public List<StockMarketSnapshotPO> getAll() {
        try (Jedis jedis = RedisUtil.getJedis()) {
            final Set<String> hkeys = jedis.hkeys(STOCK_LATEST);
            final String[] strings = hkeys.toArray(new String[0]);
            final List<String> snapShotStrs = jedis.hmget(STOCK_LATEST, strings);
            final List<StockMarketSnapshotPO> snapshotPOS = snapShotStrs.stream()
                    .map(this::toSnapShotPO)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return snapshotPOS;
        }
    }

    public Map<String, StockMarketSnapshotPO> getAllAsMap() {
        final List<StockMarketSnapshotPO> snapshotPOS = this.getAll();
        if (snapshotPOS == null || snapshotPOS.isEmpty()) {
            return null;
        }
        return snapshotPOS.stream()
                .filter(snapshot -> snapshot.getFullCode() != null)
                .collect(Collectors.toMap(StockMarketSnapshotPO::getFullCode, s -> s));
    }

    public List<StockMarketSnapshotPO> getIndexes() throws Exception {
        try (Jedis jedis = RedisUtil.getJedis()) {
            final String[] indexKeys = new String[]{"sh000001", "sz399006", "sz399001"};
            final List<String> snapShotStrs = jedis.hmget(STOCK_LATEST, indexKeys);
            final List<StockMarketSnapshotPO> snapshotPOS = snapShotStrs.stream()
                    .map(this::toSnapShotPO)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return snapshotPOS;
        }
    }

    private StockMarketSnapshotPO toSnapShotPO(String snapShotStr) {
        try {
            jacksonMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true) ;
            final StockMarketSnapshotPO stockMarketSnapshotPO = jacksonMapper.readValue(snapShotStr, StockMarketSnapshotPO.class);
            return stockMarketSnapshotPO;
        } catch (Exception e) {
            return null;
        }
    }


}
