package extractor;

import extractor.specific.*;
import javafx.util.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GeneralExtractor implements CameraDomainExtractor {
    public static final Map<String, String> GERALATTRIBUTES;

    public static final Map<String, Function<String, String>> ATTRIBUTE_TYPE_ACTIONS;

    static {
        GERALATTRIBUTES = new HashMap<>();
        GERALATTRIBUTES.putAll(CanonExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(CurrysExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(DPPreviewExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(NewEggExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(NikonExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(RicohExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(SigmaPhotoExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(SonyExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(VisionsExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(WebPhotoGraphicExtractor.MAPPED_ATTRIBUTE_NAMES);

        ATTRIBUTE_TYPE_ACTIONS = new HashMap<>();
        ATTRIBUTE_TYPE_ACTIONS.putAll(CurrysExtractor.ATTRIBUTE_TYPE_ACTIONS);
        ATTRIBUTE_TYPE_ACTIONS.put("price", Function.identity());

    }

    @Override
    public Map<String, String> extractWebSiteContent(Document document, URL link) {
        List<Element> data = search(document);

        Map<String, String> attributes = IntStream.range(0, data.size())
                .mapToObj(index -> new Pair<>(data.get(index).child(0).text(), data.get(index).child(1).text()))
                .collect(Collectors.toMap(pair -> GERALATTRIBUTES.get(pair.getKey()), Pair::getValue, (v1, v2) -> v1));

        // CANON PART
        if (attributes.keySet().stream().filter(key -> key.isEmpty()).count() >= 0) {
            attributes = data.stream()
                    .filter(d -> GERALATTRIBUTES.containsKey(d.child(0).text()))
                    .collect(Collectors.toMap(paragraph -> GERALATTRIBUTES.get(paragraph.child(0).text()), Element::text, (v1, v2) -> v1));
        }
        //CANON PART END

        //processing values
        attributes.entrySet()
                .forEach(entry -> entry.setValue(ATTRIBUTE_TYPE_ACTIONS.get(entry.getKey()).apply(entry.getValue())));


        //adding price and name
        String title = document.getElementsByTag("title").text();
        String n = title.split(" \\|| \\-|:")[0].replaceAll("[Bb]uy | [Bb]uy| [Ss]pecifications|[Ss]pecifications ", "");
        attributes.put("name", n);

        String price = document.getElementsByTag("meta").stream()
                .filter(element -> element.attributes().asList().stream()
                        .anyMatch(attribute -> attribute.getValue().toLowerCase().contains("price"))
                )
                .filter(element -> element.attr("content").matches(".\\d+([.,]\\d+)*"))
                .findFirst()
                .map(element -> element.attr("content"))
                .orElse(null);

        if (price == null) {
            price = document.getElementsByTag("span").stream()
                    .filter(element -> element.attributes().asList().stream()
                            .anyMatch(attribute -> attribute.getValue().toLowerCase().contains("price"))
                    )
                    .filter(element -> element.text().matches(".\\d+([.,]\\d+)*"))
                    .findFirst()
                    .map(Element::text)
                    .orElse(null);
        }

        if (price != null) {
            attributes.put("price", price);
        }

        return attributes;
    }

    private List<Element> search(Document root) {
        Queue<Element> queue = new LinkedList<>();
        List<Element> selected = new ArrayList<>();

        queue.add(root);
        Element current;
        while ((current = queue.poll()) != null) {
            queue.addAll(current.children());
            if (current.children().size() == 2) {
                if (GERALATTRIBUTES.containsKey(current.child(0).text())) {
                    selected.add(current);

                }
            }
        }

        return selected;
    }

}
