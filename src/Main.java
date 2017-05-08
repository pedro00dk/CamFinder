import classifier.PageAttributeExtractor;
import classifier.PageDocumentLoader;
import javafx.util.Pair;
import org.jsoup.nodes.Document;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static util.LoggingUtils.global;

public class Main {

    static final boolean pagesDownloadedAndSaved = true;
    static final Path negativePageLinksPath = Paths.get("pages", "negative.txt");
    static final Path positivePageLinksPath = Paths.get("pages", "positive.txt");
    static final Path negativeDownloadedPagesPath = Paths.get("pages", "downloaded", "negative");
    static final Path positiveDownloadedPagesPath = Paths.get("pages", "downloaded", "positive");

    public static void main(String[] args) throws Exception {

        //
        global().finer("Starting");
        List<Document> negativeDocuments;
        List<Document> positiveDocuments;
        if (pagesDownloadedAndSaved) {

            //
            global().finer("Loading local pages");
            negativeDocuments = new ArrayList<>(
                    PageDocumentLoader.fromPaths(
                            StreamSupport.stream(Files.newDirectoryStream(negativeDownloadedPagesPath).spliterator(), false)
                                    .collect(Collectors.toList()), false
                    ).values()
            );
            positiveDocuments = new ArrayList<>(
                    PageDocumentLoader.fromPaths(
                            StreamSupport.stream(Files.newDirectoryStream(positiveDownloadedPagesPath).spliterator(), false)
                                    .collect(Collectors.toList()), false
                    ).values()
            );
        } else {

            //
            global().finer("Downloading pages");
            List<URL> negativeLinks = Files.lines(negativePageLinksPath)
                    .filter(line -> line.length() != 0 && !line.startsWith("#"))
                    .map(link -> {
                        try {
                            return new URL(link);
                        } catch (MalformedURLException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            List<URL> positiveLinks = Files.lines(positivePageLinksPath)
                    .filter(line -> line.length() != 0 && !line.startsWith("#"))
                    .map(link -> {
                        try {
                            return new URL(link);
                        } catch (MalformedURLException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            negativeDocuments = new ArrayList<>(
                    PageDocumentLoader.fromLinks(negativeLinks, true).values()
            );
            positiveDocuments = new ArrayList<>(
                    PageDocumentLoader.fromLinks(positiveLinks, true).values()
            );

            //
            global().log(Level.INFO, "Saving downloaded pages locally");
            savePages(negativeDocuments, negativeDownloadedPagesPath, "Page");
            savePages(positiveDocuments, positiveDownloadedPagesPath, "Page");
        }

        //
        global().finer("Number of negative instances: " + negativeDocuments.size());
        global().finer("Number of positive instances: " + positiveDocuments.size());

        //
        global().finer("Stemming documents");
        List<Pair<Map<String, Integer>, Map<String, Integer>>> negativeStemmedDocumentContents
                = PageAttributeExtractor.collectDocumentsData(negativeDocuments);
        List<Pair<Map<String, Integer>, Map<String, Integer>>> positiveStemmedDocumentContents
                = PageAttributeExtractor.collectDocumentsData(positiveDocuments);

        //
        global().finer("Creating attributes list");
        List<Attribute> attributes = Stream.concat(negativeStemmedDocumentContents.stream(), positiveStemmedDocumentContents.stream())
                .map(Pair::getValue)
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .distinct()
                .map(Attribute::new)
                .collect(Collectors.toList());

        //
        global().finer("Adding class attribute to list");
        List<String> classAttributeValues = new ArrayList<>();
        classAttributeValues.add("negative");
        classAttributeValues.add("positive");
        Attribute classAttribute = new Attribute("--Class Attribute--", classAttributeValues);
        attributes.add(classAttribute);

        //
        global().finer("Attributes list size: " + attributes.size());

        //
        global().finer("Building weka data set");
        Instances instances = new Instances("Camera Pages", (ArrayList<Attribute>) attributes, 0);
        instances.setClassIndex(attributes.size() - 1);

        //
        global().finer("Generating instances for each document");
        negativeStemmedDocumentContents.stream()
                .map(Pair::getValue)
                .map(pageStemmedContent -> {
                    Instance pageInstance = new SparseInstance(attributes.size());
                    attributes.stream()
                            .limit(attributes.size() - 1) // Removing class attribute
                            .filter(attribute -> pageStemmedContent.containsKey(attribute.name())) // Sparse instances sets 0 in not specified attributes
                            .forEach(attribute -> pageInstance.setValue(attribute.index(), pageStemmedContent.get(attribute.name())));
                    pageInstance.setValue(attributes.get(attributes.size() - 1), "negative");
                    return pageInstance;
                })
                .forEach(instances::add);
        positiveStemmedDocumentContents.stream()
                .map(Pair::getValue)
                .map(pageStemmedContent -> {
                    Instance pageInstance = new SparseInstance(attributes.size());
                    attributes.stream()
                            .limit(attributes.size() - 1) // Removing class attribute
                            .filter(attribute -> pageStemmedContent.containsKey(attribute.name())) // Sparse instances sets 0 in not specified attributes
                            .forEach(attribute -> pageInstance.setValue(attribute.index(), pageStemmedContent.get(attribute.name())));
                    pageInstance.setValue(attributes.get(attributes.size() - 1), "positive");
                    return pageInstance;
                })
                .forEach(instances::add);

        //
        global().finer("Data set size: " + instances.size());

        float trainingInstancesRatio = 2 / 3f;
        int trainingSize = Math.round(instances.size() * trainingInstancesRatio);
        int testSize = instances.size() - trainingSize;

        //
        global().finer("Splitting data set in training and test data sets using training ratio of " + trainingInstancesRatio);
        instances.randomize(new Random());
        Instances trainingInstances = new Instances(instances, 0, trainingSize);
        Instances testInstances = new Instances(instances, trainingSize, testSize);

        //
        global().finer("Training data set size: " + trainingInstances.size());
        global().finer("Test data set size: " + testInstances.size());

        //
        global().finer("Attribute selection not implemented yet.");

        //
        global().finer("Training and evaluating classifiers");

        Classifier[] classifiers = new Classifier[]{
                new IBk(1),
                new NaiveBayes(),
                //new MultilayerPerceptron(), // Really slow without attribute selection
                new RandomForest()
        };

        Stream.of(classifiers)
                .forEach(classifier -> {
                    System.out.println(classifier.getClass().getSimpleName());
                    try {
                        classifier.buildClassifier(trainingInstances);
                        Evaluation evaluation = new Evaluation(trainingInstances);
                        evaluation.evaluateModel(classifier, testInstances);
                        System.out.println(evaluation.toSummaryString());
                        System.out.println();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    static void savePages(List<Document> pages, Path parent, String baseName) {
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
}
