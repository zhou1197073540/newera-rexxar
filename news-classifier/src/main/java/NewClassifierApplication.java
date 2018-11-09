import com.mzkj.news_classifier.NewsClassifier;
import com.mzkj.news_classifier.StockKeyWordsExtractor;

/**
 * create by zhouzhenyang on 2018/9/19
 */
public class NewClassifierApplication {

    public static void main(String[] args) throws Exception {
        NewsClassifier classifier = new NewsClassifier();
        classifier.run();
    }
}
