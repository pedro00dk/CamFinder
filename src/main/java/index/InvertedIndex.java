package index;

import extractor.attribute.Attribute;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class InvertedIndex {
    public Map<Integer, URL> indexedDocuments;
    public Map<URL, Integer> documentIndexes;

    public Map<String, Map<Integer, Integer>> termDocuments;
    public Map<String, Integer> termFrequency;
    public List<String> attributes;

    public static void serialize(InvertedIndex index, Path path, boolean compressed) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(compressed ? "c" : "d").append("\n");
        builder.append("%\n");
        index.indexedDocuments.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .forEach(entry -> builder.append(entry.getValue().toString()).append("\n"));
        builder.append("%\n");
        index.termDocuments.keySet()
                .forEach(term -> {
                    builder.append(term).append("%").append(index.termFrequency.get(term)).append("=");
                    AtomicInteger currentDocumentIndex = new AtomicInteger(0);
                    index.termDocuments.get(term).entrySet().stream()
                            .sorted(Comparator.comparingInt(Map.Entry::getKey))
                            .forEach(entry -> {
                                if (compressed) {
                                    builder.append(entry.getKey() - currentDocumentIndex.get()).append(",").append(entry.getValue()).append(";");
                                    currentDocumentIndex.set(entry.getKey());
                                } else {
                                    builder.append(entry.getKey()).append(",").append(entry.getValue()).append(";");
                                }
                            });
                    builder.append("\n");
                });
        builder.append("%\n");
        index.attributes
                .forEach(builder::append);
        builder.append("%\n");

        Files.newBufferedWriter(path, StandardCharsets.UTF_8)
                .append(builder.toString())
                .close();
    }

    public static InvertedIndex deserialize(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        boolean compressed = lines.get(0).equals("c");

        int nextBlockLine = 2;

        Map<Integer, URL> indexedDocuments = new HashMap<>();
        Map<URL, Integer> documentIndexes = new HashMap<>();
        for (int i = nextBlockLine; ; i++) {
            String line = lines.get(i);
            if (line.equals("%")) {
                nextBlockLine = i + 1;
                break;
            }
            URL url = new URL(line);
            indexedDocuments.put(i - 2, url);
            documentIndexes.put(url, i - 2);
        }

        Map<String, Map<Integer, Integer>> termDocuments = new HashMap<>();
        Map<String, Integer> termFrequency = new HashMap<>();
        for (int i = nextBlockLine; ; i++) {
            String line = lines.get(i);
            if (line.equals("%")) {
                nextBlockLine = i + 1;
                break;
            }
            String[] parts = line.split("=");
            String[] headParts = parts[0].split("%");
            String[] tailParts = parts[1].split(";");

            Map<Integer, Integer> documentsFrequency = new HashMap<>();
            for (int j = 0; j < tailParts.length - 1; j++) {
                String[] documentAndFrequency = tailParts[i].split(",");
                documentsFrequency.put(Integer.parseInt(documentAndFrequency[0]), Integer.parseInt(documentAndFrequency[1]));
            }
            termDocuments.put(headParts[0], documentsFrequency);
            termFrequency.put(headParts[0], Integer.parseInt(headParts[1]));
        }

        List<String> attributes = new ArrayList<>();
        for (int i = nextBlockLine; ; i++) {
            String line = lines.get(i);
            if (line.equals("%")) {
                break;
            }
            attributes.add(line);
        }
        return new InvertedIndex(indexedDocuments, documentIndexes, termDocuments, termFrequency, attributes);
    }

    private InvertedIndex(Map<Integer, URL> indexedDocuments, Map<URL, Integer> documentIndexes, Map<String, Map<Integer, Integer>> termDocuments, Map<String, Integer> termFrequency, List<String> attributes) {
        this.indexedDocuments = indexedDocuments;
        this.documentIndexes = documentIndexes;
        this.termDocuments = termDocuments;
        this.termFrequency = termFrequency;
        this.attributes = attributes;
    }

    public InvertedIndex(Queue<Pair<URL, Map<String, String>>> documents, int frequentAttributesToSelect) {
        final AtomicInteger index = new AtomicInteger(0);
        indexedDocuments = Collections.unmodifiableMap(
                documents.stream()
                        .map(Pair::getKey)
                        .collect(Collectors.toMap(url -> index.getAndIncrement(), Function.identity()))
        );
        documentIndexes = Collections.unmodifiableMap(
                indexedDocuments.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey))
        );

        termDocuments = new HashMap<>();
        termFrequency = new HashMap<>();

        Pair<List<String>, List<String>> highAndLowFrequencyAttributes = selectHighAndLowFrequencyAttributes(documents, frequentAttributesToSelect);
        this.attributes = highAndLowFrequencyAttributes.getKey();

        removeLowFrequencyAttributes(documents, highAndLowFrequencyAttributes.getValue());
        buildInvertedIndex(documents);
    }

    private Pair<List<String>, List<String>> selectHighAndLowFrequencyAttributes(Queue<Pair<URL, Map<String, String>>> documents, int frequentAttributesToSelect) {

        //  Ordering attributes by frequency
        List<String> attributesOrderedByFrequency = documents.stream()
                .map(Pair::getValue)
                .flatMap(map -> map.entrySet().stream()
                        .map(entry -> new Pair<>(entry.getKey(), entry.getValue().getClass())))
                .collect(Collectors.toMap(Function.identity(), word -> 1, Integer::sum)).entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue() - entry1.getValue())
                .map(entry -> entry.getKey().getKey())
                .collect(Collectors.toList());

        List<String> highFrequencyAttributes = attributesOrderedByFrequency.stream()
                .limit(frequentAttributesToSelect)
                .collect(Collectors.toList());

        List<String> lowFrequencyAttributes = attributesOrderedByFrequency.stream()
                .skip(frequentAttributesToSelect)
                .collect(Collectors.toList());

        return new Pair<>(highFrequencyAttributes, lowFrequencyAttributes);
    }

    private void removeLowFrequencyAttributes(Queue<Pair<URL, Map<String, String>>> documents, List<String> lowFrequencyAttributes) {
        documents.stream()
                .map(Pair::getValue)
                .forEach(attributeMap -> lowFrequencyAttributes.forEach(attributeMap::remove));
    }

    private void buildInvertedIndex(Queue<Pair<URL, Map<String, String>>> documents) {
        documents
                .forEach(document -> document.getValue()
                        .forEach((key, value) -> {
                                    if (key.equals("price")) {
                                        value = value.replaceAll("\\.(\\d)+", "").replaceAll("[^\\d]", "");
                                        int integerValue = Integer.parseInt(value);
                                        if (integerValue <= 500) {
                                            value = "[1-500]";
                                        } else if (integerValue <= 1000) {
                                            value = "[501-1000]";
                                        } else if (integerValue <= 2000) {
                                            value = "[1001-2000]";
                                        } else if (integerValue <= 4000) {
                                            value = "[2001-4000]";
                                        } else if (integerValue <= 10000) {
                                            value = "[4001-10000]";
                                        } else {
                                            value = "[10001-+]";
                                        }
                                    }
                                    Stream.of(value.split(" +"))
                                            .forEach(valuePart -> {
                                                String invertedIndexKey = key + "." + valuePart;

                                                // Creating or increasing the term frequency
                                                termFrequency.put(invertedIndexKey, termFrequency.getOrDefault(invertedIndexKey, 0) + 1);

                                                // Creating or adding and increasing the document frequency in the term documents
                                                Map<Integer, Integer> termDocumentFrequencyMap = termDocuments.getOrDefault(invertedIndexKey, new HashMap<>());
                                                termDocuments.put(invertedIndexKey, termDocumentFrequencyMap);
                                                int documentIndex = documentIndexes.get(document.getKey());
                                                termDocumentFrequencyMap.put(documentIndex, termDocumentFrequencyMap.getOrDefault(documentIndex, 0) + 1);
                                            });
                                }
                        )
                );
    }
}
