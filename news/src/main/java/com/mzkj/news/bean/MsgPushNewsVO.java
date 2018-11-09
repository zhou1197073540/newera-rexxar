package com.mzkj.news.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MsgPushNewsVO extends MsgPush{
    private String code;
}
