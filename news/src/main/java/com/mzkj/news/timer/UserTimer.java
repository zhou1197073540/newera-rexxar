package com.mzkj.news.timer;

import com.mzkj.news.bean.MsgPushStock;
import com.mzkj.news.bean.StockPushMsg;
import com.mzkj.news.service.MsgService;
import com.mzkj.news.utils.AndroidPushUtil;
import com.mzkj.news.utils.QueryPriceUtil;
import com.mzkj.news.utils.StringUtil;
import com.mzkj.news.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserTimer {
    @Autowired
    MsgService msgService;

//    @Scheduled(cron="0 0/5 9-15 * * MON-FRI")
    @Scheduled(cron="0/30 * 9-14 * * MON-FRI")
    public void pushMsg() {
        List<StockPushMsg> spm = msgService.queryAllStockBySetPrice();
        if(spm==null||spm.isEmpty()) return;
        List<String> codes = new ArrayList<>();
        spm.forEach(x -> codes.add(x.getCode_type()));
        Map<String, String> map = QueryPriceUtil.queryStockLatestPriceByCode(codes);
        for (StockPushMsg sp : spm) {
            try {
                if(StringUtil.isEmpty(sp.getApp_token())) continue;
                float lastPrice = Float.parseFloat(map.get(sp.getCode_type()));
                if (("1".equals(sp.getWarm_type()) && lastPrice >= sp.getWarm_price() ||
                        ("0".equals(sp.getWarm_type()) && lastPrice <= sp.getWarm_price()))) {
                    System.out.println("-----------app推送消息-----------");
                    MsgPushStock mpsMap = new MsgPushStock(sp.getCode_type(), sp.getStock_name(), StringUtil.removeStockCode(sp.getCode_type()));
                    boolean res = AndroidPushUtil.sendAndroidByToken(mpsMap, sp.getApp_token(),sp.getWarm_price());
                    if (res) msgService.updateSended(sp);
                    System.out.println("app推送消息："+sp.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
