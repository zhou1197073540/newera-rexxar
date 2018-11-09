package com.mzkj.news.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mzkj.news.bean.*;
import com.mzkj.news.bean_vo.*;
import com.mzkj.news.mapper.StockerMapper;
import com.mzkj.news.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StockService {

    @Autowired
    StockerMapper stockerMapper;
    @Autowired
    TokenUtil tokenUtil;

    public StockRecommendAlpha selectStockRecommendLastOne() {
        List<StockRecommendAlpha> alphas = stockerMapper.selectStockRecommendLastOne();
        StockRecommendAlpha first = new StockRecommendAlpha();
        first.setIndex(alphas.get(0).getIndex());
        for (int i = 0; i < alphas.size(); i++) {
            Float[] f = getValue(alphas, i);
            if (i == 0) {
                first.setRate0(f[0]);
                first.setB0(f[1]);
            } else if (i == 1) {
                first.setRate1(f[0]);
                first.setB1(f[1]);
            } else if (i == 2) {
                first.setRate2(f[0]);
                first.setB2(f[1]);
            } else if (i == 3) {
                first.setRate3(f[0]);
                first.setB3(f[1]);
            }
        }
        return first;
    }

    public String getUidByToken(String token) {
        return tokenUtil.getProperty(token,"uid");
    }

    public Float[] getValue(List<StockRecommendAlpha> alphas,int type){
        if(type==0){
            for(StockRecommendAlpha alpha:alphas){
                if(alpha.getRate0()!=0&&alpha.getB0()!=0) return new Float[]{alpha.getRate0(),alpha.getB0()};
            }
        }else if(type==1){
            for(StockRecommendAlpha alpha:alphas){
                if(alpha.getRate1()!=0&&alpha.getB1()!=0) return new Float[]{alpha.getRate1(),alpha.getB1()};
            }
        }else if(type==2){
            for(StockRecommendAlpha alpha:alphas){
                if(alpha.getRate2()!=0&&alpha.getB2()!=0) return new Float[]{alpha.getRate2(),alpha.getB2()};
            }
        }else if(type==3){
            for(StockRecommendAlpha alpha:alphas){
                if(alpha.getRate3()!=0&&alpha.getB3()!=0) return new Float[]{alpha.getRate3(),alpha.getB3()};
            }
        }
        return null;
    }

    public List<IndexTiming> getIndexTimimg(String code) {
        return stockerMapper.getIndexTimimg(code);
    }

    public List<CumAlpha> getCumAlpha() {
        return stockerMapper.getCumAlpha();
    }

    public List<GrailTime> grailTime(String ticker) {
        List<GrailTime> grails=stockerMapper.grailTime(ticker);
        Collections.reverse(grails);
        return grails;
    }

    public String stockRanking(JSONObject json) {
        String token=json.getString("token");
        String uid=tokenUtil.getProperty(token,"uid");
        if(StringUtil.isEmpty(uid)) return "token过期！！";
        JSONArray array=JSONArray.parseArray(json.getString("data"));
        for(Object ob:array){
            JSONObject oo=JSONObject.parseObject(ob.toString());
            String code=StringUtil.formatStockCode(oo.getString("code"));
            String rank=oo.getString("rank");
            stockerMapper.updateStockRanking(uid,code,rank);
        }
        return "ok！！";
    }

    public FundVO fundRecommend() {
        FofWeight fof=stockerMapper.selectFundGroupFromFofWeight();
        FundVO vo=new FundVO();
        for(int i=1;i<=5;i++){
            FundGroup fund=getFundGroup(fof,i);
            if (fund !=null) vo.fund_group.add(fund);
        }
        List<FundHistory> funds=stockerMapper.selectFundHistory();
        vo.setList_fund(funds);
        return vo;
    }

    private FundGroup getFundGroup(FofWeight fof, int i) {
        FundGroup fundGroup=new FundGroup();
        fundGroup.setDate_time(fof.getIndex());
        if(i==1){
            if(!StringUtil.isEmpty(fof.getT1(),fof.getW1())){
                String fund_name=stockerMapper.selectFundName(fof.getT1());
                fundGroup.setFund_code(fof.getT1());
                fundGroup.setFund_name(fund_name);
                fundGroup.setFund_hold_rate(fof.getW1());
                return fundGroup;
            }
        }else if(i==2){
            if(!StringUtil.isEmpty(fof.getT2(),fof.getW2())){
                String fund_name=stockerMapper.selectFundName(fof.getT2());
                fundGroup.setFund_code(fof.getT2());
                fundGroup.setFund_name(fund_name);
                fundGroup.setFund_hold_rate(fof.getW2());
                return fundGroup;
            }
        }else if(i==3){
            if(!StringUtil.isEmpty(fof.getT3(),fof.getW3())){
                String fund_name=stockerMapper.selectFundName(fof.getT3());
                fundGroup.setFund_code(fof.getT3());
                fundGroup.setFund_name(fund_name);
                fundGroup.setFund_hold_rate(fof.getW3());
                return fundGroup;
            }
        }else if(i==4){
            if(!StringUtil.isEmpty(fof.getT4(),fof.getW4())){
                String fund_name=stockerMapper.selectFundName(fof.getT4());
                fundGroup.setFund_code(fof.getT4());
                fundGroup.setFund_name(fund_name);
                fundGroup.setFund_hold_rate(fof.getW4());
                return fundGroup;
            }
        }else if(i==5){
            if(!StringUtil.isEmpty(fof.getT5(),fof.getW5())){
                String fund_name=stockerMapper.selectFundName(fof.getT5());
                fundGroup.setFund_code(fof.getT5());
                fundGroup.setFund_name(fund_name);
                fundGroup.setFund_hold_rate(fof.getW5());
                return fundGroup;
            }
        }
        return fundGroup;
    }

    public  List<FundGroup> fundWarehouse() {
        List<FofWeight> fofs=stockerMapper.selectAllFundGroupFromFofWeight();
        if (fofs==null) return null;
        List<FundGroup> funds=new ArrayList<>();
        for(FofWeight fof:fofs){
            FundGroup group=new FundGroup();
            group.setFund_name(fof.getFund_name());
            group.setFund_code(fof.getFund_code());
            group.setDate_time(fof.getIndex());
            group.setFund_hold_rate(getHoldRate(fof));
            funds.add(group);
        }
        return funds;
    }

    private String getHoldRate(FofWeight fof) {
        if (fof==null) return null;
        String fund_code=fof.getFund_code();
        if(fund_code.equals(fof.getT1())){
            return fof.getW1();
        }else if(fund_code.equals(fof.getT2())){
            return fof.getW2();
        }else if(fund_code.equals(fof.getT3())){
            return fof.getW3();
        }else if(fund_code.equals(fof.getT4())){
            return fof.getW4();
        }else if(fund_code.equals(fof.getT5())){
            return fof.getW5();
        }
        return null;
    }


    public Map<String,Object> fundInfo(String fund_code) {
        FundInfo info=stockerMapper.selectFundInfo(fund_code);
        List<FundNetValue> nets=stockerMapper.selectFundHistoryByCode(fund_code);
        FundChange fchg=null;
        try{
            fchg=stockerMapper.selectFundChange(fund_code);
        }catch (Exception e){
            fchg=new FundChange();
            fchg.setFund_code(fund_code);
            fchg.setTotal_net_value("104.556");
            fchg.setMonthly_total_chg("0.0005733944954129083");
            fchg.setWeekly_total_chg("0.050571944611679756");
            fchg.setDate(TimeUtil.getCurDateTime());
            fchg.setMonthly_rank("10");
            fchg.setDaily_chg_rate("1.34%");
        }
        Collections.reverse(nets);
        Map<String,Object> map=new HashMap<>();
        map.put("fund_info",info);
        map.put("fund_net_value",nets);
        map.put("fund_change",fchg);
        return map;
    }

    public StockPredictionVO stockTrendPrediction(String code_type) {
        return stockerMapper.selectStockTrendPrediction(code_type);
    }

    public List<IndustryIndex> stockIndustryIndex(String industry_name) {
       return stockerMapper.stockIndustryIndex(industry_name);
    }

    public KPracticeVO kLinePractice(JSONObject obj,String uid) {
        KlinePractice kp= JSONUtil.JSON2Object(obj.toString(),KlinePractice.class);
        if(null==kp) return null;
        kp.setUid(uid);
        stockerMapper.saveKlineExperience(kp);
        stockerMapper.saveKlineResult(kp);
        KPracticeVO experience=stockerMapper.selectKlineExperience(kp);
        Map<String,String> map=stockerMapper.selectKlineBeatRate(kp);
        String big_num= String.valueOf(map.get("big_num"));
        String total= String.valueOf(map.get("total"));
        String rate= MathUtil.div(big_num,total);
        kp.setDefeat_people(rate);
        kp.setLevel(MathUtil.add2Str(MathUtil.divFloor(experience.getExperience(),"500"),"1"));
        KPracticeVO vo =JSONUtil.JSON2Object(kp,KPracticeVO.class);
        vo.setPhone_num(experience.getPhone_num());
        vo.setExperience(experience.getExperience());
        return vo;
    }

    public List<KPracticeVO> kLinePracticeSelfRecords(String uid) {
        return stockerMapper.kLinePracticeSelfRecords(uid);
    }

    public List<KlineRank> kLinePracticeAllRecords() {
        return stockerMapper.kLinePracticeAllRecords();
    }

    public Map<String,String> kLineUserInfo(String uid) {
        return stockerMapper.kLineUserInfo(uid);
    }

    public List<KlineRank> kLinePracticeAllRecordsByType(String type) {
        return stockerMapper.kLinePracticeAllRecordsByType(type);
    }

    public List<StockReport> selectLatestStockReport() {
        return stockerMapper.selectLatestStockReport();
    }

    public ZhengGu searchStockReport(String code) {
        return stockerMapper.searchStockReport(code);
    }

    public List<AKShares> stockPriceRatio(int page_size, int page_num) {
        if(page_size<=0)page_size=0;
        if(page_num<=0)page_num=0;
        if(page_size>1000)page_size=1000;
        if(page_num>100)page_num=100;
        int page_index=page_size*page_num;
        return stockerMapper.stockPriceRatio(page_size,page_index);//20,40
    }

    public StockPledgeRatioVO stockPledgeRatio(String code) {
        PledgeRatio ratio= stockerMapper.queryStockPledgeRatio(code);
        List<PledgeGD> gds=stockerMapper.queryStockPledgeGD(code);
        float avg_c2=calAvgC2(gds);
        StockLatest slf = QueryPriceUtil.getStockLatestPrice(StringUtil.formatStockCode(code));
        return new StockPledgeRatioVO(code,avg_c2,slf.getNow(),slf.getName(),gds,ratio);
    }

    private float calAvgC2(List<PledgeGD> gds) {
        if(gds==null||gds.isEmpty()) return 0;
        int num=0;
        float sum=0;
        for(PledgeGD gd:gds){
            if(gd.getC2()!=null){
                num++;
                sum+=Float.parseFloat(gd.getC2());
            }
        }
        if(num==0) return 0;
        return sum/num;
    }


    public List<StockPledgeRiskVO> stockPledgeRatioRisk() {
        return stockerMapper.stockPledgeRatioRisk();
    }

    public List<HangQing> stockHangQing(int page_size,int page_num) {
        if(page_size<=0)page_size=0;
        if(page_num<=0)page_num=0;
        if(page_size>1000)page_size=1000;
        if(page_num>100)page_num=100;
        int page_index=page_size*page_num;
        return stockerMapper.stockHangQing(page_size,page_index);
    }
}
