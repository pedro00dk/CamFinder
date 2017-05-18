package classifier;

import javafx.util.Pair;
import org.jsoup.nodes.Document;
import util.SerializationUtils;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
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
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Main {

    private static final boolean LOCALLY;

    private static final Path NEGATIVE_PAGES_PATH = Paths.get("pages", "downloaded", "negative");
    private static final Path NEGATIVE_PAGE_LINKS = Paths.get("pages", "negative.txt");

    private static final Path POSITIVE_PAGES_PATH = Paths.get("pages", "downloaded", "positive");
    private static final Path POSITIVE_PAGE_LINKS = Paths.get("pages", "positive.txt");

    private static final Path SERIALIZED_CLASSIFIERS = Paths.get("classifiers", "serialized.model");

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

        //-- Load pages content

        // Loading the Page URLs (local or web)
        System.out.println("Loading negative URLs");
        List<URL> negativeUrls = loadUrls(LOCALLY,
                Files.list(NEGATIVE_PAGES_PATH)
                        .collect(Collectors.toList()),
                Files.lines(NEGATIVE_PAGE_LINKS)
                        .filter(line -> line.length() != 0 && !line.startsWith("#"))
                        .collect(Collectors.toList())
        );
        System.out.println("Loading positive URLs");
        List<URL> positiveUrls = loadUrls(LOCALLY,
                StreamSupport.stream(Files.newDirectoryStream(POSITIVE_PAGES_PATH).spliterator(), false)
                        .collect(Collectors.toList()),
                Files.lines(POSITIVE_PAGE_LINKS)
                        .filter(line -> line.length() != 0 && !line.startsWith("#"))
                        .collect(Collectors.toList())
        );

        // Loading pages
        System.out.println("Loading negative pages");
        Map<URL, Document> negativePages = PageUtils.loadFrom(negativeUrls, true, true);
        System.out.println("Loading positive pages");
        Map<URL, Document> positivePages = PageUtils.loadFrom(positiveUrls, true, true);

        // Saving pages locally if they are in the web
        if (!LOCALLY) {
            System.out.println("Saving pages locally");
            PageUtils.saveInto(new ArrayList<>(negativePages.values()), NEGATIVE_PAGES_PATH, "Page");
            PageUtils.saveInto(new ArrayList<>(positivePages.values()), POSITIVE_PAGES_PATH, "Page");
        }

        //-- Create internal classifiers and filters
        System.out.println("Creating internal classifiers and filters");
        // List without some classifiers
        List<Classifier> simpleInternalClassifiers = Stream.of(

                // Base classifiers
                new NaiveBayes(),
                new J48(),
                new SMO(),
                null, //new Logistic(), // Uses so much memory (OutOfMemoryError: Java heap space)
                null, //new MultilayerPerceptron(), // Really slow due the number of instances

                // Extra classifiers
                new IBk(3),
                new RandomForest()

        ).collect(Collectors.toList());

        // List with all classifiers
        List<Classifier> internalClassifiers = Stream.of(

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

        // Attribute selection info gain filter
        AttributeSelection filter = new AttributeSelection();
        InfoGainAttributeEval infoGainEval = new InfoGainAttributeEval();
        Ranker infoGainSearch = new Ranker();
        filter.setEvaluator(infoGainEval);
        filter.setSearch(infoGainSearch);

        // Create page classifiers to test
        System.out.println("Creating page classifiers");
        List<PageClassifier> pageClassifiers = new ArrayList<>();

        pageClassifiers.add(
                new PageClassifier("No Filters",
                        simpleInternalClassifiers,
                        0,
                        null,
                        Integer.MAX_VALUE,
                        new ArrayList<>(negativePages.values()),
                        new ArrayList<>(positivePages.values()),
                        0.75f
                        , 1137
                )
        );
        System.out.println(pageClassifiers.get(pageClassifiers.size() - 1).getLabel() + " created");

        pageClassifiers.add(
                new PageClassifier("10 High freq",
                        internalClassifiers,
                        0,
                        null,
                        10,
                        new ArrayList<>(negativePages.values()),
                        new ArrayList<>(positivePages.values()),
                        0.75f
                        , 1137
                )
        );
        System.out.println(pageClassifiers.get(pageClassifiers.size() - 1).getLabel() + " created");

        pageClassifiers.add(
                new PageClassifier("50 High freq",
                        internalClassifiers,
                        0,
                        null,
                        50,
                        new ArrayList<>(negativePages.values()),
                        new ArrayList<>(positivePages.values()),
                        0.75f
                        , 1137
                )
        );
        System.out.println(pageClassifiers.get(pageClassifiers.size() - 1).getLabel() + " created");

        pageClassifiers.add(
                new PageClassifier("100 High freq",
                        internalClassifiers,
                        0,
                        null,
                        100,
                        new ArrayList<>(negativePages.values()),
                        new ArrayList<>(positivePages.values()),
                        0.75f
                        , 1137
                )
        );
        System.out.println(pageClassifiers.get(pageClassifiers.size() - 1).getLabel() + " created");

        infoGainSearch.setNumToSelect(10);
        pageClassifiers.add(
                new PageClassifier("10 Info gain",
                        internalClassifiers,
                        0,
                        filter,
                        Integer.MAX_VALUE,
                        new ArrayList<>(negativePages.values()),
                        new ArrayList<>(positivePages.values()),
                        0.75f
                        , 1137
                )
        );
        System.out.println(pageClassifiers.get(pageClassifiers.size() - 1).getLabel() + " created");

        infoGainSearch.setNumToSelect(50);
        pageClassifiers.add(
                new PageClassifier("50 Info gain",
                        internalClassifiers,
                        0,
                        filter,
                        Integer.MAX_VALUE,
                        new ArrayList<>(negativePages.values()),
                        new ArrayList<>(positivePages.values()),
                        0.75f
                        , 1137
                )
        );
        System.out.println(pageClassifiers.get(pageClassifiers.size() - 1).getLabel() + " created");

        infoGainSearch.setNumToSelect(100);
        pageClassifiers.add(
                new PageClassifier("100 Info gain",
                        internalClassifiers,
                        0,
                        filter,
                        Integer.MAX_VALUE,
                        new ArrayList<>(negativePages.values()),
                        new ArrayList<>(positivePages.values()),
                        0.75f
                        , 1137
                )
        );
        System.out.println(pageClassifiers.get(pageClassifiers.size() - 1).getLabel() + " created");

        System.out.println("Evaluating page classifiers");
        pageClassifiers.forEach(
                pageClassifier -> {
                    System.out.println(pageClassifier.getLabel());
                    IntStream.range(0, pageClassifier.classifierCount())
                            .forEach(index -> {
                                try {
                                    Classifier classifier = pageClassifier.getClassifier(index);
                                    if (classifier == null) {
                                        System.out.println("Classifier " + index + " -> null");
                                        System.out.println();
                                        return;
                                    }
                                    System.out.println("Classifier " + index + " -> " + classifier.getClass().getSimpleName());
                                    System.out.println(pageClassifier.getClassifierEvaluation(index).toSummaryString());
                                    System.out.println();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                }
        );

        // Get best classifiers
        Pair<PageClassifier, Integer> bestPageClassifierByRecall = PageClassifier.select(
                pageClassifiers,
                evaluation -> evaluation.recall(PageClassifier.CLASSES.indexOf(PageClassifier.POSITIVE))
        );
        System.out.println("Best page classifier (recall positive): " + bestPageClassifierByRecall.getKey().getLabel() +
                "\n\t" + "Best internal classifier index: " + bestPageClassifierByRecall.getValue()
        );

        Pair<PageClassifier, Integer> bestPageClassifierByCorrectlyClassified = PageClassifier.select(
                pageClassifiers,
                Evaluation::pctCorrect
        );
        System.out.println("Best page classifier (% correct): " + bestPageClassifierByCorrectlyClassified.getKey().getLabel() +
                "\n\t" + "Best internal classifier index: " + bestPageClassifierByCorrectlyClassified.getValue()
        );

        // Print statistics
        int positiveClassIndex = PageClassifier.CLASSES.indexOf(PageClassifier.POSITIVE);
        pageClassifiers.forEach(pageClassifier -> {
            System.out.println(pageClassifier.getLabel() + " statistics");
            IntStream.range(0, pageClassifier.classifierCount())
                    .forEach(index -> {
                        try {
                            Classifier classifier = pageClassifier.getClassifier(index);
                            if (classifier == null) {
                                System.out.println("Null classifier");
                                return;
                            }
                            System.out.println("\t" + classifier.getClass().getSimpleName());
                            Evaluation evaluation = pageClassifier.getClassifierEvaluation(index);
                            System.out.println("\t\t(Positive) Precision: " + evaluation.precision(positiveClassIndex));
                            System.out.println("\t\t(Positive) Recall   : " + evaluation.recall(positiveClassIndex));
                            System.out.println("\t\t(Positive) F-Measure: " + evaluation.fMeasure(positiveClassIndex));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        });

        SerializationUtils.serialize((ArrayList<PageClassifier>) pageClassifiers, SERIALIZED_CLASSIFIERS);
        System.out.println("Serializing classifiers");
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
