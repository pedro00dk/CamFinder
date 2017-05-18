package extractor;

import extractor.specific.*;
import javafx.util.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneralExtractor implements CameraDomainExtractor {

    public static final Map<String, String> MAPPED_ATTRIBUTE_NAMES;

    public static final Map<String, Function<String, String>> ATTRIBUTE_TYPE_ACTIONS;

    static {
        Map<String, String> mappedAttributeNames = new HashMap<>();
        mappedAttributeNames.putAll(CanonExtractor.MAPPED_ATTRIBUTE_NAMES);
        mappedAttributeNames.putAll(CurrysExtractor.MAPPED_ATTRIBUTE_NAMES);
        mappedAttributeNames.putAll(DPPreviewExtractor.MAPPED_ATTRIBUTE_NAMES);
        mappedAttributeNames.putAll(NewEggExtractor.MAPPED_ATTRIBUTE_NAMES);
        mappedAttributeNames.putAll(NikonExtractor.MAPPED_ATTRIBUTE_NAMES);
        mappedAttributeNames.putAll(RicohExtractor.MAPPED_ATTRIBUTE_NAMES);
        mappedAttributeNames.putAll(SigmaPhotoExtractor.MAPPED_ATTRIBUTE_NAMES);
        mappedAttributeNames.putAll(SonyExtractor.MAPPED_ATTRIBUTE_NAMES);
        mappedAttributeNames.putAll(VisionsExtractor.MAPPED_ATTRIBUTE_NAMES);
        mappedAttributeNames.putAll(WexPhotoGraphicExtractor.MAPPED_ATTRIBUTE_NAMES);

        MAPPED_ATTRIBUTE_NAMES = Collections.unmodifiableMap(mappedAttributeNames);

        Map<String, Function<String, String>> attributeTypeActions = new HashMap<>();
        attributeTypeActions.put("Megapixels", CameraDomainExtractor::formatMegapixel);
        attributeTypeActions.put("Zoom", CameraDomainExtractor::formatZoom);
        attributeTypeActions.put("Storage Mode", Function.identity());
        attributeTypeActions.put("Sensitivity", Function.identity());
        attributeTypeActions.put("Shutter Speed", Function.identity());
        attributeTypeActions.put("Sensor Size", Function.identity());
        attributeTypeActions.put("price", Function.identity());

        ATTRIBUTE_TYPE_ACTIONS = Collections.unmodifiableMap(attributeTypeActions);
    }

    @Override
    public Map<String, Function<String, String>> getAtrributeTypeActions() {
        return ATTRIBUTE_TYPE_ACTIONS;
    }

    @Override
    public Map<String, String> extractWebSiteContent(Document document) {
        List<Element> data = search(document);

        Map<String, String> attributes = IntStream.range(0, data.size())
                .mapToObj(index -> new Pair<>(data.get(index).child(0).text(), data.get(index).child(1).text()))
                .collect(Collectors.toMap(pair -> MAPPED_ATTRIBUTE_NAMES.get(pair.getKey()), Pair::getValue, (v1, v2) -> v1));

        // CANON PART
        if (attributes.keySet().stream().filter(key -> key.isEmpty()).count() >= 0) {
            attributes = data.stream()
                    .filter(d -> MAPPED_ATTRIBUTE_NAMES.containsKey(d.child(0).text()))
                    .collect(Collectors.toMap(paragraph -> MAPPED_ATTRIBUTE_NAMES.get(paragraph.child(0).text()), Element::text, (v1, v2) -> v1));
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
            if (current.children().size() == 2 && MAPPED_ATTRIBUTE_NAMES.containsKey(current.child(0).text())) {
                selected.add(current);
            }
        }

        return selected;
    }

}
