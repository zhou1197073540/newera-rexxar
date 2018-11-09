package com.mouzhiapp.stock_market.controller;

import com.mouzhiapp.stock_market.controller.VO.CategoryStocksAndFundFlowVO;
import com.mouzhiapp.stock_market.controller.VO.CategoryWithTopStockVO;
import com.mouzhiapp.stock_market.controller.VO.StockIndexesAndRafVO;
import com.mouzhiapp.stock_market.dto.RespDto;
import com.mouzhiapp.stock_market.service.StockCategoryService;
import com.mouzhiapp.stock_market.service.StockMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * create by zhouzhenyang on 2018/5/22
 */
@Controller
@RequestMapping("/stock/")
public class StockCategoryController {

    @Autowired
    StockMarketService stockMarketService;
    @Autowired
    StockCategoryService categoryService;

//    @ApiOperation(value="获取行业列表", notes="")
    @RequestMapping(value = "/industries",method = RequestMethod.GET)
    @ResponseBody
    public RespDto<List<CategoryWithTopStockVO>> getIndustries() throws Exception {
        return new RespDto<>(true, "成功", categoryService.getIndustries());
    }

//    @ApiOperation(value="获取主题列表", notes="")
    @RequestMapping(value = "/themes",method = RequestMethod.GET)
    @ResponseBody
    public RespDto<List<CategoryWithTopStockVO>> getThemes() throws Exception {
        return new RespDto<>(true, "成功",
                categoryService.getThemes());
    }

//    @ApiOperation(value="获取行业对应资金流和股票", notes="")
    @GetMapping("/industries/{guid}/fundflow-and-stocks")
    @ResponseBody
    public RespDto<CategoryStocksAndFundFlowVO> getIndustryStocks(
            @PathVariable("guid") String guid) throws Exception {
        return new RespDto<>(true, "成功", categoryService.getIndustryStocks(guid));
    }

//    @ApiOperation(value="获取主题对应资金流和股票", notes="")
    @GetMapping(value = "/themes/{guid}/fundflow-and-stocks")
    @ResponseBody
    public RespDto<CategoryStocksAndFundFlowVO> getThemeStocks(
            @PathVariable("guid") String guid) throws Exception {
        return new RespDto<>(true, "成功",
                categoryService.getThemeStocks(guid));
    }

//    @ApiOperation(value="获取实时指数和涨跌", notes="")
    @GetMapping(value = "/indexes-and-raf")
    @ResponseBody
    public RespDto<StockIndexesAndRafVO> getStockIndexesAndRaf() throws Exception {
        return new RespDto<>(true, "成功", categoryService.getIndexesAndRaf());
    }
}
