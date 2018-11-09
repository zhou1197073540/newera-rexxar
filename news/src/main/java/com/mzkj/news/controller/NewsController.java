package com.mzkj.news.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.mzkj.news.bean.News;
import com.mzkj.news.bean.Stock;
import com.mzkj.news.bean.StockRanking;
import com.mzkj.news.bean.StockRecommend;
import com.mzkj.news.bean_vo.PointsPraise;
import com.mzkj.news.bean_vo.StockUpDownVO;
import com.mzkj.news.dto.RespResult;
import com.mzkj.news.service.NewsService;
import com.mzkj.news.utils.RedisUtil;
import com.mzkj.news.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class NewsController {
    @Autowired
    NewsService newsService;

    /**
     * 新闻
     */
    @ResponseBody
    @GetMapping(value="/news/{page_size}/{page_num}")
    public RespResult getNews(@PathVariable int page_size,@PathVariable int page_num){
        List<News> data= newsService.selectNewsByPublishTime(page_size,page_num);
        return RespResult.genSuccessResult(data);
    }
    /**
     * 新闻
     */
    @ResponseBody
    @GetMapping(value="/news/{type}/{page_size}/{page_num}")
    public RespResult getNews(@RequestParam("token")String token,@PathVariable("page_size") int page_size,
                              @PathVariable("page_num") int page_num,@PathVariable("type") String type){
        List<News> data=newsService.selectNewsByPublishTime(page_size,page_num,type);;
        String uid=newsService.getUidByToken(token);
        if(StringUtil.isEmpty(uid)) uid="";
        for(News one:data){
            //新闻的点赞数和用户是否点赞
            PointsPraise point=newsService.selectPointsAndPraise(uid,one.getGuid());
            one.setPoints(point.getPoints());
            one.setPraise(point.getPraise());
        }
        return RespResult.genSuccessResult(data);
    }
    /**
     * 新闻内容
     */
    @ResponseBody
    @GetMapping(value="/news/news_content/{guid}")
    public RespResult getNewsContent(@PathVariable("guid") String guid){
        String content= newsService.selectNewsContentByGuid(guid);
        return RespResult.genSuccessResult(content);
    }

    /**
     * 7*24小时新闻
     */
    @ResponseBody
    @GetMapping("/stock/kuaixun")
    public RespResult getStockMsg(@RequestParam("page") String page){
        int num=Integer.parseInt(page);
        if(num<=0) num=0;
        if(num>=100000) num=100000;
        num=num*20;
        List objs=null;
        try(Jedis jedis= RedisUtil.getJedis()){
            List<String> list = jedis.lrange("kuaixun_web_stock", num, num+19);
            objs=list.stream().map(x-> JSON.parseObject(x))
                    .sorted((x,y)->y.getString("time").compareTo(x.getString("time"))).collect(Collectors.toList());
        }
        return RespResult.genSuccessResult(objs);
    }
    /**
     * 选股
     */
    @ResponseBody
    @GetMapping("/stock_selection/{condition}/{page_size}/{page_num}")
    public RespResult selectStock(@PathVariable("condition") String condition,@PathVariable("page_size") int page_size,
                                  @PathVariable("page_num") int page_num){
        Map<String,Object> map=new HashMap<>();
        List<Stock> stocks=null;
        if(condition.equals("default")){
            Integer num=newsService.selectStockNum();
            stocks=newsService.selectStockByPage(page_size,page_num);
            map.put("num",num);
        }else{
            Set<String> cons= StringUtil.formatCondition(condition);
            Set<String> codes=newsService.getCommonCodes(cons);
            if(codes!=null&&codes.size()>0){
                Integer count=newsService.selectStockNumByCondition(codes);
                stocks=newsService.selectStockByCode(codes,page_size,page_num);
                map.put("num",count);
            }
        }
        newsService.getScores(stocks);
        map.put("stock",stocks);
        return RespResult.genSuccessResult(map);
    }
    /**
     * 选股条件
     */
    @ResponseBody
    @GetMapping("/stock_condition")
    public RespResult selectConditionNum(){
        String[] cons={"cashin","lowpe","lowpb","lowprice","lowturnover","highbeta","newhigh","smallequ","bigequ","addholding","upvol","rsilow","macdcross"};
        String[] names={"资金流入","低市盈率","低市净率","低价股","低换手率","波动大","股价创新高","小盘股","大盘股","股东增持","放量上涨","价格超跌","金叉"};
        List<Stock> stocks=new ArrayList<>();
        for(int i=0;i<cons.length;i++){
            Stock stock=new Stock();
            stock.setCode(cons[i]);
            stock.setName(names[i]);
            stocks.add(stock);
        }
        return RespResult.genSuccessResult(stocks);
    }

    /**
     * 行情分析中评级雷达图
     */
    @ResponseBody
    @GetMapping("/market_analysis_rating/{ticker}")
    public RespResult analysisRating(@PathVariable("ticker") String ticker){
        List<StockRanking> tickers=newsService.analysisRatingByCode(ticker);
        return RespResult.genSuccessResult(tickers);
    }
    /**
     * AI持股
     */
    @ResponseBody
    @GetMapping("/stock_recommend")
    public RespResult stockRecommend(){
        List<StockUpDownVO> tickers=newsService.stockRecommendsFromRediss();
        return RespResult.genSuccessResult(tickers);
    }

    /**
     * 股票互动问答，股票相关新闻
     */
    @ResponseBody
    @GetMapping("/union_news/{code}")
    public RespResult queryUnionNewsByCode(@PathVariable("code") String code ){
        List<News> tickers=newsService.queryUnionNewsByCode(code);
        if(tickers.isEmpty()) return RespResult.genErrorResult("无相关股票");
        return RespResult.genSuccessResult(tickers);
    }
}
