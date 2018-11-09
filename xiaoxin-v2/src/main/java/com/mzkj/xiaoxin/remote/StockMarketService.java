package com.mzkj.xiaoxin.remote;

import com.mzkj.xiaoxin.dto.RespDto;
import com.mzkj.xiaoxin.remote.PO.StockBasicIndexesVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * create by zhouzhenyang on 2018/9/18
 */
@FeignClient(value = "stock-market")
public interface StockMarketService {

    @RequestMapping(value = "/stock/timeline/{code}", method = RequestMethod.GET)
    RespDto<Map<String, Object>> getStockTimeline(
            @PathVariable("code") String code) throws Exception;

    @RequestMapping(value = "/stock/basic-indexes/{code}", method = RequestMethod.GET)
    RespDto<StockBasicIndexesVO> getStockBasicIndexes(
            @PathVariable("code") String code) throws Exception;

}
