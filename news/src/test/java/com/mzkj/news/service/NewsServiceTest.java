package com.mzkj.news.service;

import com.mzkj.news.bean.News;
import com.mzkj.news.bean.Stock;
import com.mzkj.news.dto.RespResult;
import com.mzkj.news.utils.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class NewsServiceTest {
    @Autowired
    NewsService newsService;

    @Test
    public void testNews(){
        List<News> news=newsService.selectNewsByPublishTime(10,1);
        news.stream().forEach(x-> System.out.println(x.getTitle()));
    }
    @Test
    public void testNewsContent(){
        String guid="e5caae3dc583b10e344a7e6c813d88e1";
        String con=newsService.selectNewsContentByGuid(guid);
        System.out.println(con);
    }
    @Test
    public void testRedis(){
        Set<String> cons= StringUtil.formatCondition("cashin,lowpe,lowpb");
        Set<String> codes=newsService.getCommonCodes(cons);
        if(codes!=null&&codes.size()>0){
            List<Stock> stocks=newsService.selectStockByCode(codes,20,0);
            stocks.stream().forEach(x->System.out.print(x.getCode()+"=="+x.getName()));
        }
    }
}