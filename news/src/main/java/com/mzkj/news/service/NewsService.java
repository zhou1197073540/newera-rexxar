package com.mzkj.news.service;

import com.alibaba.fastjson.JSONObject;
import com.mzkj.news.bean.News;
import com.mzkj.news.bean.Stock;
import com.mzkj.news.bean.StockRanking;
import com.mzkj.news.bean.StockRecommend;
import com.mzkj.news.bean_vo.PointsPraise;
import com.mzkj.news.bean_vo.StockUpDownVO;
import com.mzkj.news.dto.RespResult;
import com.mzkj.news.mapper.NewsMapper;
import com.mzkj.news.utils.RedisUtil;
import com.mzkj.news.utils.StringUtil;
import com.mzkj.news.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class NewsService {
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    TokenUtil tokenUtil;

    public List<News> selectNewsByPublishTime(int page_size,int page_num) {//20,2
        if(page_size<=0)page_size=0;
        if(page_num<=0)page_num=0;
        if(page_size>1000)page_size=1000;
        if(page_num>100)page_num=100;
        int page_index=page_size*page_num;
        return newsMapper.selectNewsByPublishTime(page_size,page_index);//20,40
    }
    public List<News> selectNewsByPublishTime(int page_size, int page_num, String type) {
        if(page_size<=0)page_size=0;
        if(page_num<=0)page_num=0;
        if(page_size>1000)page_size=1000;
        if(page_num>100)page_num=100;
        int page_index=page_size*page_num;
        return newsMapper.selectNewsByTypePublishTime(page_size,page_index,type);//20,40
    }
    public String selectNewsContentByGuid(String guid) {
       return newsMapper.selectNewsContentByGuid(guid);
    }

    public Set<String> getCommonCodes(Set<String> cons) {
        if(cons.size()<=0) return null;
        try(Jedis jedis= RedisUtil.getJedis()){
            List<String> list=new ArrayList<>(cons);
            Set<String> first=StringUtil.formatCodes(jedis.get(list.get(0)));
            for(String stock:cons){
                Set<String> cs= StringUtil.formatCodes(jedis.get(stock));
                first.retainAll(cs);
            }
            return first;
        }
    }

    public Integer selectStockNumByCondition(Set<String> codes) {
        String str="'"+codes.stream().reduce((x,y)->x+"','"+y).get()+"'";
        return newsMapper.selectStockNumByCondition(str);
    }

    public List<Stock> selectStockByCode(Set<String> codes,int page_size, int page_num) {
        String str="'"+codes.stream().reduce((x,y)->x+"','"+y).get()+"'";
        if(page_size<=0)page_size=0;
        if(page_num<=0)page_num=0;
        if(page_size>1000)page_size=1000;
        if(page_num>100)page_num=100;
        int page_index=page_size*page_num;
        return newsMapper.selectStockByCode(str,page_size,page_index);
    }


    public List<Stock> selectStockDefault() {
        List<Stock> stos=newsMapper.selectStockDefault();
        for(Stock sto:stos){
            String code_type="unknown";
            if(sto.getCode().startsWith("0")||sto.getCode().startsWith("3")){
                code_type="sz"+sto.getCode();
            }else if(sto.getCode().startsWith("6")){
                code_type="sh"+sto.getCode();
            }
            sto.setCode_type(code_type);
        }
        return stos;
    }

    public Integer selectStockNum() {
        return newsMapper.selectStockNum();
    }

    public List<Stock> selectStockByPage(int page_size, int page_num) {
        if(page_size<=0)page_size=0;
        if(page_num<=0)page_num=0;
        if(page_size>1000)page_size=1000;
        if(page_num>100)page_num=100;
        int page_index=page_size*page_num;
        return newsMapper.selectStockByPage(page_size,page_index);//20,40
    }


    public List<StockRanking> analysisRatingByCode(String ticker) {
        List<StockRanking> stocks= newsMapper.analysisRatingByCode(ticker);
        for(StockRanking stock:stocks){
            stock.setScore(calScore(stock));
        }
        return stocks;
    }

    public List<StockRecommend> stockRecommend() {
        List<StockRecommend> stos= newsMapper.stockRecommend();
        for(StockRecommend sto:stos){
            String code_type="unknown";
            if(sto.getCode().startsWith("0")||sto.getCode().startsWith("3")){
                code_type="sz"+sto.getCode();
            }else if(sto.getCode().startsWith("6")){
                code_type="sh"+sto.getCode();
            }
            sto.setCode_type(code_type);
        }
        return stos;
    }

    public List<StockRecommend> stockRecommends() {
        List<StockRecommend> stos= newsMapper.stockRecommends();
        for(StockRecommend sto:stos){
            String code_type="unknown";
            if(sto.getCode().startsWith("0")||sto.getCode().startsWith("3")){
                code_type="sz"+sto.getCode();
            }else if(sto.getCode().startsWith("6")){
                code_type="sh"+sto.getCode();
            }
            sto.setCode_type(code_type);
        }
        return stos;
    }

    public List<StockUpDownVO> stockRecommendsFromRedis() {
        List<StockUpDownVO> vos=new ArrayList<>();
        List<String> stos= newsMapper.selectStockRecommend();
        if(stos.size()<=0) return vos;
        try(Jedis jedis=RedisUtil.getJedis()){
            Pipeline pipeline=jedis.pipelined();
            for(String code:stos){
                pipeline.hget("stock_latest",StringUtil.formatStockCode(code,""));
            }
            List<Object> list=pipeline.syncAndReturnAll();
            for(Object obj:list){
                StockUpDownVO vo=calcuteJsonRate(obj);
                if(vo!=null) vos.add(vo);
            }
        }
        return vos;
    }
    public List<StockUpDownVO> stockRecommendsFromRediss() {
        List<StockUpDownVO> vos=new ArrayList<>();
        List<StockUpDownVO> stos= newsMapper.selectStockRecommends();
        if(stos.size()<=0) return vos;
        Map<String,StockUpDownVO> map=new HashMap<>();
        try(Jedis jedis=RedisUtil.getJedis()){
            Pipeline pipeline=jedis.pipelined();
            for(StockUpDownVO codevo:stos){
                if(!map.containsKey(codevo.getCode())){
                    map.put(codevo.getCode_type(),codevo);
                }
                pipeline.hget("stock_latest",codevo.getCode_type());
            }
            List<Object> list=pipeline.syncAndReturnAll();
            for(Object obj:list){
                calcuteJsonRates(obj,map);
            }
        }
        Collection<StockUpDownVO> values = map.values();
        return new ArrayList<>(values);
    }

    private void calcuteJsonRates(Object obj,Map<String,StockUpDownVO> map) {
        try {
            JSONObject json = JSONObject.parseObject(obj.toString());
            BigDecimal now = new BigDecimal(json.getString("now"));
            BigDecimal close = new BigDecimal(json.getString("close"));
            String full_code = json.getString("full_code");
            String _code = json.getString("code");
            String status=json.getString("status");
            if (StringUtil.isEmpty(full_code)) {
                full_code = StringUtil.formatStockCode(_code);
            }
            StockUpDownVO vo = map.get(full_code);
            if (vo != null) {
                vo.setName(json.getString("name"));
                vo.setPrice(json.getString("now"));
                vo.setStatus(status);
                if (close.intValue() == 0) {
                    vo.setUp_down("0.00");
                }else {
                    String rate = (now.subtract(close)).divide(close, 4, RoundingMode.HALF_UP).toString();
                    vo.setUp_down(rate);
                }
                map.put(full_code, vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StockUpDownVO calcuteJsonRate(Object obj) {
        try{
            JSONObject json=JSONObject.parseObject(obj.toString());
            BigDecimal now=new BigDecimal(json.getString("now"));
            BigDecimal close=new BigDecimal(json.getString("close"));
            StockUpDownVO vo= new StockUpDownVO();
            String full_code=json.getString("full_code");
            String _code=json.getString("code");
            String status=json.getString("status");
            if(StringUtil.isEmpty(_code)){
                _code=StringUtil.removeStockCode(full_code);
            }
            vo.setName(json.getString("name"));
            vo.setPrice(json.getString("now"));
            vo.setCode(_code);
            vo.setCode_type(StringUtil.formatStockCode(json.getString("full_code")));
            if(close.intValue()==0){
                vo.setUp_down("0.00");
            }else if(status.equals("1")){
                vo.setUp_down("-");
            } else{
                String rate= (now.subtract(close)).divide(close,4, RoundingMode.HALF_UP).toString();
                vo.setUp_down(rate);
            }
            return vo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String calScore(StockRanking sto) {
        Float perank=sto.getPerank()*10;
        Float pbrank=sto.getPbrank()*10;
        Float frank=sto.getFrank()*20;
        Float roerank=sto.getRoerank()*30;
        Float qrank=sto.getQrank()*30;
        return String.valueOf(perank+pbrank+frank+roerank+qrank);
    }

    public void getScores(List<Stock> stocks) {
        if (stocks!=null) {
            for(Stock stock:stocks){
                List<StockRanking> rankings= analysisRatingByCode(stock.getCode_type());
                if(rankings.size()>0){
                    stock.setScore(rankings.get(0).getScore());
                }
            }
        }
    }


    public String getUidByToken(String token) {
        return tokenUtil.getProperty(token,"uid");
    }

    public PointsPraise selectPointsAndPraise(String uid, String news_guid) {
        PointsPraise p= newsMapper.selectPointsAndPraise(uid,news_guid);
        if(p.getPraise().equals("0")){
            p.setPraise("no");
        } else{
            p.setPraise("yes");
        }
        return p;
    }

    public List<News> queryUnionNewsByCode(String code) {
        String likeCode="%"+code;
        return newsMapper.queryUnionNewsByCode(likeCode);
    }
}
