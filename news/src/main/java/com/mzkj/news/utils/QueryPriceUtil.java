package com.mzkj.news.utils;

import com.alibaba.fastjson.JSONObject;
import com.mzkj.news.bean.StockLatest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查找股票最新价
 */
public class QueryPriceUtil {

    public static StockLatest getStockLatestPrice(String code){
        try(Jedis jedis=RedisUtil.getJedis()){
            String str=jedis.hget("stock_latest",code);
            return JSONUtil.JSON2Object(str,StockLatest.class);
        }
    }

    public static Map<String,String> queryStockLatestPriceByCode(List<String> codes) {
        if(codes==null) return null;
        try(Jedis jedis=RedisUtil.getJedis()){
            Map<String,String> map=new HashMap<String,String>();
            Pipeline pipeline=jedis.pipelined();
            for(String code_type:codes){
                if(!map.containsKey(code_type)){
                    map.put(code_type,null);
                }
                pipeline.hget("stock_latest",code_type);
            }
            List<Object> list=pipeline.syncAndReturnAll();
            for(Object obj:list){
                if(null==obj) continue;
                JSONObject json = JSONObject.parseObject(obj.toString());
                String now=json.getString("now");
                String full_code = json.getString("full_code");
                if(!StringUtil.isEmpty(full_code))map.put(full_code,now);
            }
            return map;
        }
    }
}
