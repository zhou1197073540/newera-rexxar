package com.mouzhiapp.stock_market.controller;

import com.google.common.collect.ImmutableMap;
import com.mouzhiapp.stock_market.annotation.SelfSelected;
import com.mouzhiapp.stock_market.bean.UserSelectedStock;
import com.mouzhiapp.stock_market.consts.Consts;
import com.mouzhiapp.stock_market.controller.VO.StockSnapShotForMockVO;
import com.mouzhiapp.stock_market.controller.VO.StockSnapShotWithDayKline;
import com.mouzhiapp.stock_market.dto.RespDto;
import com.mouzhiapp.stock_market.service.StockCategoryService;
import com.mouzhiapp.stock_market.service.StockMarketService;
import com.mouzhiapp.stock_market.service.StockUtilService;
import com.mouzhiapp.stock_market.service.UserUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.mouzhiapp.stock_market.consts.Message.TOKEN_EXPIRED;

/**
 * create by zhouzhenyang on 2018/5/22
 */
@Controller
@RequestMapping("/stock/")
public class StockMarketController {

    @Autowired
    StockMarketService stockMarketService;
    @Autowired
    StockCategoryService categoryService;
    @Autowired
    StockUtilService utilService;
    @Autowired
    UserUtilService userUtilService;

    //    @ApiOperation(value = "获取股票基本指标", notes = "")
    @RequestMapping(value = "/basic-indexes/{code}", method = RequestMethod.GET)
    @ResponseBody
    @SelfSelected
    public RespDto<UserSelectedStock> getStockBasicIndexes(@PathVariable String code) throws Exception {
        return new RespDto<>(true, "成功", stockMarketService.getLatestStockBasicIndexes(code));
    }

    //    @ApiOperation(value = "根据code获取股票分时图", notes = "")
    @RequestMapping(value = "/timeline/{code}", method = RequestMethod.GET)
    @ResponseBody
    public RespDto<Map<String, Object>> getStockTimeline(@PathVariable String code) throws Exception {
        if (utilService.isTradingDate(LocalDate.now().toString())) {
            if (utilService.isTradingTime()) {
                return new RespDto<>(true, "成功", stockMarketService.getTimeline(code));
            } else {
                return new RespDto<>(true, "成功", stockMarketService.getLastTradingDayTimeline(code));
            }
        } else {
            return new RespDto<>(true, "成功", stockMarketService.getLastTradingDayTimeline(code));
        }
    }

    //    @ApiOperation(value = "根据code,时间段获取日k线", notes = "")
    @RequestMapping(value = "/kline/{unit:1day|1week|1year}/{code}/{start}/{end}",
            method = RequestMethod.GET)
    @ResponseBody
    public RespDto<Map<String, Object>> getStockDayKLine(@PathVariable String unit, @PathVariable String code,
                                                         @PathVariable String start, @PathVariable String end) throws Exception {
        if ("1week".equals(unit)) {
            return new RespDto<>(true, "成功",
                    ImmutableMap.of("start", start, "end", end, "kline", stockMarketService.getWeekKLine(code, start, end)));
        }
        return new RespDto<>(true, "成功",
                ImmutableMap.of("start", start, "end", end, "kline", stockMarketService.getDayKLine(code, start, end)));
    }

    //    @ApiOperation(value="根据code,时间段获取最新日k线", notes="")
    @RequestMapping(value = "/kline/{unit:1day|1week|1year}/{code}",
            method = RequestMethod.GET)
    @ResponseBody
    public RespDto<Map<String, Object>> getStockDayKLineDefault(@PathVariable String unit,
                                                                @PathVariable String code) throws Exception {
        String end = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String start = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return getStockDayKLine(unit, code, start, end);
    }

    @RequestMapping(value = "/snapshot-for-mock/{code}", method = RequestMethod.GET)
    @ResponseBody
    public RespDto<StockSnapShotForMockVO> getStockSnapShot(@PathVariable("code") String code) throws Exception {
        return new RespDto<>(true, "成功", stockMarketService.getStockSnapShotForMock(code));
    }


    /**
     * 获取K线训练营股票数据(指定股票)
     *
     * @param fullCode
     * @param token
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/kline-training/{code}", method = RequestMethod.GET)
    @ResponseBody
    public RespDto<StockSnapShotWithDayKline> getStockForKlineTraining(
            @PathVariable("code") String fullCode,
            @RequestParam("token") String token) throws Exception {
        String uid = userUtilService.getUidByToken(token);
        if (StringUtils.isEmpty(uid)) {
            return new RespDto<>(false, TOKEN_EXPIRED.statusCode, TOKEN_EXPIRED.message);
        }
        return new RespDto<>(true, "成功", stockMarketService.getStockForKlineTraining(fullCode));
    }


    /**
     * 获取K线训练营股票数据(随机股票)
     *
     * @param token
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/kline-training/", method = RequestMethod.GET)
    @ResponseBody
    public RespDto<StockSnapShotWithDayKline> getStockForKlineTrainingRandomCode(
            @RequestParam("token") String token
    ) throws Exception {
        String uid = userUtilService.getUidByToken(token);
        if (StringUtils.isEmpty(uid)) {
            return new RespDto<>(false, TOKEN_EXPIRED.statusCode, TOKEN_EXPIRED.message);
        }
        return new RespDto<>(true, "成功", stockMarketService.getStockForKlineTraining(null));
    }
}
