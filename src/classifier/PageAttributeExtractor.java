package classifier;

import javafx.util.Pair;
import org.jsoup.nodes.Document;
import weka.core.stemmers.LovinsStemmer;
import weka.core.stemmers.Stemmer;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static util.LoggingUtils.*;

/**
 * Extracts the features of the received page or list of pages.
 *
 * @author Pedro Henrique
 */
public class PageAttributeExtractor {

    /**
     * Prevents instantiation.
     */
    private PageAttributeExtractor() {
    }

    /**
     * Returns a list with pairs of maps, each pair represents an document, the first map is the words frequency and the
     * second is the stemmed words frequency.
     *
     * @param documents the documents to process
     * @return a list of pairs of maps
     * @see #collectDocumentData(Document)
     */
    public static List<Pair<Map<String, Integer>, Map<String, Integer>>> collectDocumentsData(List<Document> documents) {

        //
        global().entering(className(), methodName());
        List<Pair<Map<String, Integer>, Map<String, Integer>>> stemmedDocuments = documents.stream().parallel()
                .map(PageAttributeExtractor::collectDocumentData)
                .collect(Collectors.toList());

        //
        global().exiting(className(), methodName());
        return stemmedDocuments;
    }

    /**
     * Returns a pair of maps, the first map is the words frequency and the second is the stemmed words frequency.
     *
     * @param document the document to process
     * @return a pair of maps with words (normal in first and stemmed in second) frequency
     */
    public static Pair<Map<String, Integer>, Map<String, Integer>> collectDocumentData(Document document) {
        return new Pair<>(collectDocumentWordsFrequency(document), collectDocumentStemmedWordsFrequency(document));
    }

    /**
     * Returns the document words with their frequency.
     *
     * @param document the document to process
     * @return a map with the words and frequency
     */
    private static Map<String, Integer> collectDocumentWordsFrequency(Document document) {
        return Stream.of(document.text().split(" "))
                .map(word -> word.replaceAll("[^A-Za-z]", ""))
                .filter(word -> word.length() > 1)
                .collect(Collectors.toMap(Function.identity(), word -> 1, Integer::sum));
    }

    /**
     * Returns the stemmed document words with their frequency.
     *
     * @param document the document to process
     * @return a map with the stemmed words and frequency
     */
    private static Map<String, Integer> collectDocumentStemmedWordsFrequency(Document document) {
        Stemmer stemmer = new LovinsStemmer();
        return Stream.of(document.text().split(" "))
                .map(word -> word.replaceAll("[^A-Za-z]", ""))
                .map(stemmer::stem)
                .filter(word -> word.length() > 1)
                .collect(Collectors.toMap(Function.identity(), word -> 1, Integer::sum));
    }
}
