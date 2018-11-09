package com.mouzhiapp.stock_market.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mouzhiapp.stock_market.bean.CategoryStock;
import com.mouzhiapp.stock_market.bean.StockIndex;
import com.mouzhiapp.stock_market.consts.Consts;
import com.mouzhiapp.stock_market.controller.VO.CategoryStocksAndFundFlowVO;
import com.mouzhiapp.stock_market.controller.VO.CategoryWithTopStockVO;
import com.mouzhiapp.stock_market.controller.VO.StockIndexVO;
import com.mouzhiapp.stock_market.controller.VO.StockIndexesAndRafVO;
import com.mouzhiapp.stock_market.repo.mapper.standBy.StockCategoryStandByMapper;
import com.mouzhiapp.stock_market.repo.mapper.standBy.StockMarketOriginStandByMapper;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * create by zhouzhenyang on 2018/6/4
 */
@Service
public class StockCategoryService {

    @Autowired
    StockCategoryStandByMapper categoryMapper;
    @Autowired
    StockMarketOriginStandByMapper originStandByMapper;
    @Autowired
    StockUtilService utilService;

    private boolean isTesting = false;

    public StockCategoryService setTesting(boolean testing) {
        isTesting = testing;
        return this;
    }

    @Autowired
    private StockCategorySubService categorySubService;

    @Autowired
    private ObjectMapper jacksonMapper;

    private Logger logger = LoggerFactory.getLogger(Consts.ERROR_LOGGER);

    public StockIndexesAndRafVO getIndexesAndRaf() throws Exception {
        final String riseAndFall = categorySubService.getRiseAndFall();
        final List<StockIndex> indexes = categorySubService.getIndexes();
        final List<StockIndexVO> stockIndexVOS = indexes.stream().map(stockIndex -> {
            StockIndexVO stockIndexVO = new StockIndexVO();
            BeanUtils.copyProperties(stockIndex, stockIndexVO);
            return stockIndexVO;
        }).collect(Collectors.toList());
        return new StockIndexesAndRafVO(riseAndFall, stockIndexVOS);
    }

    public List<CategoryWithTopStockVO> getIndustries() throws Exception {
        final List<CategoryWithTopStockVO> industries =
                categoryMapper.doGetIndustriesWithTopStock()
                        .stream().map(this::toCategoryWithTopStock)
                        .filter(Objects::nonNull)
                        .sorted((o1, o2) -> {
                            if (Double.valueOf(o1.getChangeP()) >
                                    Double.valueOf(o2.getChangeP())) {
                                return -1;
                            } else if (Double.valueOf(o1.getChangeP()).equals(Double.valueOf(o2.getChangeP()))) {
                                return 0;
                            }
                            return 1;
                        })
                        .collect(Collectors.toList());
        return industries;
    }

    public List<CategoryWithTopStockVO> getThemes() throws Exception {
        final List<CategoryWithTopStockVO> themes =
                categoryMapper.doGetThemesWithTopStocks()
                        .stream().map(this::toCategoryWithTopStock)
                        .filter(Objects::nonNull)
                        .sorted((o1, o2) -> {
                            if (Double.valueOf(o1.getChangeP()) >
                                    Double.valueOf(o2.getChangeP())) {
                                return -1;
                            } else if (Double.valueOf(o1.getChangeP()).equals(Double.valueOf(o2.getChangeP()))) {
                                return 0;
                            }
                            return 1;
                        })
                        .collect(Collectors.toList());
        return themes;
    }

    private CategoryWithTopStockVO toCategoryWithTopStock(Map<String, Object> m) {
        try {
            CategoryWithTopStockVO theme =
                    new CategoryWithTopStockVO(String.valueOf(m.get("guid")), String.valueOf(m.get("name")),
                            String.valueOf(m.get("changep")));
            CategoryStock stock =
                    jacksonMapper.readValue(String.valueOf(m.get("stocks")), CategoryStock.class);
            theme.setStock(stock);
            return theme;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CategoryStocksAndFundFlowVO getIndustryStocks(String guid) throws Exception {
        final List<Map<String, Object>> maps = categoryMapper.doGetIndustryStocks(guid);
        final CategoryStocksAndFundFlowVO collect =
                maps.stream().map(this::toCategoryStocksAndFundFlow)
                        .findFirst().orElse(null);
        return collect;
    }

    public CategoryStocksAndFundFlowVO getThemeStocks(String guid) throws Exception {
        final List<Map<String, Object>> maps =
                categoryMapper.doGetThemeStocks(guid);
        CategoryStocksAndFundFlowVO collect =
                maps.stream().map(this::toCategoryStocksAndFundFlow)
                        .findFirst().orElse(null);
        return collect;
    }

    private CategoryStocksAndFundFlowVO toCategoryStocksAndFundFlow(Map<String, Object> m) {
        CategoryStocksAndFundFlowVO categoryStocksAndFundFlow =
                new CategoryStocksAndFundFlowVO(
                        String.valueOf(m.getOrDefault("smallbuy", 0)),
                        String.valueOf(m.getOrDefault("smallsale", 0)),
                        String.valueOf(m.getOrDefault("mediumbuy", 0)),
                        String.valueOf(m.getOrDefault("mediumsale", 0)),
                        String.valueOf(m.getOrDefault("largebuy", 0)),
                        String.valueOf(m.getOrDefault("largesale", 0))
                );
        categoryStocksAndFundFlow
                .setStocks(this.getCategoryStocks(((PGobject) m.get("stocks")).getValue()));
        return categoryStocksAndFundFlow;
    }

    private List<CategoryStock> getCategoryStocks(String stocksStr) {
        try {
            return jacksonMapper.readValue(stocksStr,
                    new TypeReference<List<CategoryStock>>() {
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(0);
        }
    }

}
