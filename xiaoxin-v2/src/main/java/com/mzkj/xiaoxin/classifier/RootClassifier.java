package com.mzkj.xiaoxin.classifier;

import com.mzkj.xiaoxin.consts.Message;
import com.mzkj.xiaoxin.controller.VO.QandAVO;
import com.mzkj.xiaoxin.service.StockService;
import com.mzkj.xiaoxin.util.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by zhouzhenyang on 2018/9/18
 */
@Service
public class RootClassifier {

    private final StockService stockService;
    private final WordExtractor wordExtractor;

    @Autowired
    public RootClassifier(StockService stockService, WordExtractor wordExtractor) {
        this.stockService = stockService;
        this.wordExtractor = wordExtractor;
    }

    public QandAVO<?> process(String msg) throws Exception {
        String code = wordExtractor.extractWords(msg);
        if (code == null) {
            return new QandAVO<>(Message.UN_KNOWN.type, Message.UN_KNOWN.message);
        }
        //不区分msg类型 所以这个classifier写的和service一样..
        return stockService.getStockAnswer(code);
    }
}
