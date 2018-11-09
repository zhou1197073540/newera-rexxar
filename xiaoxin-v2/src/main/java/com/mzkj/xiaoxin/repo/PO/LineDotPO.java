package com.mzkj.xiaoxin.repo.PO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * create by zhouzhenyang on 2018/5/22
 */
@Data
@Accessors(chain = true)
public class LineDotPO {

    private String open;
    private String close;
    private String high;
    private String low;
    private String volume;
    private LocalDateTime datetime;

}
