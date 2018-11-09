package com.mzkj.news.bean;

public class SelfSelectStock {
    private String uid;
    private String code;
    private String rank;
    public SelfSelectStock(String uid, String code) {
        this.uid = uid;
        this.code = code;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
