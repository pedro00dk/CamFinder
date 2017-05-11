package classifier;

import org.jsoup.nodes.Document;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.RandomForest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Main {

    private static final boolean LOCALLY;

    private static final Path NEGATIVE_PAGES_PATH = Paths.get("pages", "downloaded", "negative");
    private static final Path NEGATIVE_PAGE_LINKS = Paths.get("pages", "negative.txt");

    private static final Path POSITIVE_PAGES_PATH = Paths.get("pages", "downloaded", "positive");
    private static final Path POSITIVE_PAGE_LINKS = Paths.get("pages", "positive.txt");

    static {
        boolean locally;
        try {
            locally = Files.list(NEGATIVE_PAGES_PATH).findAny().isPresent() && Files.list(POSITIVE_PAGES_PATH).findAny().isPresent();
        } catch (IOException ignored) {
            locally = false;
        }
        LOCALLY = locally;
    }

    public static void main(String[] args) throws Exception {
        List<URL> negativeUrls = loadUrls(LOCALLY,
                Files.list(NEGATIVE_PAGES_PATH)
                        .collect(Collectors.toList()),
                Files.lines(NEGATIVE_PAGE_LINKS)
                        .filter(line -> line.length() != 0 && !line.startsWith("#"))
                        .collect(Collectors.toList())
        );
        List<URL> positiveUrls = loadUrls(LOCALLY,
                StreamSupport.stream(Files.newDirectoryStream(POSITIVE_PAGES_PATH).spliterator(), false)
                        .collect(Collectors.toList()),
                Files.lines(POSITIVE_PAGE_LINKS)
                        .filter(line -> line.length() != 0 && !line.startsWith("#"))
                        .collect(Collectors.toList())
        );

        Map<URL, Document> negativePages = PageUtils.loadFrom(negativeUrls, true);
        Map<URL, Document> positivePages = PageUtils.loadFrom(positiveUrls, true);

        if (!LOCALLY) {
            PageUtils.saveInto(new ArrayList<>(negativePages.values()), NEGATIVE_PAGES_PATH, "Page");
            PageUtils.saveInto(new ArrayList<>(positivePages.values()), POSITIVE_PAGES_PATH, "Page");
        }

        PageClassifier pageClassifier = new PageClassifier(Stream.of(new IBk(1), new NaiveBayes(), new RandomForest()).collect(Collectors.toList()), new ArrayList<>(negativePages.values()), new ArrayList<>(positivePages.values()), 0.65f);

        System.out.println("Checking negatives");
        for (Document negativePage : negativePages.values()) {
            System.out.println(pageClassifier.classifyPage(negativePage, 2)); // RandomForest
        }
        System.out.println();
        System.out.println("Checking positives");
        for (Document positivePage : positivePages.values()) {
            System.out.println(pageClassifier.classifyPage(positivePage, 2)); // RandomForest
        }
    }

    /**
     * Creates urls for the files LOCALLY or in the network.
     *
     * @param local if the files are accessible LOCALLY
     * @param paths the paths of the local files
     * @param links the links of the files in the network
     * @return a list of file URLs
     */
    public static List<URL> loadUrls(boolean local, List<Path> paths, List<String> links) {
        return (local
                ?
                paths.stream()
                        .map(path -> {
                            try {
                                return path.toUri().toURL();
                            } catch (MalformedURLException e) {
                                return null;
                            }
                        })
                :
                links.stream()
                        .map(link -> {
                            try {
                                return new URL(link);
                            } catch (MalformedURLException e) {
                                return null;
                            }
                        })
        )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
