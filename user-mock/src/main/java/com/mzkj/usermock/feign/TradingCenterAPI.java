package com.mzkj.usermock.feign;

import com.alibaba.fastjson.JSONObject;
import com.mzkj.usermock.bean_rmi.StockOrderRMI;
import com.mzkj.usermock.dto.RespResult;
import org.apache.ibatis.annotations.Select;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "mock-trading-system")
public interface TradingCenterAPI {

    @RequestMapping(value = "/order",method = RequestMethod.POST )
    RespResult buyStock(@RequestBody StockOrderRMI order);

}
