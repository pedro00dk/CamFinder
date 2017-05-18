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

public class NewEggExtractor implements CameraDomainExtractor {

    public static final Map<String, String> MAPPED_ATTRIBUTE_NAMES;

    public static final Map<String, Function<String, String>> ATTRIBUTE_TYPE_ACTIONS;

    static {
        Map<String, String> mappedAttributeNames = new HashMap<>();
        mappedAttributeNames.put("Effective Pixels", "Megapixels");
        mappedAttributeNames.put("Shutter Speed", "Shutter Speed");
        mappedAttributeNames.put("Image Sensor Size", "Sensor Size");

        MAPPED_ATTRIBUTE_NAMES = Collections.unmodifiableMap(mappedAttributeNames);

        Map<String, Function<String, String>> attributeTypeActions = new HashMap<>();
        attributeTypeActions.put("Megapixels", CameraDomainExtractor::formatMegapixel);
        attributeTypeActions.put("Shutter Speed", Function.identity());
        attributeTypeActions.put("Sensor Size", Function.identity());

        ATTRIBUTE_TYPE_ACTIONS = Collections.unmodifiableMap(attributeTypeActions);
    }

    @Override
    public Map<String, Function<String, String>> getAtrributeTypeActions() {
        return ATTRIBUTE_TYPE_ACTIONS;
    }

    @Override
    public Map<String, String> extractWebSiteContent(Document document) {
        try {
            //get camera name
            String name = document.getElementById("grpDescrip_h").text();

            // get price
            String price = document.getElementsByAttributeValue("itemprop", "price").attr("content");

            // get all attributes
            List<Element> tableData = document.getElementsByTag("dl").stream()
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
