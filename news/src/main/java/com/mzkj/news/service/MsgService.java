package com.mzkj.news.service;

import com.mzkj.news.bean.*;
import com.mzkj.news.mapper.MsgMapper;
import com.mzkj.news.utils.AndroidPushUtil;
import com.mzkj.news.utils.JSONUtil;
import com.mzkj.news.utils.TokenUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsgService {
    @Autowired
    MsgMapper msgMapper;

    @Autowired
    TokenUtil tokenUtil;


    public int saveOrUpdatePushMsg(MsgPush msg) {
        return msgMapper.saveOrUpdatePushMsg(msg);
    }

    public String getUidByToken(String token) {
        return tokenUtil.getProperty(token,"uid");
    }

    public List<StockPushMsg> queryAllStockBySetPrice() {
        return msgMapper.queryAllStockBySetPrice();
    }

    public Stock selectStockNameByCodeType(String codeType) {
        return msgMapper.selectStockNameByCodeType(codeType);
    }

    public void updateSended(StockPushMsg sp) {
        msgMapper.updateSended(sp);
    }

    public boolean pushNews(ConsumerRecord<?, ?> cr) throws Exception {
        String code = "%" + cr.key();
        System.out.println(cr.value().toString());
        News news = JSONUtil.JSON2Object(cr.value().toString(), News.class);
        System.out.println(news.toString());
        MsgPushNews msg = new MsgPushNews(news);
        MsgPushNewsVO vo = msgMapper.selectPushNewsCode(code);
        return vo != null && AndroidPushUtil.pushAndroidNews(msg, vo.getToken());
    }
}
