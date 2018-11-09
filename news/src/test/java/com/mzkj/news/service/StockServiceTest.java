package com.mzkj.news.service;

import com.alibaba.fastjson.JSONObject;
import com.mzkj.news.bean_vo.KPracticeVO;
import com.mzkj.news.mapper.StockerMapper;
import com.mzkj.news.utils.MathUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("aliyun")
public class StockServiceTest {
    @Test
    public void stockRanking() throws Exception {
        String ss="{\"token\":\"xxx\",\"data\":[{\"code\":\"600000\",\"rank\":\"1\"},{\"code\":\"600001\",\"rank\":\"2\"}]}";
        stockService.stockRanking(JSONObject.parseObject(ss));
    }

    @Autowired
    StockService stockService;
    @Autowired
    StockerMapper stockerMapper;

    @Test
    public void sss() throws Exception {
        KPracticeVO vo=new KPracticeVO();
        vo.setProfit("33");
        Map<String,String> map=stockerMapper.selectKlineBeatRate(vo);
        String big_num= String.valueOf(map.get("big_num"));
        String total= String.valueOf(map.get("total"));
        System.out.println(MathUtil.divFloor(big_num,total));
    }


    @Test
    public void grailTime() throws Exception {
        stockService.grailTime("000905");
    }

    @Test
    public void selectStockRecommendLastOne() throws Exception {
        stockService.selectStockRecommendLastOne();
    }

}