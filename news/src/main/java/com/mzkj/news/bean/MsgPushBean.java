package com.mzkj.news.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MsgPushBean {
    private String title;
    private String content;

    public MsgPushBean(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
