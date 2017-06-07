package index;

import extractor.attribute.Attribute;
import extractor.attribute.NumericAttribute;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InvertedIndexPreprocessor {
    private BlockingQueue<Pair<URL, Map<String, Attribute>>> documents;
    private Map<String, ? extends Class<? extends Attribute>> higherFrequencyAttributes;
    private Map<String, ? extends Class<? extends Attribute>> smallerFrequencyAttributes;
    private int frequentAttributesToSelect;

    private void filterHighFrequencyAttributes() {

        //  Ordering attributes by frequency
        List<Pair<String, ? extends Class<? extends Attribute>>> attributesOrderedByFrequency = documents.stream()
                .map(Pair::getValue)
                .flatMap(map -> map.entrySet().stream()
                        .map(entry -> new Pair<>(entry.getKey(), entry.getValue().getClass())))
                .collect(Collectors.toMap(Function.identity(), word -> 1, Integer::sum)).entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue() - entry1.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        higherFrequencyAttributes = attributesOrderedByFrequency.stream()
                .limit(frequentAttributesToSelect)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        smallerFrequencyAttributes = attributesOrderedByFrequency.stream()
                .skip(frequentAttributesToSelect)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        // Remove small frequency attributes
        documents.stream()
                .map(Pair::getValue)
                .forEach(map -> smallerFrequencyAttributes.keySet().forEach(map::remove));
    }

    private void discretizeNumericAttributes() {

        // Get numeric attribute values
        Map<String, List<NumericAttribute>> numericAttributeValues = documents.stream()
                .map(Pair::getValue)
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> higherFrequencyAttributes.get(entry.getKey()).equals(NumericAttribute.class))
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> Collections.singletonList((NumericAttribute) entry.getValue()),
                                (l1, l2) -> Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toList())
                        )
                );
    }

    private void simpleBinner(Map<String, Attribute> document, Map<String, List<NumericAttribute>> numericAttributeValues, int bins) {
        List<Pair<Double, Double>> ranges = new ArrayList<>();
        Map<String, Pair<Double, Double>> numericAttributeValuesRanges = numericAttributeValues.entrySet().stream()
                .map(entry ->
                        new Pair<>(
                                entry.getKey(),
                                new Pair<>(
                                        entry.getValue().stream().mapToDouble(NumericAttribute::value).min().orElse(0),
                                        entry.getValue().stream().mapToDouble(NumericAttribute::value).max().orElse(0)
                                )
                        )
                )
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }
}
