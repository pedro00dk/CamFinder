package crawler;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LinkClassifier {

    private List<Attribute> attributes;
    private Instances instances;
    private Instances trainInstances;
    private Instances testInstances;
    static List<String> links = new ArrayList<>();
    static List<String> positives = new ArrayList<>();
    static List<String> next0 = new ArrayList<>();
    static List<String> next1 = new ArrayList<>();
    static List<String> next2 = new ArrayList<>();

    private Classifier classifier;

    public LinkClassifier(float trainRatio) throws Exception {

        loadLists();

        links.addAll(next0);
        links.addAll(next1);
        links.addAll(next2);
        links.addAll(positives);

        attributes = getAllAttributes(links);
        instances = new Instances("Links", (ArrayList<Attribute>) attributes, links.size());
        instances.setClassIndex(attributes.size() - 1);

        for (int i = 0; i < positives.size(); i++) {
            instances.add(createLinkInstance(positives.get(i), "p"));
        }

        for (int i = 0; i < next0.size(); i++) {
            instances.add(createLinkInstance(next0.get(i), "n0"));
        }

        for (int i = 0; i < next1.size(); i++) {
            instances.add(createLinkInstance(next1.get(i), "n1"));
        }

        for (int i = 0; i < next2.size(); i++) {
            instances.add(createLinkInstance(next2.get(i), "n2"));
        }

        int trainingSize = Math.round(instances.size() * trainRatio);
        int testSize = instances.size() - trainingSize;
        instances.randomize(new Random());
       // System.out.println(instances.size());
        trainInstances = new Instances(instances, 0, trainingSize);
        testInstances = new Instances(instances, trainingSize, testSize);

        classifier = new RandomForest();
        classifier.buildClassifier(trainInstances);

        Evaluation eval = new Evaluation(trainInstances);
        eval.evaluateModel(classifier, testInstances);
        eval.toSummaryString();
    }

    public double classify(String link) throws Exception {
        return classifier.classifyInstance(createLinkInstance(link, null));
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

        classValues.add("p");
        classValues.add("n2");
        classValues.add("n1");
        classValues.add("n0");

        attributes.add(new Attribute("-class-", classValues));

        return attributes;
    }

    private String[] getLinkAttributes(String link) {
        String[] parts = link.split("/");
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

    private Instance createLinkInstance(String link, String clazz) {

        Instance linkInstance = new DenseInstance(attributes.size());
        linkInstance.setDataset(instances);

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
        if (clazz != null) {
            linkInstance.setClassValue(clazz);
        } else {
            linkInstance.setClassMissing();
        }

        return linkInstance;
    }

    private void loadLists() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("pages\\next0.txt"));
        while (br.ready()) {
            String linha = br.readLine();
            next0.add(linha);
        }
        br.close();

        BufferedReader br1 = new BufferedReader(new FileReader("pages\\next1.txt"));
        while (br1.ready()) {
            String linha = br1.readLine();
            next1.add(linha);
        }
        br1.close();

        BufferedReader br2 = new BufferedReader(new FileReader("pages\\next2.txt"));
        while (br2.ready()) {
            String linha = br2.readLine();
            next2.add(linha);
        }
        br2.close();

        BufferedReader br3 = new BufferedReader(new FileReader("pages\\positive.txt"));
        while (br3.ready()) {
            String linha = br3.readLine();
            positives.add(linha);
        }
        br3.close();

    }
}