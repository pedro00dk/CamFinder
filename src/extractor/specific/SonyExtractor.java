package extractor.specific;

import extractor.CameraDomainExtractor;
import javafx.util.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SonyExtractor implements CameraDomainExtractor {
    public static final Map<String, String> MAPPED_ATTRIBUTE_NAMES;

    public static final Map<String, Function<String, String>> ATTRIBUTE_TYPE_ACTIONS;

    static {
        Map<String, String> mappedAttributeNames = new HashMap<>();
        mappedAttributeNames.put("Number of Pixels (total)", "Megapixels");
        mappedAttributeNames.put("Optical Zoom", "Zoom");
        mappedAttributeNames.put("Digital Zoom (Still Image)", "Zoom");
        mappedAttributeNames.put("Media", "Storage Mode");
        mappedAttributeNames.put("Compatible Recording Media", "Storage Mode");
        mappedAttributeNames.put("ISO Sensitivity (Still Image)(Recommended Exposure Index)", "Sensitivity");
        mappedAttributeNames.put("ISO Sensitivity (Recommended Exposure Index)", "Sensitivity");
        mappedAttributeNames.put("Shutter Speed", "Shutter Speed");
        mappedAttributeNames.put("Sensor Type", "Sensor Size");

        MAPPED_ATTRIBUTE_NAMES = Collections.unmodifiableMap(mappedAttributeNames);

        //TODO funções específicas ou função geral
        Map<String, Function<String, String>> attributeTypeActions = new HashMap<>();
        attributeTypeActions.put("Megapixels", CameraDomainExtractor::formatMegapixel);
        attributeTypeActions.put("Zoom", CameraDomainExtractor::formatZoom);
        attributeTypeActions.put("Storage Mode", Function.identity());
        attributeTypeActions.put("Sensitivity", Function.identity());
        attributeTypeActions.put("Shutter Speed", Function.identity());
        attributeTypeActions.put("Sensor Size", Function.identity());

        ATTRIBUTE_TYPE_ACTIONS = Collections.unmodifiableMap(attributeTypeActions);
    }

    @Override
    public Map<String, String> extractWebSiteContent(Document document, URL link) {

        // get camera name
        String name = document.select(".primary-link.l3.breadcrumb-link").text();
        // get price
        String price = document.select(".price.p1").get(0).child(0).text();

        // get all attributes
        Elements keys1 = document.getElementsByTag("dt").select(".l3");
        Elements values1 = document.getElementsByTag("dd").select(".p3");

        //set attributes type 1
        Map<String, String> attributes = IntStream.range(0, keys1.size())
                .filter(index -> MAPPED_ATTRIBUTE_NAMES.containsKey(keys1.get(index).text()))
                .mapToObj(index -> new Pair<>(keys1.get(index).text(), values1.get(index).text()))
                .collect(Collectors.toMap(pair -> MAPPED_ATTRIBUTE_NAMES.get(pair.getKey()), Pair::getValue, (v1, v2) -> v1));

        //set attributes type 2
        Elements elements2 = document.select(".spec-cell-inner");

        Map<String, String> attributes2 = IntStream.range(0, elements2.size())
                .filter(index -> MAPPED_ATTRIBUTE_NAMES.containsKey(elements2.get(index).child(0).text()))
                .mapToObj(index -> new Pair<>(elements2.get(index).child(0).text(), elements2.get(index).child(1).text()))
                .collect(Collectors.toMap(pair -> MAPPED_ATTRIBUTE_NAMES.get(pair.getKey()), Pair::getValue, (v1, v2) -> v1));

        Map<String, String> finalAttributes = new HashMap<>();
        finalAttributes.putAll(attributes);
        finalAttributes.putAll(attributes2);

        finalAttributes.entrySet()
                .forEach(entry -> entry.setValue(ATTRIBUTE_TYPE_ACTIONS.get(entry.getKey()).apply(entry.getValue())));

        finalAttributes.put("name", name);
        finalAttributes.put("price", price);

        return finalAttributes;
    }
}
