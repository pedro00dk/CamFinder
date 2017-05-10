package extractor;

import javafx.util.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SigmaPhotoExtractor implements CameraDomainExtractor {
    public static final Map<String, String> MAPPED_ATTRIBUTE_NAMES;

    public static final Map<String, Function<String, String>> ATTRIBUTE_TYPE_ACTIONS;

    static {
        Map<String, String> mappedAttributeNames = new HashMap<>();
    //    mappedAttributeNames.put("Total Pixels", "Megapixels");
        mappedAttributeNames.put("Storage Media", "Storage Mode");
        mappedAttributeNames.put("ISO Sensitivity", "Sensitivity");
        mappedAttributeNames.put("Shutter Speed", "Shutter Speed");
        mappedAttributeNames.put("Sensor Size", "Sensor Size");

        MAPPED_ATTRIBUTE_NAMES = Collections.unmodifiableMap(mappedAttributeNames);

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
    public Map<String, String> extractWebSiteContent(Document document, URL link) throws MalformedURLException {
        //get camera name
        String name = document.getElementsByTag("h1").select(".product-title").text();
        // get price
        String price = document.getElementsByTag("span").select(".regular-price").first().text();
                //document.getElementById("product-price-29921").text();


         List<Element> a = document.getElementsByTag("tr").stream()
                .filter(data -> data.children().size()>=2).collect(Collectors.toList());
        //document.getElementsByTag("div").select(".std.tech-specs");
        /* // get price
        Elements tableData = document.getElementById("productdetail-features").child(0).children();

        Map<String, String> attributes = IntStream.range(0, tableData.size())
                .filter(index -> MAPPED_ATTRIBUTE_NAMES.containsKey(tableData.get(index).child(0).text()))
                .mapToObj(index -> new Pair<>(tableData.get(index).child(0).text(), tableData.get(index).child(1).text()))
                .collect(Collectors.toMap(pair -> MAPPED_ATTRIBUTE_NAMES.get(pair.getKey()), Pair::getValue));

        //processing values
        attributes.entrySet()
                .forEach(entry -> entry.setValue(ATTRIBUTE_TYPE_ACTIONS.get(entry.getKey()).apply(entry.getValue())));

        attributes.put("name", name);
        attributes.put("price", price);*/
        return null;
    }
}
