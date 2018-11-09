package com.mzkj.news.bean_vo;

public class UserVo {
    private String username;
    private String phone_num;
    private String token;
    private String a_account;

    public String getA_account() {
        return a_account;
    }

    public void setA_account(String a_account) {
        this.a_account = a_account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserVo(String username, String phone_num, String token,String a_account) {
        this.username = username;
        this.phone_num = phone_num;
        this.token = token;
        this.a_account=a_account;
    }
    public UserVo() {
    }
}
