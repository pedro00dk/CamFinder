package extractor.specific;


import extractor.CameraDomainExtractor;
import javafx.util.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CurrysExtractor implements CameraDomainExtractor {

    public static final Map<String, String> MAPPED_ATTRIBUTE_NAMES;

    public static final Map<String, Function<String, String>> ATTRIBUTE_TYPE_ACTIONS;

    static {
        Map<String, String> mappedAttributeNames = new HashMap<>();
        mappedAttributeNames.put("Resolution", "Megapixels");
        mappedAttributeNames.put("Memory card", "Storage Mode");
        mappedAttributeNames.put("Optical zoom", "Zoom");
        mappedAttributeNames.put("ISO sensitivity", "Sensitivity");
        mappedAttributeNames.put("Shutter", "Shutter Speed");
        mappedAttributeNames.put("Shutter speed", "Shutter Speed");
        mappedAttributeNames.put("Size", "Sensor Size");

        MAPPED_ATTRIBUTE_NAMES = Collections.unmodifiableMap(mappedAttributeNames);

        Map<String, Function<String, String>> attributeTypeActions = new HashMap<>();
        attributeTypeActions.put("Megapixels", Function.identity());
        attributeTypeActions.put("Zoom", Function.identity());
        attributeTypeActions.put("Storage Mode", Function.identity());
        attributeTypeActions.put("Sensitivity", Function.identity());
        attributeTypeActions.put("Shutter Speed", Function.identity());
        attributeTypeActions.put("Sensor Size", Function.identity());

        ATTRIBUTE_TYPE_ACTIONS = Collections.unmodifiableMap(attributeTypeActions);
    }

    @Override
    public Map<String, String> extractWebSiteContent(Document document) {
        try {
            //get camera name
            String name = document.getElementsByTag("h1").select(".page-title.nosp").text();

            // get price
            String price = document.getElementsByTag("div").select(".prd-amounts").get(0).text();

            // get all attributes
            List<Element> tableData = document.getElementsByTag("tr").stream()
                    .filter(data -> data.children().size() == 2).collect(Collectors.toList());

            Map<String, String> attributes = IntStream.range(0, tableData.size())
                    .filter(index -> MAPPED_ATTRIBUTE_NAMES.containsKey(tableData.get(index).child(0).text()))
                    .mapToObj(index -> new Pair<>(tableData.get(index).child(0).text(), tableData.get(index).child(1).text()))
                    .collect(Collectors.toMap(pair -> MAPPED_ATTRIBUTE_NAMES.get(pair.getKey()), Pair::getValue, (v1, v2) -> (v1)));

            //processing values
            attributes.entrySet()
                    .forEach(entry -> entry.setValue(ATTRIBUTE_TYPE_ACTIONS.get(entry.getKey()).apply(entry.getValue())));

            attributes.put("name", name);
            attributes.put("price", price);
            return attributes;
        } catch (NullPointerException e) {
            return new HashMap<>();
        }
    }

}
