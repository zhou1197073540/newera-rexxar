package com.mzkj.news.bean;

public class Stock {
    private String code;
    private String code_type;
    private String name;
    private String score;


    public Stock() {}
    public Stock(String code, String name,String code_type) {
        this.code = code;
        this.name = name;
        this.code_type = code_type;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCode_type() {
        return code_type;
    }

    public void setCode_type(String code_type) {
        this.code_type = code_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
