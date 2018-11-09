package com.mzkj.news_classifier;

import org.apache.flink.api.common.functions.RichMapFunction;

import java.util.Set;

/**
 * create by zhouzhenyang on 2018/9/25
 */
public class StockTagPinner extends RichMapFunction<StockNewsEvent, TagEvent> {
    private static final long serialVersionUID = 7770247482371647271L;

    @Override
    public TagEvent map(StockNewsEvent newsEvent) throws Exception {
        StockKeyWordsExtractor extractor = StockKeyWordsExtractor.getInstance();
        final Set<String> keyWords = extractor.extractKeyWord(newsEvent);
        TagEvent tagEvent =
                new TagEvent(newsEvent.getGuid(), newsEvent.getDatetime(),
                        newsEvent.getTimestamp(), keyWords);
        if (null != newsEvent.getTags()) {
            tagEvent.getTags().addAll(newsEvent.getTags());
        }
        return tagEvent;
    }
}
