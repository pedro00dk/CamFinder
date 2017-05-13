package classifier;

import org.jsoup.nodes.Document;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.filters.supervised.attribute.AttributeSelection;

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

        // Loading the Page URLs (local or web)
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

        // Loading pages
        Map<URL, Document> negativePages = PageUtils.loadFrom(negativeUrls, true);
        Map<URL, Document> positivePages = PageUtils.loadFrom(positiveUrls, true);

        // Saving pages locally if they are in the web
        if (!LOCALLY) {
            PageUtils.saveInto(new ArrayList<>(negativePages.values()), NEGATIVE_PAGES_PATH, "Page");
            PageUtils.saveInto(new ArrayList<>(positivePages.values()), POSITIVE_PAGES_PATH, "Page");
        }

        // Training classifiers

        // Simple classifier, has no attribute filter
        System.out.println("Simple classifier - no filters");
        List<Classifier> simplePageClassifierInternalClassifiers = Stream.of(

                // Base classifiers
                new NaiveBayes(),
                new J48(),
                new SMO(),
                //new Logistic(), // Uses so much memory (OutOfMemoryError: Java heap space)
                //new MultilayerPerceptron(), // Really slow due the number of instances

                // Extra classifiers
                new IBk(3),
                new RandomForest()
        ).collect(Collectors.toList());
        PageClassifier simplePageClassifier = new PageClassifier(
                simplePageClassifierInternalClassifiers,
                null,
                Integer.MAX_VALUE,
                new ArrayList<>(negativePages.values()),
                new ArrayList<>(positivePages.values()),
                0.75f
        );

        System.out.println("Simple classifier with attribute count limit set to 10");
        List<Classifier> simpleAttributeLimited10PageClassifierInternalClassifiers = Stream.of(

                // Base classifiers
                new NaiveBayes(),
                new J48(),
                new SMO(),
                new Logistic(),
                new MultilayerPerceptron(),

                // Extra classifiers
                new IBk(3),
                new RandomForest()
        ).collect(Collectors.toList());
        PageClassifier simpleAttributeLimited10PageClassifier = new PageClassifier(
                simplePageClassifierInternalClassifiers,
                null,
                Integer.MAX_VALUE,
                new ArrayList<>(negativePages.values()),
                new ArrayList<>(positivePages.values()),
                0.75f
        );

        System.out.println("Simple classifier with attribute count limit set to 50");
        List<Classifier> simpleAttributeLimited50PageClassifierInternalClassifiers = Stream.of(

                // Base classifiers
                new NaiveBayes(),
                new J48(),
                new SMO(),
                new Logistic(),
                new MultilayerPerceptron(),

                // Extra classifiers
                new IBk(3),
                new RandomForest()
        ).collect(Collectors.toList());
        PageClassifier simpleAttributeLimited50PageClassifier = new PageClassifier(
                simplePageClassifierInternalClassifiers,
                null,
                Integer.MAX_VALUE,
                new ArrayList<>(negativePages.values()),
                new ArrayList<>(positivePages.values()),
                0.75f
        );

        System.out.println("Simple classifier with attribute count limit set to 100");
        List<Classifier> simpleAttributeLimited100PageClassifierInternalClassifiers = Stream.of(

                // Base classifiers
                new NaiveBayes(),
                new J48(),
                new SMO(),
                new Logistic(),
                new MultilayerPerceptron(),

                // Extra classifiers
                new IBk(3),
                new RandomForest()
        ).collect(Collectors.toList());
        PageClassifier simpleAttributeLimited100PageClassifier = new PageClassifier(
                simplePageClassifierInternalClassifiers,
                null,
                Integer.MAX_VALUE,
                new ArrayList<>(negativePages.values()),
                new ArrayList<>(positivePages.values()),
                0.75f
        );

        System.out.println("Simple classifier with attribute count limit set to 200");
        List<Classifier> simpleAttributeLimited200PageClassifierInternalClassifiers = Stream.of(

                // Base classifiers
                new NaiveBayes(),
                new J48(),
                new SMO(),
                new Logistic(),
                new MultilayerPerceptron(),

                // Extra classifiers
                new IBk(3),
                new RandomForest()
        ).collect(Collectors.toList());
        PageClassifier simpleAttributeLimited200PageClassifier = new PageClassifier(
                simplePageClassifierInternalClassifiers,
                null,
                Integer.MAX_VALUE,
                new ArrayList<>(negativePages.values()),
                new ArrayList<>(positivePages.values()),
                0.75f
        );

        // Classifier with info gain filter (filtering 10 best attributes)
        System.out.println("InfoGain 10 classifier");
        List<Classifier> infoGainRanker10PageClassifierInternalClassifiers = Stream.of(

                // Base classifiers
                new NaiveBayes(),
                new J48(),
                new SMO(),
                new Logistic(),
                new MultilayerPerceptron(),

                // Extra classifiers
                new IBk(3),
                new RandomForest()
        ).collect(Collectors.toList());
        AttributeSelection infoGainRanker10PageClassifierInternalClassifiersFilter = new AttributeSelection(); // It's a filter
        InfoGainAttributeEval infoGainRanker10PageClassifierInternalClassifiersFilterEval = new InfoGainAttributeEval();
        Ranker infoGainRanker10PageClassifierInternalClassifiersFilterRanker = new Ranker();
        infoGainRanker10PageClassifierInternalClassifiersFilterRanker.setNumToSelect(10);
        infoGainRanker10PageClassifierInternalClassifiersFilter.setEvaluator(infoGainRanker10PageClassifierInternalClassifiersFilterEval);
        infoGainRanker10PageClassifierInternalClassifiersFilter.setSearch(infoGainRanker10PageClassifierInternalClassifiersFilterRanker);
        PageClassifier infoGainRanker10PageClassifier = new PageClassifier(
                infoGainRanker10PageClassifierInternalClassifiers,
                infoGainRanker10PageClassifierInternalClassifiersFilter,
                Integer.MAX_VALUE,
                new ArrayList<>(negativePages.values()),
                new ArrayList<>(positivePages.values()),
                0.75f
        );

        // Classifier with info gain filter (filtering 50 best attributes)
        System.out.println("InfoGain 50 classifier");
        List<Classifier> infoGainRanker50PageClassifierInternalClassifiers = Stream.of(

                // Base classifiers
                new NaiveBayes(),
                new J48(),
                new SMO(),
                new Logistic(),
                new MultilayerPerceptron(),

                // Extra classifiers
                new IBk(3),
                new RandomForest()
        ).collect(Collectors.toList());
        AttributeSelection infoGainRanker50PageClassifierInternalClassifiersFilter = new AttributeSelection(); // It's a filter
        InfoGainAttributeEval infoGainRanker50PageClassifierInternalClassifiersFilterEval = new InfoGainAttributeEval();
        Ranker infoGainRanker50PageClassifierInternalClassifiersFilterRanker = new Ranker();
        infoGainRanker50PageClassifierInternalClassifiersFilterRanker.setNumToSelect(50);
        infoGainRanker50PageClassifierInternalClassifiersFilter.setEvaluator(infoGainRanker50PageClassifierInternalClassifiersFilterEval);
        infoGainRanker50PageClassifierInternalClassifiersFilter.setSearch(infoGainRanker50PageClassifierInternalClassifiersFilterRanker);
        PageClassifier infoGainRanker50PageClassifier = new PageClassifier(
                infoGainRanker50PageClassifierInternalClassifiers,
                infoGainRanker50PageClassifierInternalClassifiersFilter,
                Integer.MAX_VALUE,
                new ArrayList<>(negativePages.values()),
                new ArrayList<>(positivePages.values()),
                0.75f
        );

        // Classifier with info gain filter (filtering 100 best attributes)
        System.out.println("InfoGain 100 classifier");
        List<Classifier> infoGainRanker100PageClassifierInternalClassifiers = Stream.of(

                // Base classifiers
                new NaiveBayes(),
                new J48(),
                new SMO(),
                new Logistic(),
                new MultilayerPerceptron(),

                // Extra classifiers
                new IBk(3),
                new RandomForest()
        ).collect(Collectors.toList());
        AttributeSelection infoGainRanker100PageClassifierInternalClassifiersFilter = new AttributeSelection(); // It's a filter
        InfoGainAttributeEval infoGainRanker100PageClassifierInternalClassifiersFilterEval = new InfoGainAttributeEval();
        Ranker infoGainRanker100PageClassifierInternalClassifiersFilterRanker = new Ranker();
        infoGainRanker100PageClassifierInternalClassifiersFilterRanker.setNumToSelect(100);
        infoGainRanker100PageClassifierInternalClassifiersFilter.setEvaluator(infoGainRanker100PageClassifierInternalClassifiersFilterEval);
        infoGainRanker100PageClassifierInternalClassifiersFilter.setSearch(infoGainRanker100PageClassifierInternalClassifiersFilterRanker);
        PageClassifier infoGainRanker100PageClassifier = new PageClassifier(
                infoGainRanker100PageClassifierInternalClassifiers,
                infoGainRanker100PageClassifierInternalClassifiersFilter,
                Integer.MAX_VALUE,
                new ArrayList<>(negativePages.values()),
                new ArrayList<>(positivePages.values()),
                0.75f
        );

        // Classifier with info gain filter (filtering 50 best attributes)
        System.out.println("InfoGain 200 classifier");
        List<Classifier> infoGainRanker200PageClassifierInternalClassifiers = Stream.of(

                // Base classifiers
                new NaiveBayes(),
                new J48(),
                new SMO(),
                new Logistic(),
                new MultilayerPerceptron(),

                // Extra classifiers
                new IBk(3),
                new RandomForest()
        ).collect(Collectors.toList());
        AttributeSelection infoGainRanker200PageClassifierInternalClassifiersFilter = new AttributeSelection(); // It's a filter
        InfoGainAttributeEval infoGainRanker200PageClassifierInternalClassifiersFilterEval = new InfoGainAttributeEval();
        Ranker infoGainRanker200PageClassifierInternalClassifiersFilterRanker = new Ranker();
        infoGainRanker200PageClassifierInternalClassifiersFilterRanker.setNumToSelect(200);
        infoGainRanker200PageClassifierInternalClassifiersFilter.setEvaluator(infoGainRanker200PageClassifierInternalClassifiersFilterEval);
        infoGainRanker200PageClassifierInternalClassifiersFilter.setSearch(infoGainRanker200PageClassifierInternalClassifiersFilterRanker);
        PageClassifier infoGainRanker200PageClassifier = new PageClassifier(
                infoGainRanker200PageClassifierInternalClassifiers,
                infoGainRanker200PageClassifierInternalClassifiersFilter,
                Integer.MAX_VALUE,
                new ArrayList<>(negativePages.values()),
                new ArrayList<>(positivePages.values()),
                0.75f
        );

//        Path pageClassifierPath = Paths.get("src", "classifier", "serialized", "PageClassifier.model");
//        SerializationUtils.serialize(pageClassifier, pageClassifierPath);
//        pageClassifier = SerializationUtils.deserialize(pageClassifierPath);
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
