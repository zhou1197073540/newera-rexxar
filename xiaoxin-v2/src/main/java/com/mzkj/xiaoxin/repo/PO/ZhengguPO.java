package com.mzkj.xiaoxin.repo.PO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * create by zhouzhenyang on 2018/9/18
 */
@Data
@Accessors(chain = true)
public class ZhengguPO {
    private String ticker;
    private BigDecimal resistance;
    private BigDecimal support;
    private BigDecimal costPrice;
    private BigDecimal alpha;
    private BigDecimal rate5;
    private String status;
}
