package com.mzkj.news_classifier;

import org.apache.commons.lang3.StringUtils;
import org.lionsoul.jcseg.tokenizer.core.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

/**
 * create by zhouzhenyang on 2018/9/27
 */
public class StockKeyWordsExtractor {

    private ISegment seg;
    private static boolean init = false;

    private static final StockKeyWordsExtractor extractor = new StockKeyWordsExtractor();

    private StockKeyWordsExtractor() {
    }

    public static StockKeyWordsExtractor getInstance() throws JcsegException, IOException {
        if (!init) {
            extractor.selfInit();
            init = true;
        }
        return extractor;
    }

    private void selfInit() throws IOException {
        JcsegTaskConfig config = new JcsegTaskConfig(true);
        final String lexiconPath = System.getProperty("user.home") + "/lexicon";
        System.out.println(lexiconPath);
        config.setLexiconPath(new String[]{lexiconPath});
        ADictionary dic = DictionaryFactory.createDefaultDictionary(config, true);
        try {
            seg = SegmentFactory
                    .createJcseg(JcsegTaskConfig.DETECT_MODE, config, dic);
        } catch (JcsegException e) {
            throw new RuntimeException(StockKeyWordsExtractor.class.getName() + "init error ", e);
        }
    }

    public Set<String> extractKeyWord(StockNewsEvent newsEvent) throws IOException {
        IWord word = null;
        Set<String> ret = new HashSet<>(10);
        if (StringUtils.isNoneBlank(newsEvent.getWhole_content())) {
            seg.reset(new StringReader(newsEvent.getWhole_content()));
            while ((word = seg.next()) != null) {
                ret.add(word.getValue());
            }
        }
        if (StringUtils.isNoneBlank(newsEvent.getContent())) {
            seg.reset(new StringReader(newsEvent.getContent()));
            while ((word = seg.next()) != null) {
                ret.add(word.getValue());
            }
        }
        if (StringUtils.isNoneBlank(newsEvent.getTitle())) {
            seg.reset(new StringReader(newsEvent.getTitle()));
            while ((word = seg.next()) != null) {
                ret.add(word.getValue());
            }
        }
        return ret;
    }


    public static void main(String[] args) throws Exception {
        final StockKeyWordsExtractor extractor = StockKeyWordsExtractor.getInstance();
        final Set<String> strings = extractor.extractKeyWord(new StockNewsEvent("hehe浦发银行", "xixi600000"));
        System.out.println(strings);
    }
}
