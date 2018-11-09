package com.mzkj.xiaoxin.classifier.PO;

import com.mzkj.xiaoxin.repo.PO.StockBasicInfoPO;
import com.mzkj.xiaoxin.repo.PO.ZhengguPO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * create by zhouzhenyang on 2018/9/18
 */
@Data
@Accessors(chain = true)
public class StockAnswerRoot {

    public StockAnswerRoot() {

    }

    public StockAnswerRoot(StockBasicInfoPO stockBasicInfo,
                           Object timeLine,
                           ZhengguPO zhenggu) {
        this.stockBasicInfo = stockBasicInfo;
        this.timeLine = timeLine;
        this.zhenggu = zhenggu;
    }

    StockBasicInfoPO stockBasicInfo;
    Object timeLine;
    ZhengguPO zhenggu;
}
