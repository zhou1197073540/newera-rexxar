package com.mzkj.news.bean;

public class AppVersion {
    private String update;
    private String new_version;
    private String apk_file_url;
    private String update_log;
    private String target_size;
    private String new_md5;
    private String constraint;

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getNew_version() {
        return new_version;
    }

    public void setNew_version(String new_version) {
        this.new_version = new_version;
    }

    public String getApk_file_url() {
        return apk_file_url;
    }

    public void setApk_file_url(String apk_file_url) {
        this.apk_file_url = apk_file_url;
    }

    public String getUpdate_log() {
        return update_log;
    }

    public void setUpdate_log(String update_log) {
        this.update_log = update_log;
    }

    public String getTarget_size() {
        return target_size;
    }

    public void setTarget_size(String target_size) {
        this.target_size = target_size;
    }

    public String getNew_md5() {
        return new_md5;
    }

    public void setNew_md5(String new_md5) {
        this.new_md5 = new_md5;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }
}
