package crawler;

import java.util.List;
import java.util.stream.Stream;

public class LinkClassifier {

    public LinkClassifier(List<String> positives, List<String> negatives) {
        positives.stream()
                .map(link -> link.split("/"))
                .map(split -> Stream.of(split)
                        .skip(4)
                        .toArray(size -> new String[size]))
                .map(split -> Stream.of(split));


    }
}