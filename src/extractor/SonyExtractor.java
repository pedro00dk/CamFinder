package extractor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SonyExtractor implements CameraDomainExtractor{
    public static final Map<String, String> MAPPED_ATTRIBUTE_NAMES;

    public static final Map<String, Function<String, String>> ATTRIBUTE_TYPE_ACTIONS;

    static {
        Map<String, String> mappedAttributeNames = new HashMap<>();
        mappedAttributeNames.put("Total Pixels", "Megapixels");
       // mappedAttributeNames.put("Digital Zoom", "Zoom");
        mappedAttributeNames.put("Recording Media", "Storage Mode");
        mappedAttributeNames.put("Storage Media", "Storage Mode");
        mappedAttributeNames.put("Sensitivity", "Sensitivity");
        mappedAttributeNames.put("Shutter Speed", "Shutter Speed");
        MAPPED_ATTRIBUTE_NAMES = Collections.unmodifiableMap(mappedAttributeNames);

        Map<String, Function<String, String>> attributeTypeActions = new HashMap<>();
        attributeTypeActions.put("Megapixels", CameraDomainExtractor::formatMegapixel);
        attributeTypeActions.put("Aspect Ratio", Function.identity());
        attributeTypeActions.put("Zoom", CameraDomainExtractor::formatZoom);
        attributeTypeActions.put("Storage Mode", Function.identity());
        attributeTypeActions.put("Sensitivity", Function.identity());
        attributeTypeActions.put("Shutter Speed", Function.identity());

        ATTRIBUTE_TYPE_ACTIONS = Collections.unmodifiableMap(attributeTypeActions);
    }

    @Override
    public Map<String, String> extractWebSiteContent(Document document, URL link) throws MalformedURLException {
        // get camera name
       /* String name = document.getElementsByAttributeValueContaining("itemprop", "name").stream()
                .filter(element -> element.tag().getName().equals("span"))
                .findFirst()
                .orElse(null)
                .text();

        //working
        // get price
        String price = document.select(".price.p1").get(0).child(0).text();

        // get all attributes
       /* Map<String, String> attributes = document.select(".content_container.service_and_support p").stream()
                .filter(paragraph -> paragraph.children().size() > 1)
                .filter(paragraph -> MAPPED_ATTRIBUTE_NAMES.containsKey(paragraph.child(0).text()))
                .collect(Collectors.toMap(paragraph -> MAPPED_ATTRIBUTE_NAMES.get(paragraph.child(0).text()), Element::text, (v1, v2) -> v1));
        attributes.entrySet()
                .forEach(entry -> entry.setValue(ATTRIBUTE_TYPE_ACTIONS.get(entry.getKey()).apply(entry.getValue())));

        attributes.put("name", name);
        attributes.put("price", price);*/

        return null;
    }
}
