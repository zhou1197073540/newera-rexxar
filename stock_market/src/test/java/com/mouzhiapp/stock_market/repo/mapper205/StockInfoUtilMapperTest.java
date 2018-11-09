package com.mouzhiapp.stock_market.repo.mapper205;

import com.mouzhiapp.stock_market.bean.StockCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


/**
 * create by zhouzhenyang on 2018/6/4
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class StockInfoUtilMapperTest {

    @Autowired
    StockInfoUtilMapper infoUtilMapper;

    @Test
    public void doGetIndustryCategoryStocks() throws Exception {
        final List<StockCategory> industries = infoUtilMapper.doGetIndustryCategoryStocks();
        for (StockCategory industry : industries) {
            System.out.println(industry.getCategory());
            String[] tickers = industry.getTickers().replaceAll("\\{|\\}", "").split(",");
            for (String ticker : tickers) {

            }
            System.out.println(industry.getTickers().replaceAll("\\{|\\}", ""));
        }
    }
}