package com.mzkj.news.bean_vo;

import java.util.ArrayList;
import java.util.List;

public class FundVO {
    public List<FundHistory> list_fund;
    public List<FundGroup> fund_group=new ArrayList<>();

    public List<FundHistory> getList_fund() {
        return list_fund;
    }

    public void setList_fund(List<FundHistory> list_fund) {
        this.list_fund = list_fund;
    }
}
