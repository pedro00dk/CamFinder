package classifier;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import weka.core.stemmers.LovinsStemmer;
import weka.core.stemmers.Stemmer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Download the pages to be used in the classifier.
 *
 * @author Pedro Henrique
 */
public class PageCollector {

    /**
     * Path to the negative page links.
     */
    public static final Path negativePageLinksPath = Paths.get("pages", "negative.txt");

    /**
     * Path to the positive page links.
     */
    public static final Path positivePageLinksPath = Paths.get("pages", "positive.txt");

    /**
     * Path to save the downloaded negative pages.
     */
    public static final Path negativeDownloadedPagesPath = Paths.get("pages", "downloaded", "negative");

    /**
     * Path to save the downloaded positive pages.
     */
    public static final Path positiveDownloadedPagesPath = Paths.get("pages", "downloaded", "positive");

    /**
     * Path to save the stemmed negative pages.
     */
    public static final Path negativeStemmedPagesPath = Paths.get("pages", "stemmed", "negative");

    /**
     * Path to save the stemmed positive pages.
     */
    public static final Path positiveStemmedPagesPath = Paths.get("pages", "stemmed", "positive");

    /**
     * Download the positive and negative pages.
     *
     * @param args ignored
     * @throws IOException if fails to read the links file or the directory path does not exist
     */
    public static void main(String[] args) throws IOException {

        System.out.println("Downloading negative instances");
        downloadPages(
                Files.lines(negativePageLinksPath)
                        .filter(link -> !link.startsWith("#") && link.trim().length() != 0)
                        .collect(Collectors.toList()),
                negativeDownloadedPagesPath,
                negativeStemmedPagesPath,
                "Page "
        );

        System.out.println("Downloading positive instances");
        downloadPages(
                Files.lines(positivePageLinksPath)
                        .filter(link -> !link.startsWith("#") && link.trim().length() != 0)
                        .collect(Collectors.toList()),
                positiveDownloadedPagesPath,
                positiveStemmedPagesPath,
                "Page "
        );
    }

    /**
     * Download the received pages and saves the html in the received directory.
     *
     * @param links             the links to download
     * @param downloadDirectory the destination directory
     * @param stemDirectory     the directory of the stemmed content
     * @param prefix            the file name prefix
     */
    private static void downloadPages(List<String> links, Path downloadDirectory, Path stemDirectory, String prefix) {
        AtomicInteger downloadedPages = new AtomicInteger(0);
        links.stream().parallel()
                .forEach(link -> {
                            int currentPage = downloadedPages.getAndIncrement();
                            System.out.println("Downloading link (" + currentPage + "): " + link);
                            try {
                                Document document = Jsoup.connect(link).get();
                                Files.newBufferedWriter(Paths.get(downloadDirectory.toString(), prefix + currentPage + ".html"))
                                        .append(document.html())
                                        .close();
                                System.out.println("Stemming link (" + currentPage + "): " + link);
                                Files.newBufferedWriter(Paths.get(stemDirectory.toString(), prefix + currentPage + ".stem.txt"))
                                        .append(stemAndCount(document.text().split(" ")).entrySet().stream()
                                                .sorted(Comparator.comparingLong(Map.Entry::getValue))
                                                .map(entry -> entry.getKey() + " " + entry.getValue())
                                                .collect(Collectors.joining("\n")))
                                        .close();
                                System.out.println("Link (" + currentPage + ") Success");
                            } catch (IOException ignored) {
                                System.out.println("Link (" + currentPage + ") Fail");
                            }
                        }
                );
    }


    /**
     * Stem the received words and count then.
     *
     * @param words the words to stem and count
     * @return a map of words the their frequency
     */
    public static Map<String, Long> stemAndCount(String[] words) {
        Stemmer stemmer = new LovinsStemmer();
        return Stream.of(words)
                .map(stemmer::stem)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
