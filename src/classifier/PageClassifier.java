package classifier;

import org.jsoup.nodes.Document;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The page classifier, saves multiples classifiers of different types that can be used to classify instances.
 *
 * @author Pedro Henrique
 */
public class PageClassifier implements Serializable {

    /**
     * The label  of this classifier.
     */
    private String label;

    /**
     * The classifiers to train.
     */
    private List<Classifier> classifiers;

    /**
     * The default classifier index.
     */
    private int defaultClassifierIndex;
    /**
     * The instances filter.
     */
    private Filter filter;

    /**
     * Limits the number of attributes based on they total frequency.
     */
    private int maxAttributeCount;

    /**
     * The built attribute list based on the received pages.
     */
    private List<Attribute> attributes;

    /**
     * All instances created using the attributes and pages.
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
     * The list of classes.
     */
    public static final List<String> CLASSES = Collections.unmodifiableList(Stream.of(NEGATIVE, POSITIVE).collect(Collectors.toList()));

    /**
     * Creates and trains the page classifier. The classifiers and filter are cloned, null classifiers are accepted.
     *
     * @param label                  the page classifier label
     * @param classifiers            the classifiers to test
     * @param defaultClassifierIndex the default classifier index
     * @param filter                 the filter to be used
     * @param maxAttributeCount      the max allowed attributes (filtered by frequency)
     * @param negativePages          the list of negative pages
     * @param positivePages          the list of positive pages
     * @param trainRatio             the ratio of instances to be used in training
     */
    public PageClassifier(String label, List<Classifier> classifiers, int defaultClassifierIndex, Filter filter, int maxAttributeCount, List<Document> negativePages, List<Document> positivePages, float trainRatio) {
        this.label = label != null ? label : getClass().getSimpleName();

        Objects.requireNonNull(classifiers, "The classifiers list can not be null.");
        this.classifiers = Collections.unmodifiableList(
                classifiers.stream()
                        .map(AbstractClassifier.class::cast)
                        .map(classifier -> {
                            if (classifier == null) {
                                return null;
                            }
                            try {
                                return AbstractClassifier.makeCopy(classifier);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .collect(Collectors.toList())
        );

        if (filter != null) {
            try {
                this.filter = Filter.makeCopy(filter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (maxAttributeCount <= 0) {
            throw new IllegalArgumentException("The maxAttributeCount should be greater than 0.");
        }
        this.maxAttributeCount = maxAttributeCount;

        // Stemming pages
        List<Map<String, Integer>> negativePagesStem = negativePages.stream()
                .map(page -> PageUtils.collectDocumentWordsFrequency(page, true))
                .collect(Collectors.toList());
        List<Map<String, Integer>> positivePagesStem = positivePages.stream()
                .map(page -> PageUtils.collectDocumentWordsFrequency(page, true))
                .collect(Collectors.toList());

        this.attributes = collectAttributes(Stream.concat(negativePagesStem.stream(), positivePagesStem.stream()).collect(Collectors.toList()), this.maxAttributeCount);
        attributes.add(new Attribute("--CLASS--", CLASSES)); // Adding class attribute

        instances = new Instances("Instances", (ArrayList<Attribute>) attributes, negativePages.size() + positivePages.size());
        instances.setClassIndex(instances.numAttributes() - 1);
        negativePagesStem.stream()
                .map(stem -> buildInstanceFromStem(stem, NEGATIVE))
                .forEach(instances::add);
        positivePagesStem.stream()
                .map(stem -> buildInstanceFromStem(stem, POSITIVE))
                .forEach(instances::add);

        instances = filterInstances(instances, filter);

        int trainingSize = Math.round(instances.size() * trainRatio);
        int testSize = instances.size() - trainingSize;
        instances.randomize(new Random());
        trainInstances = new Instances(instances, 0, trainingSize);
        testInstances = new Instances(instances, trainingSize, testSize);

        this.classifiers
                .forEach(classifier -> {
                    if (classifier == null) {
                        return;
                    }
                    try {
                        classifier.buildClassifier(trainInstances);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Collect the attributes from the stemmed pages.
     *
     * @param pagesStem         the stemmed pages to collect attributes
     * @param maxAttributeCount the max number of attributes (high frequency attributes are selected)
     * @return a list of attributes ordered (descending) by their frequency
     */
    private List<Attribute> collectAttributes(List<Map<String, Integer>> pagesStem, int maxAttributeCount) {
        return pagesStem.stream()
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum)).entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, (v1, v2) -> v2 - v1))
                .limit(maxAttributeCount)
                .map(Map.Entry::getKey)
                .map(Attribute::new)
                .collect(Collectors.toList());
    }

    /**
     * Filter the received instances. If fail to filter the instances, the original is returned and the stack trace is
     * print.
     *
     * @param instances the instances to filter the attributes
     * @param filter    the filter to be used (null  filters nothing)
     * @return the instances with filtered attributes
     */
    private Instances filterInstances(Instances instances, Filter filter) {
        if (filter == null) {
            return instances;
        }
        try {
            filter.setInputFormat(instances);
            return Filter.useFilter(instances, filter);
        } catch (Exception e) {
            e.printStackTrace();
            return instances;
        }
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
        instance.setDataset(instances);
        if (clazz != null) {
            instance.setClassValue(clazz);
        } else {
            instance.setClassMissing();
        }
        return instance;
    }

    //

    /**
     * Returns the page classifier label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the number of internal classifiers in this page classifier. Null classifiers are count too.
     *
     * @return the classifier count
     */
    public int classifierCount() {
        return classifiers.size();
    }

    /**
     * Returns a copy of the classifier in the received index.
     *
     * @param index the classifier index
     * @return a classifier copy (already trained) or null if is a null classifier
     */
    public Classifier getClassifier(int index) throws Exception {
        return classifiers.get(index) != null ? AbstractClassifier.makeCopy(classifiers.get(index)) : null;
    }

    /**
     * Returns a copy of each classifier in this page classifier.
     *
     * @return the classifiers
     */
    public List<Classifier> getClassifiers() {
        return classifiers.stream()
                .map(AbstractClassifier.class::cast)
                .map(classifier -> {
                    if (classifier == null) {
                        return null;
                    }
                    try {
                        return AbstractClassifier.makeCopy(classifier);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns the evaluation of the classifier in the received index.
     *
     * @param index the classifier index
     * @return the evaluation of the classifier ({@link Evaluation#evaluateModel(Classifier, Instances, Object...)}
     * already called with the training and test instances) or null if the classifier is null.
     */
    public Evaluation getClassifierEvaluation(int index) throws Exception {
        Classifier classifier = classifiers.get(index);
        if (classifier == null) {
            return null;
        }
        Evaluation evaluation = new Evaluation(trainInstances);
        evaluation.evaluateModel(classifier, testInstances);
        return evaluation;
    }

    /**
     * Returns the evaluation of each classifier.
     *
     * @return the evaluation of each classifier ({@link Evaluation#evaluateModel(Classifier, Instances, Object...)}
     * already called with the training and test instances) or null if the classifier is null.
     */
    public List<Evaluation> getClassifierEvaluations() {
        return classifiers.stream()
                .map(classifier -> {
                    if (classifier == null) {
                        return null;
                    }
                    try {
                        Evaluation evaluation = new Evaluation(trainInstances);
                        evaluation.evaluateModel(classifier, testInstances);
                        return evaluation;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Gets the default classifier index.
     *
     * @return the default classifier index
     */
    public int getDefaultClassifierIndex() {
        return defaultClassifierIndex;
    }

    /**
     * Sets the default classifier index.
     *
     * @param defaultClassifierIndex the new classifier index
     */
    public void setDefaultClassifierIndex(int defaultClassifierIndex) {
        this.defaultClassifierIndex = defaultClassifierIndex;
    }

    /**
     * Returns a copy of the page classifier filter.
     *
     * @return a copy of the filter or null if has no filter
     */
    public Filter getFilter() throws Exception {
        return filter != null ? Filter.makeCopy(filter) : null;
    }

    /**
     * Returns the max attribute count.
     *
     * @return the max attribute count
     */
    public int getMaxAttributeCount() {
        return maxAttributeCount;
    }

    /**
     * Classifies the received page and returns the class, the default classifier is used to classify.
     *
     * @param page the page to classify
     * @return the page class
     * @throws Exception if some error happens with the classifier or if the selected classifier is null.
     */
    public String classify(Document page) throws Exception {
        return classify(page, defaultClassifierIndex);
    }

    /**
     * Classifies the received page and returns the class.
     *
     * @param page            the page to classify
     * @param classifierIndex the index of the classifier to be used
     * @return the page class
     * @throws Exception if some error happens with the classifier or if the selected classifier is null.
     */
    public String classify(Document page, int classifierIndex) throws Exception {
        Instance pageInstance = buildInstanceFromPage(page, null);
        return CLASSES.get((int) Math.round(classifiers.get(classifierIndex).classifyInstance(pageInstance)));
    }
}
