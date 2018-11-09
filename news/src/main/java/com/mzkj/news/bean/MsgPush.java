package com.mzkj.news.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
//@JsonIgnoreProperties(ignoreUnknown=true)
public class MsgPush {
    private String token; //app的token
    private String account; //用户的uid
    private String platform; //平台
}
