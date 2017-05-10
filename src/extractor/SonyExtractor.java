package extractor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

public class SonyExtractor implements CameraDomainExtractor{
    public static final Map<String, String> MAPPED_ATTRIBUTE_NAMES;

    public static final Map<String, Function<String, String>> ATTRIBUTE_TYPE_ACTIONS;

    static {
        Map<String, String> mappedAttributeNames = new HashMap<>();
        mappedAttributeNames.put("Number of Pixels (total)", "Megapixels");
        mappedAttributeNames.put("Digital Zoom (Still Image)", "Zoom");
        mappedAttributeNames.put("Compatible Recording Media", "Storage Mode");
        mappedAttributeNames.put("ISO Sensitivity (Movie)", "Sensitivity");
        mappedAttributeNames.put("Shutter Speed", "Shutter Speed");
        mappedAttributeNames.put("Sensor Type","Sensor Size");

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
    public Map<String, String> extractWebSiteContent(Document document, URL link) throws MalformedURLException {
        // get camera name

        // get camera name
        String name = document.select(".primary-link.l3.breadcrumb-link").text();
        // get price
        String price = document.select(".price.p1").get(0).child(0).text();

        // get all attributes
        Elements keys = document.getElementsByTag("dt").select(".l3");
        Elements values = document.getElementsByTag("dd").select(".p3");
        //set attributes
        Map<String, String> attributes  = new HashMap<>();
        IntStream.range(0, keys.size())
        .forEach(index -> {
            if(MAPPED_ATTRIBUTE_NAMES.containsKey(keys.get(index).text())){
                attributes.put(keys.get(index).text(), values.get(index).text());
            }
        });
        
        attributes.put("name", name);
        attributes.put("price", price);

        return attributes;
    }
}
