package classifier;

import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import weka.core.stemmers.LovinsStemmer;
import weka.core.stemmers.Stemmer;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Class that contains some methods to help the page processing.
 *
 * @author Pedro Henrique
 */
public class PageUtils {

    /**
     * Prevents instantiation.
     */
    private PageUtils() {
    }

    /**
     * Loads a list of pages from the received URLs. If some page could not be loaded,  it is removed from the resultant
     * map.
     *
     * @param urls     the urls
     * @param parallel if should download in parallel
     * @return a map with the URLs and pages
     */
    public static Map<URL, Document> loadFrom(List<URL> urls, boolean parallel) {
        return (parallel ? urls.stream().sequential() : urls.stream().parallel())
                .map(url -> {
                    try {
                        return new Pair<>(url, Jsoup.parse(url.openStream(), "UTF-8", ""));
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    /**
     * Saves the received page list in the parent path, where the names are baseName i (i is the page index).
     *
     * @param pages    the pages to save
     * @param parent   the parent path
     * @param baseName the pages base name
     */
    public static void saveInto(List<Document> pages, Path parent, String baseName) {
        IntStream.range(0, pages.size())
                .forEach(index -> {
                            try {
                                Files.newBufferedWriter(Paths.get(parent.toString(), baseName + " " + index + ".html"))
                                        .append(pages.get(index).html())
                                        .close();
                            } catch (IOException ignored) {
                            }
                        }
                );
    }

    //

    /**
     * Returns a list with pairs of maps, each pair represents a page, the first map in the pair is the words
     * frequency and the second is the stemmed words frequency.
     *
     * @param pages the pages to process
     * @return a list of pairs of maps
     * @see #collectDocumentData(Document)
     */
    public static Map<URL, Pair<Map<String, Integer>, Map<String, Integer>>> collectDocumentsData(Map<URL, Document> pages) {
        return pages.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> collectDocumentData(entry.getValue())));
    }

    /**
     * Returns a pair of maps, the first map is the words frequency and the second is the stemmed words frequency.
     *
     * @param page the page to process
     * @return a pair of maps with words (normal in first and stemmed in second) frequency
     * @see #collectDocumentsData(Map)
     */
    public static Pair<Map<String, Integer>, Map<String, Integer>> collectDocumentData(Document page) {
        return new Pair<>(collectDocumentWordsFrequency(page, false), collectDocumentWordsFrequency(page, true));
    }

    /**
     * Returns the page words with their frequency.
     *
     * @param page the page to process
     * @param stem if should stem the words
     * @return a map with the words and frequency
     */
    public static Map<String, Integer> collectDocumentWordsFrequency(Document page, boolean stem) {
        Stemmer stemmer = new LovinsStemmer();
        return Stream.of(page.text().split(" "))
                .map(word -> word.replaceAll("[^A-Za-z]", ""))
                .map(stem ? stemmer::stem : Function.identity())
                .filter(word -> word.length() > 0)
                .collect(Collectors.toMap(Function.identity(), word -> 1, Integer::sum));
    }
}
