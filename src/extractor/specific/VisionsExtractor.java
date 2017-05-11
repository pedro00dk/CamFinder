package extractor.specific;

import extractor.CameraDomainExtractor;
import javafx.util.Pair;
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
import java.util.stream.IntStream;

public class VisionsExtractor implements CameraDomainExtractor {
    public static final Map<String, String> MAPPED_ATTRIBUTE_NAMES;

    public static final Map<String, Function<String, String>> ATTRIBUTE_TYPE_ACTIONS;

    static {
        Map<String, String> mappedAttributeNames = new HashMap<>();
        mappedAttributeNames.put("# of Megapixels", "Megapixels");
        mappedAttributeNames.put("Optical Zoom (Cameras)", "Zoom");
        mappedAttributeNames.put("Memory Card Type (Cameras)", "Storage Mode");
        mappedAttributeNames.put("ISO Ratings / Sensitivity (Cameras)", "Sensitivity");
        mappedAttributeNames.put("Shutter Speed (Cameras)", "Shutter Speed");


        MAPPED_ATTRIBUTE_NAMES = Collections.unmodifiableMap(mappedAttributeNames);

        //TODO funções específicas ou função geral
        Map<String, Function<String, String>> attributeTypeActions = new HashMap<>();
        attributeTypeActions.put("Megapixels", CameraDomainExtractor::formatMegapixel);
        attributeTypeActions.put("Zoom", CameraDomainExtractor::formatZoom);
        attributeTypeActions.put("Storage Mode", Function.identity());
        attributeTypeActions.put("Sensitivity", Function.identity());
        attributeTypeActions.put("Shutter Speed", Function.identity());

        ATTRIBUTE_TYPE_ACTIONS = Collections.unmodifiableMap(attributeTypeActions);
    }

    @Override
    public Map<String, String> extractWebSiteContent(Document document, URL link) {
        //get camera name
        String name = document.getElementById("ctl00_ContentPlaceHolder1_ctrlProdDetailUC_lblProdTitle").text();
        // get price
        String price = document.getElementById("ctl00_ContentPlaceHolder1_ctrlProdDetailUC_lblSaleprice").text();

        // get all attributes
        Elements tableData = document.getElementById("productdetail-features").child(0).children();

        Map<String, String> attributes = IntStream.range(0, tableData.size())
                .filter(index -> MAPPED_ATTRIBUTE_NAMES.containsKey(tableData.get(index).child(0).text()))
                .mapToObj(index -> new Pair<>(tableData.get(index).child(0).text(), tableData.get(index).child(1).text()))
                .collect(Collectors.toMap(pair -> MAPPED_ATTRIBUTE_NAMES.get(pair.getKey()), Pair::getValue));

        //processing values
        attributes.entrySet()
                .forEach(entry -> entry.setValue(ATTRIBUTE_TYPE_ACTIONS.get(entry.getKey()).apply(entry.getValue())));

        attributes.put("name", name);
        attributes.put("price", price);
        return attributes;
    }
}
