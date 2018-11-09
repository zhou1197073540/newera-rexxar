package com.mouzhiapp.stock_market.repo.PO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * create by zhouzhenyang on 2018/8/17
 */
@Data
@Accessors(chain = true)
public class KLineWithLimitDate {

    public KLineWithLimitDate(LocalDateTime limitStartDate, LocalDateTime limitEndDate, String kLine) {
        this.limitStartDate = limitStartDate;
        this.limitEndDate = limitEndDate;
        this.kLine = kLine;
    }

    LocalDateTime limitStartDate;
    LocalDateTime limitEndDate;
    String kLine;
}
