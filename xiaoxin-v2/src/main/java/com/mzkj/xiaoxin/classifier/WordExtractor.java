package com.mzkj.xiaoxin.classifier;

import com.mzkj.xiaoxin.repo.PO.SearchStockPO;
import com.mzkj.xiaoxin.repo.mapper205.ClassifierMapper;
import org.apache.commons.lang.StringUtils;
import org.lionsoul.jcseg.tokenizer.Word;
import org.lionsoul.jcseg.tokenizer.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * create by zhouzhenyang on 2018.10.11
 */
@Component
public class WordExtractor {

    @Autowired
    private ClassifierMapper classifierMapper;

    private ADictionary dic;
    private JcsegTaskConfig config;
    private boolean init = false;
    private final Logger logger = LoggerFactory.getLogger(WordExtractor.class);

    public void init() {
        if (!init) {
            try {
                logger.info(this.getClass().getName() + " init start");
                config = new JcsegTaskConfig(true);
                config.setMaxLength(12);
                config.setLexiconPath(null);
                dic = DictionaryFactory.createSingletonDictionary(config, false);
                refreshWords();
                logger.info(this.getClass().getName() + " init over");
            } catch (Exception e) {
                logger.error(this.getClass().getName() + "init error\n", e);
                System.exit(-1);
            }
            init = true;
        }
    }

    public String extractWords(String msg) throws IOException, JcsegException {
        if (StringUtils.isNotBlank(msg)) {
            ISegment seg = SegmentFactory
                    .createJcseg(JcsegTaskConfig.DETECT_MODE, config, dic);
            seg.reset(new StringReader(msg.toLowerCase()));
            IWord word = seg.next();
            if (word != null) {
                return word.getSyn().getRootWord().getValue();
            }
        }
        return null;
    }

    public void refreshWords() throws Exception {
        final List<SearchStockPO> searchStocks = classifierMapper.doGetSearchStockWords();
        ADictionary tmpDic = DictionaryFactory.createDefaultDictionary(config, false, false);
        SearchStockPO searchStock;
        Word rootWord;
        Word synWord;
        SynonymsEntry tongyici;
        for (int i = 0; i < searchStocks.size(); i++) {
            searchStock = searchStocks.get(i);
            rootWord = new Word(searchStock.getFullCode(), ILexicon.CJK_WORD);
            tongyici = new SynonymsEntry(rootWord);
            synWord = new Word(searchStock.getCode(), ILexicon.CJK_WORD);
            tmpDic.add(ILexicon.CJK_WORD, synWord);
            tongyici.add(synWord);
            synWord = new Word(searchStock.getName(), ILexicon.CJK_WORD);
            tmpDic.add(ILexicon.CJK_WORD, synWord);
            tongyici.add(synWord);
            synWord = new Word(searchStock.getPinyinFirstWord().toLowerCase(), ILexicon.CJK_WORD);
            tmpDic.add(ILexicon.CJK_WORD, synWord);
            tongyici.add(synWord);
            rootWord.setSyn(tongyici);
            tmpDic.add(ILexicon.CJK_WORD, rootWord);
        }
        ADictionary toBeCleanedDic = dic;
        dic = tmpDic;
        cleanDic(toBeCleanedDic);
    }

    private void cleanDic(ADictionary dic) throws Exception {
        final Field dics = dic.getClass().getDeclaredField("dics");
        final Field rootMap = dic.getClass().getSuperclass().getDeclaredField("rootMap");
        dics.setAccessible(true);
        rootMap.setAccessible(true);
        final Map<String, IWord>[] o = (Map<String, IWord>[]) dics.get(dic);
        final Map<String, SynonymsEntry> o1 = (Map<String, SynonymsEntry>) rootMap.get(dic);
        o1.clear();
        for (Map<String, IWord> stringIWordMap : o) {
            stringIWordMap.clear();
        }
    }
}
