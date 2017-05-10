package crawler;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;
import java.util.stream.Stream;

public class LinkClassifier {
    private List<Attribute> attributes;
    private Instances instances;
    private Instances trainInstances;
    private Instances testInstances;

    private Classifier classifier;

    public LinkClassifier(List<String> positives, List<String> negatives, float trainRatio) throws Exception {

        List<String> links = new ArrayList<>();
        links.addAll(negatives);
        links.addAll(positives);

        attributes = getAllAttributes(links);
        instances = new Instances("Links", (ArrayList<Attribute>) attributes, links.size());
        instances.setClassIndex(attributes.size() - 1);

        for (int i = 0; i < positives.size(); i++) {
            instances.add(createLinkInstance(positives.get(i), "p"));
        }

        for (int i = 0; i < negatives.size(); i++) {
            instances.add(createLinkInstance(negatives.get(i), "n"));
        }

        int trainingSize = Math.round(instances.size() * trainRatio);
        int testSize = instances.size() - trainingSize;
        instances.randomize(new Random());
        trainInstances = new Instances(instances, 0, trainingSize);
        testInstances = new Instances(instances, trainingSize, testSize);

        classifier = new RandomForest();
        classifier.buildClassifier(trainInstances);

        Evaluation eval = new Evaluation(trainInstances);
        eval.evaluateModel(classifier, testInstances);
        eval.toSummaryString();
    }

    public double classify(String link) throws Exception {
        return classifier.classifyInstance(createLinkInstance(link, ""));
    }

    private List<Attribute> getAllAttributes(List<String> links) {
        Set<String> stringAttributes = new HashSet<>();
        for (String link : links) {
            String[] linkAttributes = getLinkAttributes(link);
            for (String linkAttribute : linkAttributes) {
                stringAttributes.add(linkAttribute);
            }
        }
        List<Attribute> attributes = new ArrayList<>();
        for (String stringAttribute : stringAttributes) {
            attributes.add(new Attribute(stringAttribute));
        }
        List<String> classValues = new ArrayList<>();
        classValues.add("n");
        classValues.add("p");
        attributes.add(new Attribute("-class-", classValues));
        return attributes;
    }

    private String[] getLinkAttributes(String link) {
        String[] parts = link.split("/");
        String[][] partsOfParts = new String[parts.length][];
        int partsCount = 0;
        for (int i = 0; i < parts.length; i++) {
            partsOfParts[i] = parts[i].split("[^A-Za-z]");
            String[] filteredParts = new String[partsOfParts[i].length - 4];
            for (int j = 0; j < filteredParts.length; j++) {
                filteredParts[j] = partsOfParts[i][j + 4];
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

    private Instance createLinkInstance(String link, String clazz) {
        Instance linkInstance = new DenseInstance(attributes.size());
        for (int i = 0; i < attributes.size() - 1; i++) {
            boolean attributeContained = false;
            String[] linkAttributes = getLinkAttributes(link);
            for (String linkAttribute : linkAttributes) {
                if (linkAttribute.equals(attributes.get(i).name())) {
                    attributeContained = true;
                    break;
                }
            }
            linkInstance.setValue(i, attributeContained ? 1 : 0);
        }
        linkInstance.setValue(attributes.size() - 1, clazz);
        return linkInstance;
    }
}