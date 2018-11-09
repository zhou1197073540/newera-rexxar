package com.mzkj.news.controller;

import com.alibaba.fastjson.JSONObject;
import com.mzkj.news.bean.*;
import com.mzkj.news.bean_vo.*;
import com.mzkj.news.dto.RespResult;
import com.mzkj.news.service.StockService;
import com.mzkj.news.utils.RedisUtil;
import com.mzkj.news.utils.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StockController {

    @Autowired
    StockService stockService;

    /**
     *统计每日选股有效性
     */
    @ResponseBody
    @GetMapping("/dailyStockRecomment")
    public RespResult getStockRecomment(){
        StockRecommendAlpha alpha= stockService.selectStockRecommendLastOne();
        return RespResult.genSuccessResult(alpha);
    }

    /**
     *智投功能：大盘择时信号15分钟
     */
    @ResponseBody
    @GetMapping("/todayIndexTiming")
    public RespResult getIndexTiming(@RequestParam("code") String code){
        List<IndexTiming> index= stockService.getIndexTimimg(code);
        return RespResult.genSuccessResult(index);
    }

    /**
     *历史每日选股计算
     */
    @ResponseBody
    @GetMapping("/cumAlpha")
    public RespResult getCumAlpha(){
        List<CumAlpha> index= stockService.getCumAlpha();
        return RespResult.genSuccessResult(index);
    }

    /**
     *证明大盘择时信号有效性
     */
    @ResponseBody
    @GetMapping("/s/grailTime")
    public RespResult grailTime(@RequestParam("code") String code){
        List<GrailTime> index= stockService.grailTime(code);
        return RespResult.genSuccessResult(index);
    }

    /**
     *择时信号--股票预测赢大奖
     * 用户投票
     */
    @ResponseBody
    @GetMapping("/s/vote")
    public RespResult stockPredict(HttpServletRequest request){
        String type=request.getParameter("type");
        if(StringUtil.isEmpty(type)) return RespResult.genErrorResult("type参数不能为空！");
        VoteStock vo=new VoteStock();
        try(Jedis jedis= RedisUtil.getJedis()){
            Map<String,String> map=new HashMap<>();
            Map<String,String> last_vote=jedis.hgetAll("app-timing-predict");
            String up=last_vote.get("up");
            String down=last_vote.get("down");
            if(!StringUtil.isEmpty(up,down)){
                if("up".equals(type)){
                    int up_add=Integer.parseInt(up)+1;
                    map.put("up",String.valueOf(up_add));
                    map.put("down",String.valueOf(down));
                }else{
                    int down_add=Integer.parseInt(down)+1;
                    map.put("up",String.valueOf(up));
                    map.put("down",String.valueOf(down_add));
                }
            }
            jedis.hmset("app-timing-predict",map);
            Map<String,String> mmap=jedis.hgetAll("app-timing-predict");
            genVoteBean(vo,mmap);
        }
        return RespResult.genSuccessResult(vo);
    }

    private void genVoteBean(VoteStock vo,Map<String, String> mmap) {
        if(mmap==null) return;
        try{
            BigDecimal total=new BigDecimal(mmap.get("up")).add(new BigDecimal(mmap.get("down")));
            if(total.intValue()>0){
                BigDecimal up_rate=new BigDecimal(mmap.get("up")).divide(total,4,BigDecimal.ROUND_HALF_UP);
                BigDecimal down_rate=new BigDecimal(1).subtract(up_rate);
                vo.setUp_num(mmap.get("up"));
                vo.setDown_num(mmap.get("down"));
                vo.setUp_rate(String.valueOf(up_rate));
                vo.setDown_rate(String.valueOf(down_rate));
            }else{
                vo.setUp_num("0");
                vo.setDown_num("0");
                vo.setUp_rate("0.00");
                vo.setDown_rate("0.00");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *择时信号--股票预测赢大奖
     * 用户投票
     */
    @ResponseBody
    @GetMapping("/s/g-vote")
    public RespResult getStockVote(){
        VoteStock vo=new VoteStock();
        try(Jedis jedis= RedisUtil.getJedis()){
            Map<String,String> mmap=jedis.hgetAll("app-timing-predict");
            genVoteBean(vo,mmap);
        }
        return RespResult.genSuccessResult(vo);
    }
    /**
     *行情-自选-股票排序
     */
    @ResponseBody
    @PostMapping("/s/stock-ranking")
    public RespResult stockRanking(@RequestBody JSONObject json){
        String msg=stockService.stockRanking(json);
        return RespResult.genSuccessResult(msg);
    }

    /**
     *基金-推荐组合
     */
    @ResponseBody
    @GetMapping("/s/fund-recommend")
    public RespResult fundRecommend(){
        FundVO fundvo=stockService.fundRecommend();
        return RespResult.genSuccessResult(fundvo);
    }
    /**
     *基金-调仓记录
     */
    @ResponseBody
    @GetMapping("/s/fund-warehouse")
    public RespResult fundWarehouse(){
        List<FundGroup> fundvo=stockService.fundWarehouse();
        return RespResult.genSuccessResult(fundvo);
    }

    /**
     *基金详情
     */
    @ResponseBody
    @GetMapping("/s/fund-info")
    public RespResult fundInfo(@RequestParam("fund_code") String fund_code){
        Map<String,Object> fund=stockService.fundInfo(fund_code);
        return RespResult.genSuccessResult(fund);
    }

    /**
     *股票预测
     */
    @ResponseBody
    @GetMapping("/s/stock-trend-prediction")
    public RespResult stockTrendPrediction(@RequestParam("code_type") String code_type){
        StockPredictionVO vo=stockService.stockTrendPrediction(code_type);
        return RespResult.genSuccessResult(vo);
    }

    /**
     *股票行业指数
     */
    @ResponseBody
    @GetMapping("/s/stock-industry-index")
    public RespResult stockIndustryIndex(@RequestParam("industry_name") String industry_name){
        List<IndustryIndex> vo=stockService.stockIndustryIndex(industry_name);
        return RespResult.genSuccessResult(vo);
    }

    /**
     *模拟训练营
     */
    @ResponseBody
    @PostMapping("/s/kline-practice-settlement")
    public RespResult kLinePractice(@RequestBody JSONObject obj){
        String uid=stockService.getUidByToken(obj.getString("token"));
        if(StringUtil.isEmpty(uid)) return RespResult.genErrorResult("token已过期！");
        KPracticeVO vo=stockService.kLinePractice(obj,uid);
        return RespResult.genSuccessResult(vo);
    }

    /**
     *查询模拟训练营个人记录
     */
    @ResponseBody
    @GetMapping("/s/kline-practice-selfrecords")
    public RespResult kLinePracticeSelfRecords(@RequestParam("token") String token){
        String uid=stockService.getUidByToken(token);
        if(StringUtil.isEmpty(uid)) return RespResult.genErrorResult("token已过期！");
        List<KPracticeVO> vo=stockService.kLinePracticeSelfRecords(uid);
        return RespResult.genSuccessResult(vo);
    }
    /**
     *查询模拟训练营所有记录
     */
    @ResponseBody
    @GetMapping("/s/kline-practice-allrecords")
    public RespResult kLinePracticeAllRecords(){
        List<KlineRank> vo=stockService.kLinePracticeAllRecords();
        return RespResult.genSuccessResult(vo);
    }

    /**
     *查询模拟训练营所有记录
     */
    @ResponseBody
    @GetMapping("/s/kline-practice-allrecords/{type}")
    public RespResult kLinePracticeAllRecordsByType(@PathVariable("type")String type){
        List<KlineRank> vo=stockService.kLinePracticeAllRecordsByType(type);
        return RespResult.genSuccessResult(vo);
    }

    /**
     *查询模拟训练营用户的手机号和等级
     */
    @ResponseBody
    @GetMapping("/s/kline-user-info")
    public RespResult kLineUserInfo(@RequestParam("token") String token){
        String uid=stockService.getUidByToken(token);
        if(StringUtil.isEmpty(uid)) return RespResult.genErrorResult("token已过期！");
        Map<String,String> vo=stockService.kLineUserInfo(uid);
        return RespResult.genSuccessResult(vo);
    }

    /**
     * 最新10条股票 研报
     */
    @ResponseBody
    @GetMapping("/s/stock-report")
    public RespResult stockReport(){
        List<StockReport> list=stockService.selectLatestStockReport();
        return RespResult.genSuccessResult(list);
    }
    /**
     * 根据用户发的消息，股票号等等，返回对应的股票的信息
     */
    @ResponseBody
    @GetMapping("/s/search-stock-report/{code}")
    public RespResult searchStockReport(@PathVariable String code){
        ZhengGu list=stockService.searchStockReport(code);
        if(null==list) return RespResult.genErrorResult("暂时没有改股票的相关信息！");
        return RespResult.genSuccessResult(list);
    }

    /**
     * A股港股比价
     */
    @ResponseBody
    @GetMapping("/s/stock-price-ratio/{page_size}/{page_num}")
    public RespResult stockPriceRatio(@PathVariable("page_size")int page_size,
                                      @PathVariable("page_num")int page_num){
        List<AKShares> list=stockService.stockPriceRatio(page_size,page_num);
        if(null==list||list.isEmpty()) return RespResult.genErrorResult("暂无数据！");
        return RespResult.genSuccessResult(list);
    }

    /**
     * 股权质押
     */
    @ResponseBody
    @GetMapping("/s/stock-pledge-ratio")
    public RespResult stockPledgeRatio(@RequestParam("code")String code){
        StockPledgeRatioVO vo=stockService.stockPledgeRatio(code);
        if(null==vo) return RespResult.genErrorResult("暂无数据！");
        return RespResult.genSuccessResult(vo);
    }

    /**
     * 股权质押风险计算
     */
    @ResponseBody
    @GetMapping("/s/stock-pledge-ratio/risk")
    public RespResult stockPledgeRatioRisk(){
        List<StockPledgeRiskVO> vos=stockService.stockPledgeRatioRisk();
        if(null==vos||vos.isEmpty()) return RespResult.genErrorResult("暂无数据！");
        return RespResult.genSuccessResult(vos);
    }
    /**
     * 股权质押风险计算
     */
    @ResponseBody
    @GetMapping("/s/stock-hangqing/{page_size}/{page_num}")
    public RespResult stockHangQing(@PathVariable("page_size") int page_size,
                                    @PathVariable("page_num") int page_num){
        List<HangQing> vos=stockService.stockHangQing(page_size,page_num);
        if(null==vos||vos.isEmpty()) return RespResult.genErrorResult("暂无数据！");
        return RespResult.genSuccessResult(vos);
    }

}
