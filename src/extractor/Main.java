package extractor;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws IOException {
        CameraDomainExtractor extractor = new SonyExtractor();
        System.out.println("Extracting page content from " + extractor.domain());
        URL url = new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilca-99m2/specifications");
        Map<String, String> content = extractor.extractWebSiteContent(Jsoup.parse(url, 0), url);
        content.entrySet()
                .forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue()));
    }
}















