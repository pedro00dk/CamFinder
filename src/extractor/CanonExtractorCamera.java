package extractor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CanonExtractorCamera implements CameraDomainExtractor {

    public static final Map<String, String> MAPPED_ATTRIBUTE_NAMES;

    public static final Map<String, Function<String, String>> ATTRIBUTE_TYPE_ACTIONS;

    static {
        Map<String, String> mappedAttributeNames = new HashMap<>();
        mappedAttributeNames.put("Total Pixels", "Megapixels");
        mappedAttributeNames.put("Aspect Ratio", "Aspect Ratio");
        mappedAttributeNames.put("Digital Zoom", "Zoom");
        mappedAttributeNames.put("Zoom Magnification", "Zoom");
        MAPPED_ATTRIBUTE_NAMES = Collections.unmodifiableMap(mappedAttributeNames);
        //
        Map<String, Function<String, String>> attributeTypeActions = new HashMap<>();
        attributeTypeActions.put("Megapixels", CameraDomainExtractor::formatMegapixel);
        attributeTypeActions.put("Aspect Ratio", Function.identity());
        attributeTypeActions.put("Zoom", CameraDomainExtractor::formatZoom);
        ATTRIBUTE_TYPE_ACTIONS = Collections.unmodifiableMap(attributeTypeActions);

    }

    @Override
    public Map<String, String> extractWebSiteContent(Document document, URL link) throws MalformedURLException {

        // get camera name
        String name = document.getElementsByAttributeValueContaining("itemprop", "name").stream()
                .filter(element -> element.tag().getName().equals("span"))
                .findFirst()
                .orElse(null)
                .text();

        // get price
        String price = document.select(".price.final_price").text();

        // get all attributes
        Map<String, String> attributes = document.select(".content_container.service_and_support p").stream()
                .filter(paragraph -> paragraph.children().size() > 1)
                .filter(paragraph -> MAPPED_ATTRIBUTE_NAMES.containsKey(paragraph.child(0).text()))
                .collect(Collectors.toMap(paragraph -> MAPPED_ATTRIBUTE_NAMES.get(paragraph.child(0).text()), Element::text, (v1, v2) -> v1));
        attributes.entrySet()
                .forEach(entry -> entry.setValue(ATTRIBUTE_TYPE_ACTIONS.get(entry.getKey()).apply(entry.getValue())));

        attributes.put("name", name);
        attributes.put("price", price);
        return attributes;
    }

    private static String canonAttributeExtractor(Element attr) {
        return attr.childNodes().stream()
                .filter(node -> node instanceof TextNode)
                .map(TextNode.class::cast)
                .map(node -> node.text().trim())
                .filter(node -> node.length() > 0)
                .collect(Collectors.joining());
    }
}
