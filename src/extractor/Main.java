package extractor;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws IOException {
        CameraDomainExtractor extractor = new CanonExtractorCamera();
        System.out.println("Extracting page content from " + extractor.domain());
        URL url = new URL("https://shop.usa.canon.com/shop/en/catalog/eos-m10-ef-m-15-45mm-f-3-5-6-3-is-stm-kit-white-refurbished");
        Map<String, String> content = extractor.extractWebSiteContent(Jsoup.parse(url, 0), url);
        content.entrySet()
                .forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue()));
    }
}















