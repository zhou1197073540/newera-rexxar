package com.mzkj.news.bean;

import java.util.HashMap;
import java.util.Set;

public class MsgPushNews extends HashMap<String,Object>{
    public final static String package_name="com.newerarnd.stock.NewsInfoActivity";

    private MsgPushNews(){}

    public MsgPushNews(News news) {
        super.put("tipValue","头条");
        super.put("titleValue",news.getTitle());
        super.put("infoPublDateValue",news.getPublish_time());
        super.put("newsId",news.getGuid());
        super.put("typeIndex",3);
    }

    public MsgPushNews push(String key,String val){
        this.put(key,val);
        return this;
    }
    public String getTitle(){
        return this.get("titleValue").toString();
    }
    public String getNewsId(){
        return this.get("newsId").toString();
    }

}
