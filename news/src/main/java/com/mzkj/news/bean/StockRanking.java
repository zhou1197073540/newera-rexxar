package com.mzkj.news.bean;

public class StockRanking {
    private String code;
    private Float perank;
    private Float pbrank;
    private Float frank;
    private Float roerank;
    private Float qrank;
    private String date;
    private String evaluation;
    private String score;//综合评分

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Float getPerank() {
        return perank;
    }

    public void setPerank(Float perank) {
        this.perank = perank;
    }

    public Float getPbrank() {
        return pbrank;
    }

    public void setPbrank(Float pbrank) {
        this.pbrank = pbrank;
    }

    public Float getFrank() {
        return frank;
    }

    public void setFrank(Float frank) {
        this.frank = frank;
    }

    public Float getRoerank() {
        return roerank;
    }

    public void setRoerank(Float roerank) {
        this.roerank = roerank;
    }

    public Float getQrank() {
        return qrank;
    }

    public void setQrank(Float qrank) {
        this.qrank = qrank;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }
}
