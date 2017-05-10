package extractor;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws IOException {
        //testExtractor(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx540-hs"));
        testExtractor(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-6300-body-kit/specifications"));
    }


    static void testExtractor(CameraDomainExtractor extractor, URL url) throws IOException {
        System.out.println("Extracting page content from " + extractor.domain());
        Map<String, String> content = extractor.extractWebSiteContent(Jsoup.parse(url, 0), url);
        content.entrySet()
                .forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue()));
    }
}















