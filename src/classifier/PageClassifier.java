package classifier;

import org.jsoup.nodes.Document;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The page classifier, saves multiples classifiers of different types that can be used to classify instances.
 *
 * @author Pedro Henrique
 */
public class PageClassifier {

    /**
     * The classifiers to train.
     */
    private List<Classifier> classifiers;

    /**
     * Indicates if the machines was trained.
     */
    private boolean trained;

    /**
     * The built attribute list based on the received pages
     */
    private List<Attribute> attributes;

    /**
     * All instances created using the attributes and pages
     */
    private Instances instances;

    /**
     * Selected training instances from the instances.
     */
    private Instances trainInstances;

    /**
     * Selected test instances from the instances.
     */
    private Instances testInstances;

    /**
     * The string representing the negative class.
     */
    public static final String NEGATIVE = "n";

    /**
     * The string representing the positive class.
     */
    public static final String POSITIVE = "p";

    /**
     * Initializes the page classifier with the received parameters.
     *
     * @param classifiers   the classifiers to train
     * @param negativePages the negative pages
     * @param positivePages the positive pages
     * @param trainRatio    the number of instances to the train data set ratio
     */
    public PageClassifier(List<Classifier> classifiers, List<Document> negativePages, List<Document> positivePages, float trainRatio) {

        this.classifiers = classifiers;
        trained = false;

        List<Map<String, Integer>> negativePagesStem = negativePages.stream()
                .map(page -> PageUtils.collectDocumentWordsFrequency(page, true))
                .collect(Collectors.toList());
        List<Map<String, Integer>> positivePagesStem = positivePages.stream()
                .map(page -> PageUtils.collectDocumentWordsFrequency(page, true))
                .collect(Collectors.toList());

        attributes = collectAttributes(Stream.concat(negativePagesStem.stream(), positivePagesStem.stream()).collect(Collectors.toList()));
        instances = new Instances("Instances", (ArrayList<Attribute>) attributes, negativePages.size() + positivePages.size());
        instances.setClassIndex(instances.numAttributes() - 1);

        negativePagesStem.stream()
                .map(stem -> buildInstanceFromStem(stem, NEGATIVE))
                .forEach(instances::add);
        positivePagesStem.stream()
                .map(stem -> buildInstanceFromStem(stem, POSITIVE))
                .forEach(instances::add);


        int trainingSize = Math.round(instances.size() * trainRatio);
        int testSize = instances.size() - trainingSize;
        instances.randomize(new Random());
        trainInstances = new Instances(instances, 0, trainingSize);
        testInstances = new Instances(instances, trainingSize, testSize);
    }

    /**
     * Collect the attributes from the stemmed pages.
     *
     * @param pagesStem the stemmed pages to collect attributes
     * @return a list of attributes ordered (descending) by their frequency
     */
    private List<Attribute> collectAttributes(List<Map<String, Integer>> pagesStem) {

        // Distinguishing and ordering words by frequency
        List<Attribute> attributes = pagesStem.stream()
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum)).entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, (v1, v2) -> v2 - v1))
                .map(Map.Entry::getKey)
                .map(Attribute::new)
                .collect(Collectors.toList());

        // Adding class attribute
        Attribute classAttribute = new Attribute("--CLASS--", Stream.of(PageClassifier.POSITIVE, PageClassifier.NEGATIVE).collect(Collectors.toList()));
        attributes.add(classAttribute);

        return attributes;
    }

    /**
     * Build an instance from the received page.
     *
     * @param page  the page the create the instance
     * @param clazz the page class (null if has no class,  used to classify, not test)
     * @return the instance representing the page
     */
    private Instance buildInstanceFromPage(Document page, String clazz) {
        return buildInstanceFromStem(PageUtils.collectDocumentWordsFrequency(page, true), clazz);
    }

    /**
     * Build an instance from the received page.
     *
     * @param pageStem the page stem the create the instance
     * @param clazz    the page class (null if has no class,  used to classify, not test)
     * @return the instance representing the page
     */
    private Instance buildInstanceFromStem(Map<String, Integer> pageStem, String clazz) {
        Instance instance = new DenseInstance(
                1,
                attributes.stream()
                        .mapToDouble(attribute -> pageStem.getOrDefault(attribute.name(), 0))
                        .toArray()
        );
        instance.setValue(instances.classAttribute(), clazz);
        return instance;
    }

    /**
     * Train all classifiers.
     *
     * @param parallel if should train in parallel
     */
    public void trainClassifiers(boolean parallel) {
        (parallel ? classifiers.stream().sequential() : classifiers.stream().parallel())
                .forEach(classifier -> {
                    try {
                        classifier.buildClassifier(trainInstances);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        trained = true;
    }

    /**
     * Print the summary of all trained machines. If the classifiers was not trained throws a
     * {@link IllegalStateException}.
     */
    public void printSummary() {
        if (!trained) {
            throw new IllegalStateException("The machines was not trained.");
        }
        classifiers.stream()
                .forEach(classifier -> {
                    try {
                        Evaluation evaluation = new Evaluation(trainInstances);
                        evaluation.evaluateModel(classifier, testInstances);
                        System.out.println(evaluation.toSummaryString(classifier.getClass().getSimpleName(), true));
                        System.out.println();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
