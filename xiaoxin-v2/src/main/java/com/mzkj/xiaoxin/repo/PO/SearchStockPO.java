package com.mzkj.xiaoxin.repo.PO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * create by zhouzhenyang on 2018.10.11
 */
@Data
@Accessors(chain = true)
public class SearchStockPO {

    private String fullCode;
    private String code;
    private String name;
    private String pinyinFirstWord;

}
