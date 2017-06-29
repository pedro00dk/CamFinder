package rank;

import index.InvertedIndex;
import javafx.util.Pair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class Rank {
    private InvertedIndex invertedIndex;
    private Map<String, Integer> termOrder;

    private Map<Integer, double[]> documentTfVectors;
    private Map<Integer, Double> documentTfVectorMagnitudes;

    private Map<Integer, double[]> documentTfIdfVectors;
    private Map<Integer, Double> documentTfIdfVectorMagnitudes;

    public Rank(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
        calculateTermOrder();
        calculateDocumentVectors();
        calculateDocumentVectorMagnitudes();
    }

    private void calculateTermOrder() {
        AtomicInteger termIndex = new AtomicInteger(0);
        termOrder = invertedIndex.termFrequency.entrySet().stream()
                .map(entry -> new Pair<>(entry.getKey(), termIndex.getAndIncrement()))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private void calculateDocumentVectors() {
        documentTfVectors = invertedIndex.indexedDocuments.keySet().stream()
                .map(index -> new Pair<>(index, calculateDocumentTfIdfVector(index, true)))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        documentTfIdfVectors = invertedIndex.indexedDocuments.keySet().stream()
                .map(index -> new Pair<>(index, calculateDocumentTfIdfVector(index, false)))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private double[] calculateDocumentTfIdfVector(int documentIndex, boolean tfOnly) {
        return invertedIndex.termDocuments.entrySet().stream()
                .map(entry -> {
                    int termTfIdfArrayIndex = termOrder.get(entry.getKey());
                    int termFrequencyInDocument = entry.getValue().getOrDefault(documentIndex, 0);
                    if (termFrequencyInDocument > 0) {
                        boolean a = false;
                    }
                    double tf = tfOnly ? (termFrequencyInDocument > 0 ? 1 : 0) : (termFrequencyInDocument == 0 ? 0 : 1 + Math.log(termFrequencyInDocument));
                    double idf = tfOnly ? 1 : Math.log(
                            invertedIndex.indexedDocuments.size()
                                    / (double) invertedIndex.termFrequency.get(entry.getKey())
                    );
                    return new Pair<>(termTfIdfArrayIndex, tf * idf);
                })
                .sorted(Comparator.comparingInt(Pair::getKey))
                .map(Pair::getValue)
                .mapToDouble(boxedDouble -> boxedDouble)
                .toArray();
    }

    private void calculateDocumentVectorMagnitudes() {
        documentTfIdfVectorMagnitudes = documentTfIdfVectors.entrySet().stream()
                .map(entry -> new Pair<>(entry.getKey(), calculateDocumentTfIdfVectorMagnitude(entry.getValue())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        documentTfVectorMagnitudes = documentTfVectors.entrySet().stream()
                .map(entry -> new Pair<>(entry.getKey(), calculateDocumentTfIdfVectorMagnitude(entry.getValue())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private double calculateDocumentTfIdfVectorMagnitude(double[] value) {
        return Math.sqrt(DoubleStream.of(value)
                .map(termTfIdf -> termTfIdf * termTfIdf)
                .sum()
        );

    }

    public List<URL> rank(List<String> query, boolean tfOnly) {
        double[] queryVector = calculateQueryTfIdfVector(query, tfOnly);
        return invertedIndex.documentIndexes.entrySet().stream()
                .map(entry -> {
                    double[] documentVector = tfOnly ? documentTfVectors.get(entry.getValue()) : documentTfIdfVectors.get(entry.getValue());
                    double documentVectorMagnitude = tfOnly ? documentTfVectorMagnitudes.get(entry.getValue()) : documentTfIdfVectorMagnitudes.get(entry.getValue());
                    return new Pair<>(entry.getKey(), calculateCossineBetweenVectors(queryVector, 1, documentVector, documentVectorMagnitude));
                })
                .sorted((p1, p2) -> (int) Math.signum(p2.getValue() - p1.getValue()))
                .map(Pair::getKey)
                .collect(Collectors.toList());
    }

    private double[] calculateQueryTfIdfVector(List<String> query, boolean tfOnly) {
        return invertedIndex.termDocuments.entrySet().stream()
                .map(entry -> {
                    int termTfIdfArrayIndex = termOrder.get(entry.getKey());
                    boolean queryContainsTerm = query.stream()
                            .anyMatch(queryTerm -> queryTerm.toLowerCase().contains(entry.getKey().toLowerCase())
                                    || entry.getKey().toLowerCase().contains(queryTerm.toLowerCase()));
                    double idf = !queryContainsTerm
                            ? 0
                            : tfOnly
                            ? 1
                            : Math.log(invertedIndex.indexedDocuments.size() / (double) invertedIndex.termFrequency.get(entry.getKey()));
                    return new Pair<>(termTfIdfArrayIndex, idf);
                })
                .sorted(Comparator.comparingInt(Pair::getKey))
                .map(Pair::getValue)
                .mapToDouble(boxedDouble -> boxedDouble)
                .toArray();
    }

    private double calculateCossineBetweenVectors(double[] vector1, double vector1Magnitude, double[] vector2, double vector2Magnitude) {
        return dotProduct(vector1, vector2) / vector1Magnitude * vector2Magnitude;
    }

    private double dotProduct(double[] vector1, double[] vector2) {
        return IntStream.range(0, vector1.length)
                .mapToDouble(index -> vector1[index] * vector2[index])
                .sum();
    }
}
