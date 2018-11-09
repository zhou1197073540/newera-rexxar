package com.mzkj.xiaoxin.controller.VO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * create by zhouzhenyang on 2018/9/18
 */
@Data
@Accessors(chain = true)
public class QandAVO<T> {

    public QandAVO() {
    }

    public QandAVO(String type) {
        this.type = type;
    }

    public QandAVO(String type, T content) {
        this.type = type;
        this.content = content;
    }

    private String type;
    private T content;
}
