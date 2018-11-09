package com.mouzhiapp.stock_market.repo.cache;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.mouzhiapp.stock_market.repo.PO.StockMarketSnapshotPO;
import com.mouzhiapp.stock_market.consts.Consts;
import com.mouzhiapp.stock_market.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * create by zhouzhenyang on 2018/7/14
 */
@Repository
public class StockMarketSnapShotCache {

    @Autowired
    ObjectMapper jacksonMapper;
    private static Set<String> indexes = Sets.newHashSet(
            "sh000001", "sz399001", "sz399005", "sz399006", "sh000016", "sh000906", "sh000905", "sh000300"
    );

    public StockMarketSnapshotPO getByCode(String fullCode) throws Exception {
        if (fullCode == null) return null;
        jacksonMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        try (Jedis jedis = RedisUtil.getJedis()) {
            String str = jedis.hget(Consts.STOCK_LATEST, fullCode);
            if (str == null) return null;
            final StockMarketSnapshotPO stockMarketSnapshotPO =
                    jacksonMapper.readValue(str, StockMarketSnapshotPO.class);
            return stockMarketSnapshotPO;
        }
    }

    public List<StockMarketSnapshotPO> getAll() throws Exception {
        try (Jedis jedis = RedisUtil.getJedis()) {
            final Set<String> hkeys = jedis.hkeys(Consts.STOCK_LATEST);
            final String[] strings = hkeys.toArray(new String[0]);
            final List<String> snapShotStrs = jedis.hmget(Consts.STOCK_LATEST, strings);
            final List<StockMarketSnapshotPO> snapshotPOS = snapShotStrs.stream()
                    .map(this::toSnapShotPO)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return snapshotPOS;
        }
    }

    public List<StockMarketSnapshotPO> getIndexes() throws Exception {
        try (Jedis jedis = RedisUtil.getJedis()) {
            final String[] indexKeys = new String[]{"sh000001", "sz399006", "sz399001"};
            final List<String> snapShotStrs = jedis.hmget(Consts.STOCK_LATEST, indexKeys);
            final List<StockMarketSnapshotPO> snapshotPOS = snapShotStrs.stream()
                    .map(this::toSnapShotPO)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return snapshotPOS;
        }
    }

    private StockMarketSnapshotPO toSnapShotPO(String snapShotStr) {
        try {
            final StockMarketSnapshotPO stockMarketSnapshotPO = jacksonMapper.readValue(snapShotStr, StockMarketSnapshotPO.class);
            return stockMarketSnapshotPO;
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getKeys() {
        try (Jedis jedis = RedisUtil.getJedis()) {
            final Set<String> fullCodes = jedis.hkeys(Consts.STOCK_LATEST);
            final List<String> collect = fullCodes
                    .stream()
                    .filter(str -> !isIndex(str))
                    .collect(Collectors.toList());
            return collect;
        }
    }

    private boolean isIndex(String fullCode) {
        return indexes.contains(fullCode);
    }
}
