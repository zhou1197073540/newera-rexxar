package com.mzkj.news.bean;

import com.mzkj.news.utils.TimeUtil;

public class MessageCenter {
    private String title;
    private String content;
    private String datetime= TimeUtil.getDateTime();
    private String type;
    private String user_uid;
    private String platform;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public MessageCenter (){
    }

    public MessageCenter (String user_uid,String type,String psd){
        this.title="新用户通知";
        this.content="尊敬的用户，您好！您的初始密码为:"+psd+"，请及时上官网 https://www.mzkj88.com 登录修改密码。";
        this.type=type;
        this.user_uid=user_uid;
        this.platform="app";
    }
}
