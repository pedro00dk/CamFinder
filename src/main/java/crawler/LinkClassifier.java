package crawler;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LinkClassifier {
    private Classifier classifier;
    private List<Attribute> attributes;
    private Instances instances;
    private Instances trainInstances;
    private Instances testInstances;

    public static final String NEGATIVE = "n";
    public static final String NEXT_1 = "n1";
    public static final String NEXT_0 = "n0";
    public static final String POSITIVE = "p";

    public static final List<String> CLASSES = Collections.unmodifiableList(Stream.of(NEGATIVE, NEXT_0, NEXT_1, POSITIVE).collect(Collectors.toList()));

    public LinkClassifier(List<URL> negatives, List<URL> nexts0, List<URL> nexts1, List<URL> positives, float trainRatio) throws Exception {
        List<URL> urls = new ArrayList<>();
        urls.addAll(negatives);
        urls.addAll(nexts0);
        urls.addAll(nexts1);
        urls.addAll(positives);

        attributes = getAllAttributes(urls);
        attributes.add(new Attribute("-class-", CLASSES));

        instances = new Instances("Links", (ArrayList<Attribute>) attributes, urls.size());
        instances.setClassIndex(attributes.size() - 1);
        for (URL url : negatives) {
            instances.add(createLinkInstance(url, NEGATIVE));
        }
        for (URL url : nexts0) {
            instances.add(createLinkInstance(url, NEXT_0));
        }
        for (URL url : nexts1) {
            instances.add(createLinkInstance(url, NEXT_1));
        }
        for (URL url : positives) {
            instances.add(createLinkInstance(url, POSITIVE));
        }
        int trainingSize = Math.round(instances.size() * trainRatio);
        int testSize = instances.size() - trainingSize;
        instances.randomize(new Random());
        trainInstances = new Instances(instances, 0, trainingSize);
        testInstances = new Instances(instances, trainingSize, testSize);

        classifier = new NaiveBayes();
        classifier.buildClassifier(trainInstances);
    }

    private List<Attribute> getAllAttributes(List<URL> urls) {
        Set<String> stringAttributes = new HashSet<>();
        for (URL url : urls) {
            String[] linkAttributes = getURLAttributes(url);
            for (String linkAttribute : linkAttributes) {
                stringAttributes.add(linkAttribute);
            }
        }
        List<Attribute> attributes = new ArrayList<>();
        for (String stringAttribute : stringAttributes) {
            attributes.add(new Attribute(stringAttribute));
        }
        return attributes;
    }

    private String[] getURLAttributes(URL link) {
        String[] parts = link.toString().split("/");
        String[][] partsOfParts = new String[parts.length][];
        int partsCount = 0;
        for (int i = 0; i < parts.length; i++) {
            partsOfParts[i] = parts[i].split("[^A-Za-z]");
            String[] filteredParts = new String[partsOfParts[i].length];
            for (int j = 0; j < filteredParts.length; j++) {
                filteredParts[j] = partsOfParts[i][j];
            }
            partsOfParts[i] = filteredParts;
            partsCount += partsOfParts[i].length;
        }
        parts = new String[partsCount];
        partsCount = 0;
        for (int i = 0; i < partsOfParts.length; i++) {
            for (int j = 0; j < partsOfParts[i].length; j++) {
                parts[partsCount++] = partsOfParts[i][j];
            }
        }
        return parts;
    }

    private Instance createLinkInstance(URL url, String clazz) {

        Instance linkInstance = new DenseInstance(attributes.size());
        linkInstance.setDataset(instances);

        for (int i = 0; i < attributes.size() - 1; i++) {
            boolean attributeContained = false;
            String[] linkAttributes = getURLAttributes(url);
            for (String linkAttribute : linkAttributes) {
                if (linkAttribute.equals(attributes.get(i).name())) {
                    attributeContained = true;
                    break;
                }
            }
            linkInstance.setValue(i, attributeContained ? 1 : 0);
        }
        if (clazz != null) {
            linkInstance.setClassValue(clazz);
        } else {
            linkInstance.setClassMissing();
        }

        return linkInstance;
    }

    public int classifyIndex(URL url) throws Exception {
        return (int) classifier.classifyInstance(createLinkInstance(url, null));
    }

    public String classify(URL url) throws Exception {
        return CLASSES.get(classifyIndex(url));
    }

    public Evaluation getEvaluation() throws Exception {
        Evaluation evaluation = new Evaluation(trainInstances);
        evaluation.evaluateModel(classifier, testInstances);
        return evaluation;
    }
}